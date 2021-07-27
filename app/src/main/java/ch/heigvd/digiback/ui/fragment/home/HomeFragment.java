package ch.heigvd.digiback.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.ui.data.LoginDataSource;
import ch.heigvd.digiback.ui.data.LoginRepository;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView welcomeText = root.findViewById(R.id.welcome_text_content);
        LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
        welcomeText.setText(
                getString(R.string.welcome_start) + " " +
                loginRepository.getUsername() + ".\n" +
                getString(R.string.welcome_end));
        return root;
    }
}
