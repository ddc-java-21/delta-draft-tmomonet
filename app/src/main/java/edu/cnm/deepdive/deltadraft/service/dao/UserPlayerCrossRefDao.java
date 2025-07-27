package edu.cnm.deepdive.deltadraft.service.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser.PlayerWithUsers;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser.UserPlayerCrossRef;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface UserPlayerCrossRefDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Completable insert(UserPlayerCrossRef crossRef);

  @Delete
  Completable delete(UserPlayerCrossRef crossRef);

  @Transaction
  @Query("SELECT * FROM player WHERE player_id = :playerId")
  Flowable<PlayerWithUsers> getPlayerWithUsers(String playerId);
}

