package edu.cnm.deepdive.deltadraft.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.deltadraft.databinding.ItemPlayerBinding;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser.PlayerWithUsers;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.Holder> {

  private final List<Player> players = new ArrayList<>();
  private final LayoutInflater inflater;
  private OnPlayerClickListener listener = (player, position) -> {};
  public PlayerAdapter(Context context) {
    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemPlayerBinding binding = ItemPlayerBinding.inflate(inflater, parent, false);
    return new Holder(binding, listener);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(players.get(position), position);
  }

  @Override
  public int getItemCount() {
    return players.size();
  }

  @SuppressLint("NotifyDataSetChanged")
  public void setPlayers(List<Player> players) {
    this.players.clear();
    this.players.addAll(players);
    notifyDataSetChanged();
  }

  public void setListener(OnPlayerClickListener listener) {
    this.listener = listener;
  }

  public interface OnPlayerClickListener {
    void onPlayerClick(Player player, int position);
  }

  static class Holder extends RecyclerView.ViewHolder {

    private final ItemPlayerBinding binding;
    private final OnPlayerClickListener listener;

    public Holder(ItemPlayerBinding binding, OnPlayerClickListener listener) {
      super(binding.getRoot());
      this.binding = binding;
      this.listener = listener;
    }

    public void bind(Player player, int position) {
      binding.playerName.setText(player.getPlayerName());
      binding.pos.setText(player.getPosition());
      binding.teamName.setText(player.getTeamMlb());

      // Format averages as decimals with 3 decimal places
      binding.itemAvg.setText(String.format(Locale.US, "%.3f", player.getAvg()));
      binding.itemBabip.setText(String.format(Locale.US, "%.3f", player.getBabip()));

      // Format delta (assuming it's a decimal value)
      binding.itemDelta.setText(String.valueOf(player.getDelta()));

      // Format hard hit percentage (value multiplied by 100, shown with percent sign)
      binding.itemHardhit.setText(String.format(Locale.US, "%.1f%%", player.getHardHit()));

      // Format exit velocity as plain number (or use decimal formatting as needed)
      binding.itemEv.setText(String.format(Locale.US, "%.1f", player.getExitVelo()));

      binding.getRoot().setOnClickListener((v) -> listener.onPlayerClick(player, position));
    }

  }
}
