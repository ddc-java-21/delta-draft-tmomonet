package edu.cnm.deepdive.deltadraft.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.deltadraft.model.entity.User;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser.PlayerWithUsers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import java.time.Instant;
import java.util.List;

@Dao
public interface UserDao {

  @Insert
  Single<Long> _insert(User user);

  default Single<User> insert(User user) {
    return Single
        .just(user)
        .doOnSuccess((u) -> {
          Instant now = Instant.now();
          u.setCreated(now);
          u.setModified(now);
        })
        .flatMap(this::_insert)
        .doOnSuccess(user::setId)
        .map(id -> user);
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
  Single<Integer> delete(User user);

  @Delete
  Single<Integer> delete(User... users);

  @Delete
  Single<Integer> delete(List<User> users);

  @Query("SELECT * FROM user WHERE user_id = :userId")
  LiveData<User> select(long userId);

  @Query("SELECT * FROM user WHERE oauth_key = :oauthKey")
  Maybe<User> select(String oauthKey);

  @Query("SELECT * FROM user ORDER BY display_name ASC")
  LiveData<List<User>> selectAll();

  @Transaction
  @Query("SELECT * FROM player WHERE player_id = :playerId")
  LiveData<PlayerWithUsers> getPlayerWithUsers(String playerId);

  @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
  @Query("INSERT INTO user_player (player_id, user_id) VALUES (:playerId, :userId)")
  Completable link(String playerId, long userId);
}
