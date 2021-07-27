package ch.heigvd.digiback.ui.fragment.tip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.digiback.R;

public class TipsFragment extends Fragment {

    private TipsViewModel tipsViewModel, state;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tipsViewModel =
                ViewModelProviders.of(this).get(TipsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tip, container, false);

        state = new ViewModelProvider(this, new TipsViewModelFactory()).get(TipsViewModel.class);

        TipAdapter tipAdapter = new TipAdapter(state, this, this);

        RecyclerView tipList = root.findViewById(R.id.tips_view);
        tipList.setLayoutManager(new LinearLayoutManager(getContext()));
        tipList.setAdapter(tipAdapter);
        return root;
    }
}
