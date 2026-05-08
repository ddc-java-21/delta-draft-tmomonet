package edu.cnm.deepdive.diceroller.service;

import android.content.Context;
import android.net.Uri;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.diceroller.model.entity.User;
import edu.cnm.deepdive.diceroller.service.DicerollerDatabase.Converters;
import edu.cnm.deepdive.diceroller.service.dao.UserDao;
import java.net.URL;
import java.time.Instant;

@Database(
    entities = {User.class },
    version = DicerollerDatabase.VERSION
)
@TypeConverters({Converters.class})
public abstract class DicerollerDatabase extends androidx.room.RoomDatabase {

  private static volatile DicerollerDatabase instance;
  static final int VERSION = 1;
  private static final String NAME = "diceroller-db";
  public static String getName() {
    return NAME;
  }

  public abstract UserDao getUserDao();

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

  public static DicerollerDatabase getInstance(Context context) {
    if (instance == null) {
      synchronized (DicerollerDatabase.class) {
        if (instance == null) {
          instance = Room.databaseBuilder(
              context.getApplicationContext(),
              DicerollerDatabase.class,
              "diceroller_db"
          ).build();
        }
      }
    }
    return instance;
  }



}
