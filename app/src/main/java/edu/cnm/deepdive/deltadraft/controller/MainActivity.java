package edu.cnm.deepdive.deltadraft.controller;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.deltadraft.R;
import edu.cnm.deepdive.deltadraft.databinding.ActivityMainBinding;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.service.DeltaDraftDatabase;
import edu.cnm.deepdive.deltadraft.service.dao.PlayerDao;
import edu.cnm.deepdive.deltadraft.service.scraper.PlayerScraper01;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private ActivityMainBinding binding;
  private AppBarConfiguration appBarConfig;
  private NavController navController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setUpUI();
    setUpNavigation();
    populateIfEmpty();
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfig);
  }

  private void setUpUI() {
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
  }

  private void setUpNavigation() {
    appBarConfig = new AppBarConfiguration.Builder
        (R.id.pre_login_fragment, R.id.login_fragment, R.id.list_fragment)
        .build();
    NavHostFragment host = binding.navHostFragment.getFragment();
    navController = host.getNavController();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
  }

  private void populateIfEmpty() {
    Executors.newSingleThreadExecutor().execute(() -> {
      PlayerDao dao = DeltaDraftDatabase.getInstance(this).getPlayerDao();

      // Only scrape + insert if table is empty
      if (dao.count() == 0) {
        try {
          List<Player> players = PlayerScraper01.scrapePlayers();
          dao.insertAll(players);
          Log.i("Populate", "Inserted " + players.size() + " players.");
        } catch (IOException e) {
          Log.e("Populate", "Scraping failed", e);
        }
      } else {
        Log.i("Populate", "Players already exist in DB. Skipping scrape.");
      }
    });
  }
}