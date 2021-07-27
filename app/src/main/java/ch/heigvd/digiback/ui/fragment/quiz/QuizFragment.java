package ch.heigvd.digiback.ui.fragment.quiz;

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

public class QuizFragment extends Fragment {

    private QuizViewModel quizViewModel, state;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        quizViewModel =
                ViewModelProviders.of(this).get(QuizViewModel.class);
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);
        state = new ViewModelProvider(this, new QuizViewModelFactory()).get(QuizViewModel.class);

        QuizAdapter quizAdapter = new QuizAdapter(state, this, this);

        RecyclerView quizList = root.findViewById(R.id.info_view);
        quizList.setLayoutManager(new LinearLayoutManager(getContext()));
        quizList.setAdapter(quizAdapter);
        return root;
    }

}
