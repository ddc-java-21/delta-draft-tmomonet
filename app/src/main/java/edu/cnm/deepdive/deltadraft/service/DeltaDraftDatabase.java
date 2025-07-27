package edu.cnm.deepdive.deltadraft.service;

import android.content.Context;
import android.net.Uri;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.model.entity.Team;
import edu.cnm.deepdive.deltadraft.model.entity.User;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playerteam.PlayerTeamCrossRef;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser.UserPlayerCrossRef;
import edu.cnm.deepdive.deltadraft.service.DeltaDraftDatabase.Converters;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerDao;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerTeamCrossRefDao;
import edu.cnm.deepdive.deltadraft.service.dao.TeamDao;
import edu.cnm.deepdive.deltadraft.service.dao.UserDao;
import edu.cnm.deepdive.deltadraft.service.dao.UserPlayerCrossRefDao;
import java.net.URL;
import java.time.Instant;

@Database(
    entities = {User.class, Player.class, Team.class,
        PlayerTeamCrossRef.class, UserPlayerCrossRef.class },
    version = DeltaDraftDatabase.VERSION
)
@TypeConverters({Converters.class})
public abstract class DeltaDraftDatabase extends androidx.room.RoomDatabase {

  private static volatile DeltaDraftDatabase instance;
  static final int VERSION = 1;
  private static final String NAME = "deltadraft-db";
  public static String getName() {
    return NAME;
  }

  public abstract UserDao getUserDao();

  public abstract UserPlayerCrossRefDao getUserPlayerCrossRefDao();

  public abstract PlayerTeamCrossRefDao getPlayerTeamCrossRefDao();

  public abstract PlayerDao getPlayerDao();

  public abstract TeamDao getTeamDao();

  public static class Converters {

    @TypeConverter
    public static Long fromInstant(Instant value) {
      return (value == null) ? null : value.toEpochMilli();
    }

    @TypeConverter
    public static String fromUrl(URL url) {
      return (url == null) ? null : url.toString();
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

  public static DeltaDraftDatabase getInstance(Context context) {
    if (instance == null) {
      synchronized (DeltaDraftDatabase.class) {
        if (instance == null) {
          instance = Room.databaseBuilder(
              context.getApplicationContext(),
              DeltaDraftDatabase.class,
              "delta_draft_db"
          ).build();
        }
      }
    }
    return instance;
  }



}
