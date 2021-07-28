package ch.heigvd.digiback.ui.fragment.exercise;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.activity.GetActivity;
import ch.heigvd.digiback.business.api.activity.iOnActivityFetched;
import ch.heigvd.digiback.business.model.Activity;
import lombok.Getter;

public class ExerciseFragment extends Fragment {
    private final String TAG = "ExerciseFragment";
    private ExerciseViewModel state;

    private ExerciseViewModel exerciseViewModel;

    @Getter
    private final MutableLiveData<List<Long>> exercises = new MutableLiveData<>(new LinkedList<>());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exerciseViewModel =
                ViewModelProviders.of(this).get(ExerciseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_exercise, container, false);

        state = new ViewModelProvider(this, new ExerciseViewModelFactory()).get(ExerciseViewModel.class);

        try {
            final TaskRunner taskRunner = new TaskRunner();
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            taskRunner.executeAsync(new GetActivity(simpleDateFormat.format(Calendar.getInstance().getTime()), new iOnActivityFetched() {
                @Override
                public void showProgressBar() {

                }

                @Override
                public void hideProgressBar() {

                }

                @Override
                public void setDataInPageWithResult(Activity activity) {
                    exercises.postValue(activity.getExercises());
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(state, this, this);

        RecyclerView exerciseList = root.findViewById(R.id.info_view);
        exerciseList.setLayoutManager(new LinearLayoutManager(getContext()));
        exerciseList.setAdapter(exerciseAdapter);

        return root;
    }
}
