package edu.cnm.deepdive.deltadraft.model.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.time.Instant;

@Entity(tableName = "player_user_team",
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "user_id",
        childColumns = "owner_id",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {
        @Index(value = {"player_id"}, unique = true),
        @Index(value = {"player_name"}, unique = true),
        @Index(value = {"owner_id"}, unique = true)
    })
public class Player {

  @PrimaryKey(autoGenerate = false)
  @NonNull
  private String player_id;

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


  @NonNull
  public String getPlayer_id() {
    return player_id;
  }

  public String getId() {
    return player_id;
  }

  public long getOwner_id() {
    return owner_id;
  }

  @NonNull
  public String getPlayer_name() {
    return player_name;
  }

  public void setPlayer_id(@NonNull String player_id) {
    this.player_id = player_id;
  }

  public void setOwner_id(long owner_id) {
    this.owner_id = owner_id;
  }

  @NonNull
  public Instant getModified() {
    return modified;
  }

  public void setModified(@NonNull Instant modified) {
    this.modified = modified;
  }

  public void setPlayer_name(@NonNull String player_name) {
    this.player_name = player_name;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getTeam_mlb() {
    return team_mlb;
  }

  public void setTeam_mlb(String team_mlb) {
    this.team_mlb = team_mlb;
  }

  public float getAvg() {
    return avg;
  }

  public void setAvg(float avg) {
    this.avg = avg;
  }

  public float getBabip() {
    return babip;
  }

  public void setBabip(float babip) {
    this.babip = babip;
  }

  public float getBarrel_rate() {
    return barrel_rate;
  }

  public void setBarrel_rate(float barrel_rate) {
    this.barrel_rate = barrel_rate;
  }

  public int getDelta() {
    return delta;
  }

  public void setDelta(int delta) {
    this.delta = delta;
  }

  public float getExit_velo() {
    return exit_velo;
  }

  public void setExit_velo(float exit_velo) {
    this.exit_velo = exit_velo;
  }

  public float getLaunch_angle() {
    return launch_angle;
  }

  public void setLaunch_angle(float launch_angle) {
    this.launch_angle = launch_angle;
  }
}
