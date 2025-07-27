package edu.cnm.deepdive.deltadraft.service.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playerteam.PlayerTeamCrossRef;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playerteam.TeamWithPlayers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlayerTeamCrossRefDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Completable insert(PlayerTeamCrossRef crossRef);

  @Delete
  Completable delete(PlayerTeamCrossRef crossRef);

  @Transaction
  @Query("SELECT * FROM team WHERE team_id = :teamId")
  Flowable<TeamWithPlayers> getTeamWithPlayers(long teamId);
}
