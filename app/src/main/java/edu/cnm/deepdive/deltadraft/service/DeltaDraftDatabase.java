package edu.cnm.deepdive.deltadraft.service;

import android.net.Uri;
import androidx.room.Database;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.deltadraft.model.entity.Image;
import edu.cnm.deepdive.deltadraft.model.entity.Note;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.model.entity.Team;
import edu.cnm.deepdive.deltadraft.model.entity.User;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playerteam.PlayerTeamCrossRef;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser.UserPlayerCrossRef;
import edu.cnm.deepdive.deltadraft.service.DeltaDraftDatabase.Converters;
import edu.cnm.deepdive.deltadraft.service.dao.ImageDao;
import edu.cnm.deepdive.deltadraft.service.dao.NoteDao;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerDao;
import edu.cnm.deepdive.deltadraft.service.dao.TeamDao;
import edu.cnm.deepdive.deltadraft.service.dao.UserDao;
import java.time.Instant;

@Database(
    entities = {User.class, Player.class, Team.class, Note.class, Image.class,
        PlayerTeamCrossRef.class, UserPlayerCrossRef.class },
    version = DeltaDraftDatabase.VERSION
)
@TypeConverters({Converters.class})
public abstract class DeltaDraftDatabase extends androidx.room.RoomDatabase {

  static final int VERSION = 1;
  private static final String NAME = "deltadraft-db";
  public static String getName() {
    return NAME;
  }

  public abstract UserDao getUserDao();

  public abstract PlayerDao getPlayerDao();

  public abstract TeamDao getTeamDao();

  // TODO: 7/9/2025 Phase out implementation of prior daos

  public abstract NoteDao getNoteDao();

  public abstract ImageDao getImageDao();

  public static class Converters {
    @TypeConverter
    public static Long fromInstant(Instant value) {
      return (value == null) ? null : value.toEpochMilli();
    }

    @TypeConverter
    public static Instant fromLong(Long value) {
      return (value == null) ? null : Instant.ofEpochMilli(value);
    }

    @TypeConverter
    public static String fromUri(Uri value) {
      return (value == null) ? null : value.toString();
    }

    @TypeConverter
    public static Uri fromString(String value) {
      return (value == null) ? null : Uri.parse(value);
    }
  }

}
