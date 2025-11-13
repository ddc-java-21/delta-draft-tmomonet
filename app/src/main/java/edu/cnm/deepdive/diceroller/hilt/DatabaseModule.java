package edu.cnm.deepdive.diceroller.hilt;

import android.content.Context;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.diceroller.service.DiceRollerDatabase;
import edu.cnm.deepdive.diceroller.service.dao.PlayerDao;
import edu.cnm.deepdive.diceroller.service.dao.PlayerTeamCrossRefDao;
import edu.cnm.deepdive.diceroller.service.dao.TeamDao;
import edu.cnm.deepdive.diceroller.service.dao.UserDao;
import edu.cnm.deepdive.diceroller.service.dao.UserPlayerCrossRefDao;
import edu.cnm.deepdive.diceroller.service.util.Preloader;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

  @Provides
  @Singleton
  DiceRollerDatabase provideDatabase(@ApplicationContext Context context, Preloader preloader) {
    return Room.databaseBuilder(context, DiceRollerDatabase.class, DiceRollerDatabase.getName())
        .addCallback(preloader)
        .build();
  }

  @Provides
  @Singleton
  UserDao provideUserDao(DiceRollerDatabase database) {
    return database.getUserDao();
  }

  @Provides
  @Singleton
  PlayerDao providePlayerDao(DiceRollerDatabase database) {
    return database.getPlayerDao();
  }

  @Provides
  @Singleton
  TeamDao provideTeamDao(DiceRollerDatabase database) {
    return database.getTeamDao();
  }

  @Provides
  @Singleton
  PlayerTeamCrossRefDao providePlayerTeamCrossRefDao(DiceRollerDatabase database) {
    return database.getPlayerTeamCrossRefDao();
  }

  @Provides
  @Singleton
  UserPlayerCrossRefDao provideUserPlayerCrossRefDao(DiceRollerDatabase database) {
    return database.getUserPlayerCrossRefDao();
  }

}
