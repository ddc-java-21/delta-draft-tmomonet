package edu.cnm.deepdive.diceroller;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;

@HiltAndroidApp
public class DiceRollerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
  }
}

