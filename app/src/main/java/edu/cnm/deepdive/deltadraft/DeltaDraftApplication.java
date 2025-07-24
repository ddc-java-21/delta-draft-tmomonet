package edu.cnm.deepdive.deltadraft;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerDao;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;

@HiltAndroidApp
public class DeltaDraftApplication extends Application {

  @Inject
  PlayerDao playerDao;

  @Override
  public void onCreate() {
    super.onCreate();
    playerDao.delete()
        .subscribeOn(
            Schedulers.io()
        )
        .subscribe();
  }
}

