package edu.cnm.deepdive.deltadraft.service;

import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerDao;
import edu.cnm.deepdive.deltadraft.service.util.PlayerHelper;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.List;

public class PlayerRepository {

  private final PlayerDao playerDao;

  @Inject
  public PlayerRepository(PlayerDao playerDao) {
    this.playerDao = playerDao;
  }

  public void refreshPlayers() {
    new Thread(() -> {
      try {
        List<Player> players = PlayerHelper.scrapePlayers();
        for (Player player : players) {
          playerDao.insert(player); // Or insertAll(players)
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
  }
}