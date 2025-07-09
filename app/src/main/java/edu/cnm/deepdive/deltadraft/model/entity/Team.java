package edu.cnm.deepdive.deltadraft.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.time.Instant;

@Entity(tableName = "team",
    indices = {
        @Index(value = {"team_id"}, unique = true),
        @Index(value = {"team_name"}, unique = true)
    })

public class Team {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "team_id", collate = ColumnInfo.NOCASE)
  private long teamId;

  @NonNull
  @ColumnInfo(name = "team_name", collate = ColumnInfo.NOCASE)
  private String teamName ="";

  @NonNull
  @ColumnInfo(name = "owner_id", index = true)
  private long ownerId;

  private Instant created = Instant.now();

  @NonNull
  private Instant modified = Instant.now();

  public long getTeamId() {
    return teamId;
  }

  public long getOwnerId() {
    return ownerId;
  }

  @NonNull
  public String getTeamName() {
    return teamName;
  }

  public Instant getCreated() {
    return created;
  }

  @NonNull
  public Instant getModified() {
    return modified;
  }

  public void setOwnerId(long ownerId) {
    this.ownerId = ownerId;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }

  public void setModified(@NonNull Instant modified) {
    this.modified = modified;
  }

  public void setTeamName(@NonNull String teamName) {
    this.teamName = teamName;
  }


}
