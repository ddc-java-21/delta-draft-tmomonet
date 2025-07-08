package edu.cnm.deepdive.deltadraft.service;

import android.net.Uri;
import androidx.room.Database;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.deltadraft.model.entity.Image;
import edu.cnm.deepdive.deltadraft.model.entity.Note;
import edu.cnm.deepdive.deltadraft.model.entity.User;
import edu.cnm.deepdive.deltadraft.service.NotesDatabase.Converters;
import edu.cnm.deepdive.deltadraft.service.dao.ImageDao;
import edu.cnm.deepdive.deltadraft.service.dao.NoteDao;
import edu.cnm.deepdive.deltadraft.service.dao.UserDao;
import java.time.Instant;

@Database(
    entities = {User.class, Note.class, Image.class},
    version = NotesDatabase.VERSION
)
@TypeConverters({Converters.class})
public abstract class NotesDatabase extends androidx.room.RoomDatabase {

  static final int VERSION = 1;
  private static final String NAME = "notes-db";
  public static String getName() {
    return NAME;
  }

  public abstract UserDao getUserDao();

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
