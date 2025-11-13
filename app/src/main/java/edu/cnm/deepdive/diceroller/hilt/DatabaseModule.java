package edu.cnm.deepdive.diceroller.hilt;

import android.content.Context;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.diceroller.service.DiceRollerDatabase;
import edu.cnm.deepdive.diceroller.service.dao.UserDao;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

  @Provides
  @Singleton
  UserDao provideUserDao(DiceRollerDatabase database) {
    return database.getUserDao();
  }

}
