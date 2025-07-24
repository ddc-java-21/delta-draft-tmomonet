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
import edu.cnm.deepdive.deltadraft.databinding.FragmentSearchBinding;
import edu.cnm.deepdive.deltadraft.viewmodel.LogInViewModel;
import edu.cnm.deepdive.deltadraft.R;

import javax.inject.Inject;

@AndroidEntryPoint
public class SearchFragment extends Fragment implements MenuProvider {

  private FragmentSearchBinding binding;
  private LogInViewModel loginViewModel;


  @Override
  // TODO: 7/9/2025 Implement onCreate recyclerview for players
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentActivity activity = requireActivity();
    ViewModelProvider provider = new ViewModelProvider(activity);
    LifecycleOwner owner = getViewLifecycleOwner();
    loginViewModel = provider.get(LogInViewModel.class);
    loginViewModel
        .getAccount()
            .observe((owner), (account) -> {
              if (account == null) {
               Navigation.findNavController(binding.getRoot())
                   .navigate(SearchFragmentDirections.showPreLogin());
              }
            });
    activity.addMenuProvider(this, owner, State.RESUMED);
  }

  @Nullable
  @Override

  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentSearchBinding.inflate(inflater, container, false);
//    binding.placeHolder.setAdapter(adapter);
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
