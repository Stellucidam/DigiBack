package ch.heigvd.digiback.ui.fragment.conseils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ch.heigvd.digiback.R;

public class ConseilsFragment extends Fragment {

    private ConseilsViewModel conseilsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        conseilsViewModel =
                ViewModelProviders.of(this).get(ConseilsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_conseils, container, false);
        /*
        final TextView textView = root.findViewById(R.id.text_conseils);
        conseilsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}
