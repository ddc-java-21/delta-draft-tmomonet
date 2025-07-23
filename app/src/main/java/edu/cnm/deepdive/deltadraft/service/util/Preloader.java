package edu.cnm.deepdive.deltadraft.service.util;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.deltadraft.R;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerDao;
import edu.cnm.deepdive.deltadraft.service.dao.UserDao;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;

public class Preloader extends RoomDatabase.Callback {

  private final Context context;
  private final Provider<PlayerDao> playerDaoProvider;

  @Inject
  Preloader(@ApplicationContext Context context,
      Provider<UserDao> userDaoProvider,
      Provider<PlayerDao> playerDaoProvider) {
    this.context = context;
    this.playerDaoProvider = playerDaoProvider;
  }

  @Override
  public void onCreate(@NonNull SupportSQLiteDatabase db) {
    super.onCreate(db);
    try (
        InputStream input = context.getResources().openRawResource(R.raw.advanced2025);
        Reader reader = new InputStreamReader(input);
        CSVReader csv=new CSVReaderBuilder(reader).withSkipLines(1).build()
    ){
      List<Player> players = new ArrayList<>();
      String[] line;
      while ((line = csv.readNext()) != null) {
        // parse columns
        Player p = new Player();
        p.setPlayerId(line[35]);
        p.setPlayerName(line[1]);
        p.setPosition(line[2]);
        players.add(p);
        System.out.println(p.getPlayerName());
      }
      Scheduler scheduler = Schedulers.io();
      PlayerDao playerDao = playerDaoProvider.get();
      playerDao.insertAll(players)
          .subscribeOn(scheduler)
          .subscribe();
    } catch (IOException | CsvValidationException e) {
      throw new RuntimeException(e);
    }
  }
}
