package edu.cnm.deepdive.deltadraft.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.deltadraft.model.entity.Team;
import edu.cnm.deepdive.deltadraft.model.entity.User;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playerteam.TeamWithPlayers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.time.Instant;
import java.util.List;

@Dao
public interface TeamDao {
  @Insert
  Single<Long> _insert(Team team);

  default Single<Team> insert(Team team) {
    return Single
        .just(team)
        .doOnSuccess((u) -> {
          Instant now = Instant.now();
          u.setCreated(now);
          u.setModified(now);
        })
        .flatMap(this::_insert)
        .doOnSuccess(team::setTeamId)
        .map(id -> team);
  }

  @Update
  Single<Integer> _update(User user);

  default Single<User> update(User user){
    return Single
        .just(user)
        .doOnSuccess((u) -> u.setModified(Instant.now()))
        .flatMap(this::update)
        .map(count -> user);
  }


  @Delete
  Single<Integer> delete(Team team);

  @Delete
  Single<Integer> delete(Team... teams);

  @Delete
  Single<Integer> delete(List<Team> teams);

  @Query("SELECT * FROM team WHERE team_id = :teamId")
  LiveData<Team> select(long teamId);

  @Transaction
  @Query("""
      SELECT * FROM team
      WHERE team_id IN (
        SELECT team_id FROM player_team WHERE player_id = :playerId
      )
  """)
  LiveData<List<TeamWithPlayers>> getTeamsForPlayer(String playerId);

  @Query("INSERT INTO player_team (player_id, team_id) VALUES (:playerId, :teamId)")
  Completable link(String playerId, long teamId);

}
