package edu.cnm.deepdive.deltadraft.hilt;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.deltadraft.service.DeltaDraftDatabase;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerDao;
import edu.cnm.deepdive.deltadraft.service.dao.TeamDao;
import edu.cnm.deepdive.deltadraft.service.dao.UserDao;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

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
