package edu.cnm.deepdive.deltadraft.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.time.Instant;

@Entity(tableName = "user_team",
    foreignKeys = @ForeignKey (
      entity = User.class,
        parentColumns = "user_id",
        childColumns = "owner_id",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {
        @Index(value = {"team_id"}, unique = true),
        @Index(value = {"team_name"}, unique = true)
    })



public class Team {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "team_id", collate = ColumnInfo.NOCASE)
  private long id;

  @NonNull
  @ColumnInfo(name = "team_name", collate = ColumnInfo.NOCASE)
  private String teamName ="";

  @NonNull
  @ColumnInfo(name = "owner_id", index = true)
  private long ownerId;

  private Instant created = Instant.now();

  @NonNull
  private Instant modified = Instant.now();

  public long getId() {
    return id;
  }

  public long getOwnerId() {
    return ownerId;
  }

  @NonNull
  public String getTeamName() {
    return teamName;
  }

  public Team setTeamName(@NonNull String teamName) {
    this.teamName = teamName;
    return this;
  }


}
