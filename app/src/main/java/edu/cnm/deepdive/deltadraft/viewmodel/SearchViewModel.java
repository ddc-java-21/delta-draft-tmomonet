package edu.cnm.deepdive.deltadraft.viewmodel;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.model.entity.User;
import edu.cnm.deepdive.deltadraft.service.PlayerRepository;
import edu.cnm.deepdive.deltadraft.service.UserRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class SearchViewModel extends ViewModel implements DefaultLifecycleObserver {

  private Context context;
  private final PlayerRepository playerRepository;
  private final UserRepository userRepository;
  private final MutableLiveData<String> playerId;
  private final MutableLiveData<List<Player>> players;
  private final MutableLiveData<List<Player>> player;
  private final MutableLiveData<User> user;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  SearchViewModel(@ApplicationContext Context context, @NonNull PlayerRepository playerRepository, @NonNull UserRepository userRepository) {
    this.playerRepository = playerRepository;
    this.userRepository = userRepository;
    pending = new CompositeDisposable();
    playerId = new MutableLiveData<>();
    player = new MutableLiveData<>();
    players = new MutableLiveData<>();
    user = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    fetchUser();
  }

  public MutableLiveData<List<Player>> getPlayers() {
    return players;
  }

  public MutableLiveData<List<Player>> getPlayer() {
    return player;
  }

  public MutableLiveData<User> getUser() {
    return user;
  }

  public void addPlayer(Player player) {
    List<Player> players = this.players.getValue();
    //noinspection DataFlowIssue
    players.add(player);
    this.players.setValue(players);
  }

  public void removePlayer(Player player) {
    List<Player> players = this.players.getValue();
    //noinspection DataFlowIssue
    players.remove(player);
    this.players.setValue(players);
  }

  public void onStop(@NonNull LifecycleOwner owner) {
    pending.clear();
    DefaultLifecycleObserver.super.onStop(owner);
  }

  private void fetchUser() {
    throwable.setValue(null);
    userRepository
        .getCurrentUser()
        .subscribe(
            user::postValue,
            this::postThrowable,
            pending
        );
  }

  private void postThrowable(Throwable throwable) {
    Log.e(SearchViewModel.class.getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
