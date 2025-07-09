package edu.cnm.deepdive.deltadraft.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.model.entity.Team;
import edu.cnm.deepdive.deltadraft.model.entity.User;
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



//  @Query("SELECT * FROM user_team WHERE player_name = :playerId")
//  LiveData<Player> select(long playerId);
//
//
//  @Query("SELECT * FROM user_team ORDER BY player_name ASC")
//  LiveData<List<Team>> selectAll();

}
