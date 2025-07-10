package edu.cnm.deepdive.deltadraft.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.deltadraft.model.entity.Team;
import edu.cnm.deepdive.deltadraft.model.entity.User;
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

  // TODO: 7/10/2025 List<Team> teams by owner

}
