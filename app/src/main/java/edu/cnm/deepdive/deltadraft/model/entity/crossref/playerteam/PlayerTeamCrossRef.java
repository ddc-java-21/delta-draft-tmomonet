package edu.cnm.deepdive.deltadraft.model.entity.crossref.playerteam;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import edu.cnm.deepdive.deltadraft.model.entity.Team;

@Entity(tableName = "player_team",
    primaryKeys = {"player_id", "team_id"},
    foreignKeys = {@ForeignKey(
        entity = Team.class,
        parentColumns = "team_id",
        childColumns = "team_id",
        onDelete = ForeignKey.CASCADE
    )},
    indices = {@Index("player_id"), @Index("team_id")})
public class PlayerTeamCrossRef {

  @NonNull
  @ColumnInfo(name = "player_id")
  String playerId;

  @ColumnInfo(name = "team_id")
  long teamId;

  public String getPlayerId() {
    return playerId;
  }

  public long getTeamId() {
    return teamId;
  }

}
