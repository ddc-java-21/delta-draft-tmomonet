package edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import edu.cnm.deepdive.deltadraft.model.entity.User;

@Entity(tableName = "user_player",
    primaryKeys = {"user_id", "player_id"},
    foreignKeys = {@ForeignKey(
        entity = User.class,
        parentColumns = "user_id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE
    )})
public class UserPlayerCrossRef {

  @NonNull
  @ColumnInfo(name = "user_id")
  public Long userId;
  @NonNull
      @ColumnInfo(name = "player_id")
  public String playerId;

  public Long getUserId() {
    return userId;
  }

  public String getPlayerId() {
    return playerId;
  }

}
