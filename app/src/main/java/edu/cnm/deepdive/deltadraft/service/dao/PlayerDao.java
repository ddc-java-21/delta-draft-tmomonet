package edu.cnm.deepdive.deltadraft.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import java.time.Instant;
import java.util.List;

@Dao
public interface PlayerDao {
  @Insert
  Single<Long> _insert(Player player);

  default Single<Player> insert(Player player) {
    return Single
        .just(player)
        .doOnSuccess((u) -> {
          Instant now = Instant.now();
          u.setModified(now);
        })
        .flatMap(this::_insert)
        .map(id -> player);
  }

  @Update
  Single<Integer> _update(Player player);

  default Single<Player> update(Player player){
    return Single
        .just(player)
        .doOnSuccess((u) -> u.setModified(Instant.now()))
        .flatMap(this::update)
        .map(count -> player);
  }


  @Delete
  Single<Integer> delete(Player player);

  @Delete
  Single<Integer> delete(Player... players);

  @Delete
  Single<Integer> delete(List<Player> players);

  //Players by player_id
  @Query("SELECT * FROM player WHERE player_id = :player")
  LiveData<Player> getPlayerById(String player);

  @Query("SELECT * FROM player ORDER BY player_id ASC")
  LiveData<List<Player>> getAll();

//Players by Team
  @Transaction
  @Query("""
  SELECT p.* FROM player p
  INNER JOIN player_team pt ON p.player_id = pt.player_id
  WHERE pt.team_id = :teamId
  """)
  LiveData<List<Player>> getPlayersOnTeam(long teamId);

  // TODO: 7/10/2025 See below
// TODO: 7/10/2025 test 1,2
  @Query("SELECT COUNT(*) FROM player")
  int count();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  Single<List<Long>> insertAll(List<Player> players);

  @Query("SELECT * FROM player")
  Single<List<Player>> selectAll();

  //PlayersByOwner?

  //Image Queries?

  //Players by StatQualifiers?
}
