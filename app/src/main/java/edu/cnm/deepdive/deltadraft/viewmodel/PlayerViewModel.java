package edu.cnm.deepdive.deltadraft.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playerteam.TeamWithPlayers;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser.PlayerWithUsers;
import edu.cnm.deepdive.deltadraft.service.PlayerRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class PlayerViewModel extends AndroidViewModel {

  private final PlayerRepository playerRepository;
  private final MutableLiveData<String> playerId;
  private final LiveData<PlayerWithUsers> playerWithUsers;
  private final LiveData<List<TeamWithPlayers>> teamsForPlayer;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  public PlayerViewModel(@NonNull Application application, @NonNull PlayerRepository playerRepository,
      LiveData<PlayerWithUsers> playerWithUsers, LiveData<List<TeamWithPlayers>> teamsForPlayer) {
    super(application);
    this.playerRepository = playerRepository;
    this.playerWithUsers = playerWithUsers;
    this.teamsForPlayer = teamsForPlayer;
    this.playerId = new MutableLiveData<>();
    this.throwable = new MutableLiveData<>();
    this.pending = new CompositeDisposable();

  }

  public LiveData<PlayerWithUsers> getPlayerWithUsers() {
    return playerWithUsers;
  }

  public LiveData<List<TeamWithPlayers>> getTeamsForPlayer() {
    return teamsForPlayer;
  }

  public MutableLiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void setPlayerId(String id) {
    this.playerId.setValue(id);
  }

  public void linkPlayerToUser(String playerId, long userId) {
    playerRepository.linkToUser(playerId, userId)
        .subscribe(
            () -> {},
            this::postThrowable
        );
  }

  public void linkPlayerToTeam(String playerId, long teamId) {
    playerRepository.linkToTeam(playerId, teamId)
        .subscribe(
            () -> {},
            this::postThrowable
        );
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  @Override
  protected void onCleared() {
    pending.clear();
    super.onCleared();
  }

}
