//package edu.cnm.deepdive.deltadraft.service.util;
//
//import edu.cnm.deepdive.deltadraft.model.entity.Player;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//
//public class PlayerHelpers {
//
//
//  public static void setPlayerThumbnail(Player player) {
//    try {
//      String playerId = player.getId(); // e.g., "deverra01"
//      String url = "https://www.baseball-reference.com/players/"
//          + playerId.charAt(0) + "/" + playerId + ".shtml";
//
//      Document doc = Jsoup.connect(url).get();
//
//      // The player's name should match the alt text: "Photo of <Name>"
//      String expectedAlt = "Photo of " + player.getPlayerName();
//
//      Element img = doc.select("img[alt='" + expectedAlt + "']").first();
//
//      if (img != null) {
//        String thumbnailUrl = img.absUrl("src");
//        player.setImage(thumbnailUrl);
//      } else {
//        System.err.println("Thumbnail image not found for " + player.getPlayerName());
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}
