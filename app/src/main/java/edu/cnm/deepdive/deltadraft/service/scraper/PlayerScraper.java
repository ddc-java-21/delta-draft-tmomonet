package edu.cnm.deepdive.deltadraft.service.scraper;

import edu.cnm.deepdive.deltadraft.model.entity.Player;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class PlayerScraper {

  private static final String STANDARD_URL =
      "https://www.baseball-reference.com/leagues/majors/2025-standard-batting.shtml";
  private static final String ADVANCED_URL =
      "https://www.baseball-reference.com/leagues/majors/2025-advanced-batting.shtml";

  private static final Map<String, String> POSITION_MAP = Map.ofEntries(
      Map.entry("1", "P"),
      Map.entry("2", "C"),
      Map.entry("3", "1B"),
      Map.entry("4", "2B"),
      Map.entry("5", "3B"),
      Map.entry("6", "SS"),
      Map.entry("7", "LF"),
      Map.entry("8", "CF"),
      Map.entry("9", "RF"),
      Map.entry("D", "DH"),
      Map.entry("H", "PH")
  );

  public static List<Player> scrapePlayers() throws IOException {

    Map<String, Float> babipMap = new HashMap<>();
    Map<String, Float> hardHitMap = new HashMap<>();
    Map<String, Float> evMap = new HashMap<>();
    Map<String, Float> launchMap = new HashMap<>();
    Set<String> multiTeamPlayers = new HashSet<>();

    // === Scrape Advanced Batting ===
    Document advancedDoc = Jsoup.connect(ADVANCED_URL).get();
    Element advancedTable = extractCommentedTable(advancedDoc, "advanced_batting");

// Extract from commented-out HTML if necessary
    for (Node node : advancedDoc.body().childNodes()) {
      if (node instanceof Comment) {
        String data = ((Comment) node).getData();
        if (data.contains("id=\"advanced_batting\"")) {
          Document commentDoc = Jsoup.parse(data);
          advancedTable = commentDoc.selectFirst("table#advanced_batting");
          break;
        }
      }
    }

    if (advancedTable == null) {
      throw new IllegalStateException("Advanced table not found in commented HTML");
    }

    for (Element row : advancedTable.select("tbody tr")) {
      if (row.hasClass("thead"))
        continue; // skip repeated header rows

      Element link = row.selectFirst("a[href^=/players/]");
      if (link == null)
        continue;

      String playerId = extractPlayerId(link.attr("href"));
      if (playerId.isEmpty())
        continue;

      String team = getStat(row, "team_ID");

      float babip = parseFloatSafe(getStat(row, "BABIP"));
      float hardHit = parseFloatSafe(getStat(row, "HardHitPct"));
      float ev = parseFloatSafe(getStat(row, "avg_hit_speed"));
      float launch = parseFloatSafe(getStat(row, "launch_angle"));

      if (team.matches("\\dTM")) {
        multiTeamPlayers.add(playerId);
        babipMap.put(playerId, babip);
        hardHitMap.put(playerId, hardHit);
        evMap.put(playerId, ev);
        launchMap.put(playerId, launch);
      } else if (!babipMap.containsKey(playerId)) {
        babipMap.put(playerId, babip);
        hardHitMap.put(playerId, hardHit);
        evMap.put(playerId, ev);
        launchMap.put(playerId, launch);
      }
    }

    // === Scrape Standard Batting ===
    Document standardDoc = Jsoup.connect(STANDARD_URL).get();
    Element standardTable = null;

// Search through comment nodes in the body
    for (Node node : standardDoc.body().childNodes()) {
      if (node instanceof Comment) {
        String data = ((Comment) node).getData();
        if (data.contains("id=\"players_standard_batting\"")) {
          Document commentDoc = Jsoup.parse(data);
          standardTable = commentDoc.selectFirst("table#players_standard_batting");
          break;
        }
      }
    }

    if (standardTable == null)
      throw new IllegalStateException("Standard table not found in commented HTML");

    Map<String, Player> playersById = new HashMap<>();

    for (Element row : standardTable.select("tbody tr")) {
      if (row.hasClass("thead"))
        continue; // skip repeated header rows

      Element link = row.selectFirst("a[href^=/players/]");
      if (link == null)
        continue;

      String playerId = extractPlayerId(link.attr("href"));
      if (playerId.isEmpty())
        continue;

      String playerName = link.text();
      String team = getStat(row, "team_ID");

      if (!team.matches("\\dTM") && multiTeamPlayers.contains(playerId))
        continue;
      if (playersById.containsKey(playerId))
        continue;

      float avg = babipMap.getOrDefault(playerId, .000f);
      float babip = babipMap.getOrDefault(playerId, .000f);
      int delta = Math.round((babip - avg) * 1000);

      float hardHit = hardHitMap.getOrDefault(playerId, .000f);
      float ev = evMap.getOrDefault(playerId, .000f);
      float launch = launchMap.getOrDefault(playerId, .000f);

      String rawPos = getStat(row, "pos");
      String position = parsePrimaryPosition(rawPos);

      Player player = new Player();
      player.setPlayer_id(playerId);
      player.setPlayer_name(playerName);
      player.setTeam_mlb(team != null ? team : "");
      player.setAvg(avg);
      player.setBabip(babip);
      player.setDelta(delta);
      player.setHardHit(hardHit);
      player.setExit_velo(ev);
      player.setLaunch_angle(launch);
      player.setPosition(position);
      player.setModified(Instant.now());

      playersById.put(playerId, player);
    }

    return new ArrayList<>(playersById.values());
  }

  private static String extractPlayerId(String href) {
    if (href == null || !href.contains("/players/") || !href.endsWith(".shtml")) return "";
    String[] parts = href.split("/");
    return parts.length > 0 ? parts[parts.length - 1].replace(".shtml", "") : "";
  }

  private static String getStat(Element row, String statName) {
    Element cell = row.selectFirst("td[data-stat=" + statName + "]");
    return (cell != null) ? cell.text().trim() : "";
  }

  private static float parseFloatSafe(String str) {
    if (str == null || str.isEmpty() || str.equals("--")) return 0f;
    try {
      return Float.parseFloat(str);
    } catch (NumberFormatException e) {
      return 0f;
    }
  }

  private static String parsePrimaryPosition(String raw) {
    if (raw == null || raw.isEmpty()) return "";
    String code = raw.split("/")[0].trim();
    return POSITION_MAP.getOrDefault(code, code); // fallback to code if unknown
  }

  private static Element extractCommentedTable(Document doc, String tableId) {
    for (Node node : doc.body().childNodes()) {
      if (node instanceof Comment) {
        String data = ((Comment) node).getData();
        if (data.contains("id=\"" + tableId + "\"")) {
          Document commentDoc = Jsoup.parse(data);
          return commentDoc.getElementById(tableId);
        }
      }
    }
    return null;
  }

}
