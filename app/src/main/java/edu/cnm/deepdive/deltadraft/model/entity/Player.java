package edu.cnm.deepdive.deltadraft.model.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.time.Instant;

@Entity(tableName = "user_team",
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "user_id",
        childColumns = "owner_id",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {
        @Index(value = {"player_id"}, unique = true),
        @Index(value = {"player_name"}, unique = true)
    })
public class Player {

  @PrimaryKey(autoGenerate = false)
  private String id;

  private long owner_id;

  @NonNull
  private Instant modified = Instant.now();

  @NonNull
  private String player_name;

  private String position;

  private String team_mlb;

  private float avg;

  private float babip;

  private float barrel_rate;

  private int delta;

  private float exit_velo;

  private float launch_angle;

  // TODO: 7/8/2025 Ask Nick about setters when accessing data from external sources?

  public String getId() {
    return id;
  }

  public long getOwner_id() {
    return owner_id;
  }

  @NonNull
  public String getPlayer_name() {
    return player_name;
  }

  public String getPosition() {
    return position;
  }

  public String getTeam_mlb() {
    return team_mlb;
  }

  public float getAvg() {
    return avg;
  }

  public float getBabip() {
    return babip;
  }

  public float getBarrel_rate() {
    return barrel_rate;
  }

  public int getDelta() {
    return delta;
  }

  public float getExit_velo() {
    return exit_velo;
  }

  public float getLaunch_angle() {
    return launch_angle;
  }

  @NonNull
  public Instant getModified() {
    return modified;
  }

  public Player setModified(@NonNull Instant modified) {
    this.modified = modified;
    return this;
  }
}
