package edu.cnm.deepdive.deltadraft.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.time.Instant;

@Entity(tableName = "user",
    indices = {
        @Index(value = {"oauth_key"}, unique = true),
        @Index(value = {"display_name"}, unique = true)
    })
public class User {

  /*
  C - Create notes
  R - Retrieve notes and images from self, possibly others shared to user
  U - Update notes, captions, and images
  D - Remove notes, images, and captions (account deletion?)
   */

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "user_id", collate = ColumnInfo.NOCASE)
  private long id;

  @NonNull
  @ColumnInfo(name = "oauth_key")
  private String oauthKey = "";

  @NonNull
  @ColumnInfo(name = "display_name", collate = ColumnInfo.NOCASE)
  private String displayName ="";

  private Instant created = Instant.now();

  @NonNull
  private Instant modified = Instant.now();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;

  }

  @NonNull
  public String getOauthKey() {
    return oauthKey;
  }

  public void setOauthKey(@NonNull String oauthKey) {
    this.oauthKey = oauthKey;

  }

  @NonNull
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@NonNull String displayName) {
    this.displayName = displayName;

  }

  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;

  }

  @NonNull
  public Instant getModified() {
    return modified;
  }

  public void setModified(@NonNull Instant modified) {
    this.modified = modified;

  }
}
