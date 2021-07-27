package ch.heigvd.digiback.ui.activity.quiz;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.iOnStatusFetched;
import ch.heigvd.digiback.business.api.quiz.PostAnswer;
import ch.heigvd.digiback.business.model.Question;
import ch.heigvd.digiback.business.model.QuestionAnswer;
import ch.heigvd.digiback.business.model.Status;

public class ScreenSlideQuestionPageFragment extends Fragment {
    private final String TAG = "QuestionPageFragment";
    private final Question question;
    private final Long idQuiz;

    public ScreenSlideQuestionPageFragment(Long idQuiz, Question question) {
        this.question = question;
        this.idQuiz = idQuiz;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_question_page, container, false);

        TextView text = rootView.findViewById(R.id.questions_pager_text);
        text.setText(question.getTitle());


        List<RadioButton> buttons = new LinkedList<>();
        buttons.add(rootView.findViewById(R.id.radio_true));
        buttons.add(rootView.findViewById(R.id.radio_maybe_true));
        buttons.add(rootView.findViewById(R.id.radio_maybe_false));
        buttons.add(rootView.findViewById(R.id.radio_false));
        buttons.add(rootView.findViewById(R.id.radio_not_sure));
        buttons.forEach(button -> button.setOnClickListener(this::onRadioButtonClicked));


        return rootView;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        QuestionAnswer answer = QuestionAnswer.NONE;

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_true:
                if (checked)
                    answer = QuestionAnswer.TRUE;
                break;
            case R.id.radio_maybe_true:
                if (checked)
                    answer = QuestionAnswer.MAYBE_TRUE;
                break;
            case R.id.radio_not_sure:
                if (checked)
                    answer = QuestionAnswer.NOT_SURE;
                break;
            case R.id.radio_false:
                if (checked)
                    answer = QuestionAnswer.FALSE;
                break;
            case R.id.radio_maybe_false:
                if (checked)
                    answer = QuestionAnswer.MAYBE_FALSE;
                break;
        }

        TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeAsync(new PostAnswer(idQuiz, question.getIdQuestion(), answer, new iOnStatusFetched() {
            @Override
            public void showProgressBar() {

            }

            @Override
            public void hideProgressBar() {

            }

            @Override
            public void setDataInPageWithResult(Status status) {
                Log.d(TAG, status.getStatus());
            }
        }));
    }
}
