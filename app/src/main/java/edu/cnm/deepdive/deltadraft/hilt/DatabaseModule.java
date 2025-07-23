package edu.cnm.deepdive.deltadraft.hilt;

import android.content.Context;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.deltadraft.service.DeltaDraftDatabase;
import edu.cnm.deepdive.deltadraft.service.dao.ImageDao;
import edu.cnm.deepdive.deltadraft.service.dao.NoteDao;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerDao;
import edu.cnm.deepdive.deltadraft.service.dao.TeamDao;
import edu.cnm.deepdive.deltadraft.service.dao.UserDao;
import edu.cnm.deepdive.deltadraft.service.util.Preloader;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

  @Provides
  @Singleton
  DeltaDraftDatabase provideDatabase(@ApplicationContext Context context, Preloader preloader) {
    return Room.databaseBuilder(context, DeltaDraftDatabase.class, DeltaDraftDatabase.getName())
        .addCallback(preloader)
        .build();
  }

  @Provides
  @Singleton
  UserDao provideUserDao(DeltaDraftDatabase database) {
    return database.getUserDao();
  }

  @Provides
  @Singleton
  PlayerDao providePlayerDao(DeltaDraftDatabase database) {
    return database.getPlayerDao();
  }

  @Provides
  @Singleton
  TeamDao provideTeamDao(DeltaDraftDatabase database) {
    return database.getTeamDao();
  }

}
