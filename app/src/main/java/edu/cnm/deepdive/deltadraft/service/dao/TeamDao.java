package edu.cnm.deepdive.deltadraft.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.deltadraft.model.entity.Team;
import edu.cnm.deepdive.deltadraft.model.entity.User;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import java.time.Instant;
import java.util.List;

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
        .doOnSuccess(team::setId)
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

  @Query("SELECT * FROM user_team WHERE owner_id = :teamId")
  LiveData<User> select(long userId);


  @Query("SELECT * FROM user_team ORDER BY owner_id ASC")
  LiveData<List<Team>> selectAll();

}
