package edu.cnm.deepdive.deltadraft.model.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.time.Instant;

@Entity(tableName = "player",
    indices = {
        @Index(value = {"player_id"}, unique = true),
        @Index(value = {"player_name"}, unique = true)
    })
public class Player {

  @PrimaryKey(autoGenerate = false)
  @NonNull
  @ColumnInfo(name = "player_id", collate = ColumnInfo.NOCASE)
  private String playerId;

  @ColumnInfo(name = "owner_id", index = true)
  private long ownerId;

  private String image;

  @NonNull
  private Instant modified = Instant.now();

  @NonNull
  @ColumnInfo(name = "player_name", collate = ColumnInfo.NOCASE)
  private String playerName;

  private String position;

  @ColumnInfo(name = "team_mlb", index = true)
  private String teamMlb;

  private float avg;
  private float babip;
  private float hardHit;
  private int delta;
  private float exitVelo;

  @NonNull
  public String getPlayerId() {
    return playerId;
  }

  public String getId() {
    return playerId;
  }

  public long getOwnerId() {
    return ownerId;
  }

  @NonNull
  public String getPlayerName() {
    return playerName;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {}

  public void setPlayerId(@NonNull String playerId) {
    this.playerId = playerId;
  }

  public void setOwnerId(long ownerId) {
    this.ownerId = ownerId;
  }

  @NonNull
  public Instant getModified() {
    return modified;
  }

  public void setModified(@NonNull Instant modified) {
    this.modified = modified;
  }

  public void setPlayerName(@NonNull String playerName) {
    this.playerName = playerName;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getTeamMlb() {
    return teamMlb;
  }

  public void setTeamMlb(String teamMlb) {
    this.teamMlb = teamMlb;
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

  public float getHardHit() {
    return hardHit;
  }

  public void setHardHit(float hardHit) {
    this.hardHit = hardHit;
  }

  public int getDelta() {
    return delta;
  }

  public void setDelta(int delta) {
    this.delta = delta;
  }

  public float getExitVelo() {
    return exitVelo;
  }

  public void setExitVelo(float exitVelo) {
    this.exitVelo = exitVelo;
  }

}
