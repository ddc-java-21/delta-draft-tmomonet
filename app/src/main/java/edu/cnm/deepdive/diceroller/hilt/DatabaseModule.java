package edu.cnm.deepdive.diceroller.hilt;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.diceroller.service.DicerollerDatabase;
import edu.cnm.deepdive.diceroller.service.dao.UserDao;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

  @Provides
  @Singleton
  UserDao provideUserDao(DicerollerDatabase database) {
    return database.getUserDao();
  }

}
