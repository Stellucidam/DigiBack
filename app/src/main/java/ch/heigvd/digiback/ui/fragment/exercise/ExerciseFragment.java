package ch.heigvd.digiback.ui.fragment.exercise;

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

public class ExerciseFragment extends Fragment {
    private ExerciseViewModel state;

    private ExerciseViewModel exerciseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exerciseViewModel =
                ViewModelProviders.of(this).get(ExerciseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);

        state = new ViewModelProvider(this, new ExerciseViewModelFactory()).get(ExerciseViewModel.class);

        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(state, this, this);

        RecyclerView exerciseList = root.findViewById(R.id.info_view);
        exerciseList.setLayoutManager(new LinearLayoutManager(getContext()));
        exerciseList.setAdapter(exerciseAdapter);

        return root;
    }
}
