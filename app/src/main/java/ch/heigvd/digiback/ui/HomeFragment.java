package ch.heigvd.digiback.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.ui.data.LoginDataSource;
import ch.heigvd.digiback.ui.data.LoginRepository;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
