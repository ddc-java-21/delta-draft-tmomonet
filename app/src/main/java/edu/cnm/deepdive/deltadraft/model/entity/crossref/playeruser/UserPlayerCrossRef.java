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
  String user_id;
  @NonNull
  String player_id;

  public String getUser_id() {
    return user_id;
  }

  public String getPlayer_id() {
    return player_id;
  }

}
