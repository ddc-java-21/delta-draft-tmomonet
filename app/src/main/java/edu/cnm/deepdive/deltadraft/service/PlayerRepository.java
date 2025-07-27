package edu.cnm.deepdive.deltadraft.service;

import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playerteam.PlayerTeamCrossRef;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playerteam.TeamWithPlayers;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser.PlayerWithUsers;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser.UserPlayerCrossRef;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerDao;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerTeamCrossRefDao;
import edu.cnm.deepdive.deltadraft.service.dao.TeamDao;
import edu.cnm.deepdive.deltadraft.service.dao.UserDao;
import edu.cnm.deepdive.deltadraft.service.dao.UserPlayerCrossRefDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import java.util.List;

public class PlayerRepository {

  private final PlayerDao playerDao;
  private final UserDao userDao;
  private final TeamDao teamDao;
  private final PlayerTeamCrossRefDao playerTeamCrossRefDao;
  private final UserPlayerCrossRefDao userPlayerCrossRefDao;

  @Inject
  public PlayerRepository(PlayerDao playerDao, UserDao userDao, TeamDao teamDao,
      PlayerTeamCrossRefDao playerTeamCrossRefDao,
      UserPlayerCrossRefDao userPlayerCrossRefDao) {
    this.playerDao = playerDao;
    this.userDao = userDao;
    this.teamDao = teamDao;
    this.playerTeamCrossRefDao = playerTeamCrossRefDao;
    this.userPlayerCrossRefDao = userPlayerCrossRefDao;
  }

  public Single<Player> insert(Player player) {
    return playerDao.insert(player);
  }

  public Single update(Player player) {
    return playerDao.update(player);
  }

  public Single delete(Player player) {
    return playerDao.delete(player);
  }

  public Single<List<Player>> getAllPlayers() {
    return playerDao.selectAll();
  }

  public LiveData<Player> getPlayerById(String playerId) {
    return playerDao.getPlayerById(playerId);
  }

  // ========== LINK/UNLINK TEAM ==========

  public Completable linkToTeam(String playerId, long teamId) {
    PlayerTeamCrossRef crossRef = new PlayerTeamCrossRef();
    crossRef.playerId = playerId;
    crossRef.teamId = teamId;
    return playerTeamCrossRefDao.insert(crossRef);
  }

  public Completable unlinkFromTeam(String playerId, long teamId) {
    PlayerTeamCrossRef crossRef = new PlayerTeamCrossRef();
    crossRef.playerId = playerId;
    crossRef.teamId = teamId;
    return playerTeamCrossRefDao.delete(crossRef);
  }

  public Flowable<TeamWithPlayers> getTeamWithPlayers(long teamId) {
    return playerTeamCrossRefDao.getTeamWithPlayers(teamId);
  }

  // ========== LINK/UNLINK USER ==========

  public Completable linkToUser(String playerId, long userId) {
    UserPlayerCrossRef crossRef = new UserPlayerCrossRef();
    crossRef.playerId = playerId;
    crossRef.userId = userId;
    return userPlayerCrossRefDao.insert(crossRef);
  }

  public Completable unlinkFromUser(String playerId, long userId) {
    UserPlayerCrossRef crossRef = new UserPlayerCrossRef();
    crossRef.playerId = playerId;
    crossRef.userId = userId;
    return userPlayerCrossRefDao.delete(crossRef);
  }

  public LiveData<PlayerWithUsers> getPlayerWithUsers(String playerId) {
    return userDao.getPlayerWithUsers(playerId);
  }

  public LiveData<List<TeamWithPlayers>> getTeamsForPlayer(String playerId) {
    return teamDao.getTeamsForPlayer(playerId);
  }
}