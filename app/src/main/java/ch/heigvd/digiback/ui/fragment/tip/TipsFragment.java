package ch.heigvd.digiback.ui.fragment.tip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ch.heigvd.digiback.R;

public class TipsFragment extends Fragment {

    private TipsViewModel tipsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tipsViewModel =
                ViewModelProviders.of(this).get(TipsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tip, container, false);
        /*
        final TextView textView = root.findViewById(R.id.text_conseils);
        tipsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}
