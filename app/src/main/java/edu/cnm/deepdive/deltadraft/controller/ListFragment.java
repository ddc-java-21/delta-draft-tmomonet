package edu.cnm.deepdive.deltadraft.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.deltadraft.view.adapter.NoteAdapter;
import edu.cnm.deepdive.deltadraft.viewmodel.LogInViewModel;
import edu.cnm.deepdive.deltadraft.viewmodel.NoteViewModel;
import edu.cnm.deepdive.notes.R;
import edu.cnm.deepdive.notes.databinding.FragmentListBinding;
import javax.inject.Inject;

@AndroidEntryPoint
public class ListFragment extends Fragment implements MenuProvider {

  @Inject
  NoteAdapter adapter;

  private FragmentListBinding binding;
  private NoteViewModel viewModel;
  private LogInViewModel loginViewModel;


  @Override
  // TODO: 6/16/2025
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentActivity activity = requireActivity();
    ViewModelProvider provider = new ViewModelProvider(activity);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel = provider.get(NoteViewModel.class);
    viewModel
        .getNotes()
        .observe(owner, (notes) -> adapter.setNotes(notes));
    loginViewModel = provider.get(LogInViewModel.class);
    loginViewModel
        .getAccount()
            .observe((owner), (account) -> {
              if (account == null) {
               Navigation.findNavController(binding.getRoot())
                   .navigate(ListFragmentDirections.showPreLogin());
              }
            });
    activity.addMenuProvider(this, owner, State.RESUMED);
  }

  @Nullable
  @Override

  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentListBinding.inflate(inflater, container, false);
    binding.deltadraft.setAdapter(adapter);
    adapter.setListener((note, position) -> Navigation.findNavController(binding.getRoot())
        .navigate(ListFragmentDirections.showDetails(note.getId())));
    binding.addNote.setOnClickListener((v) -> Navigation.findNavController(binding.getRoot())
        .navigate(ListFragmentDirections.showDetails(0)));
    return binding.getRoot();
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @Override
  public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.note_options, menu);
  }

  @Override
  public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
    boolean handled = false;
    if (menuItem.getItemId() == R.id.sign_out) {
      loginViewModel.signOut();
      handled = true;
    }
    return handled;
  }
}
