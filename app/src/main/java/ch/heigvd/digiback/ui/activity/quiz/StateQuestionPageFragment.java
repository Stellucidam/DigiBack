package ch.heigvd.digiback.ui.activity.quiz;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class StateQuestionPageFragment extends Fragment {
    private final String TAG = "StateQuestionPage";
    private final QuestionAnswer answer;
    private final Question question;
    private final Long idQuiz;
    private final int position, totalQuestions;

    public StateQuestionPageFragment(
            Long idQuiz,
            QuestionAnswer answer,
            Question question,
            int position,
            int totalQuestions) {
        this.answer = answer;
        this.question = question;
        this.idQuiz = idQuiz;
        this.position = position + 1;
        this.totalQuestions = totalQuestions;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_question_page, container, false);

        TextView text = rootView.findViewById(R.id.questions_pager_text);
        text.setText(question.getTitle());

        TextView pageNumber = rootView.findViewById(R.id.page_number);
        pageNumber.setText("Question " + position + " / " + totalQuestions);

        List<RadioButton> buttons = new LinkedList<>();
        buttons.add(rootView.findViewById(R.id.radio_true)); // 0
        buttons.add(rootView.findViewById(R.id.radio_false));// 1

        Button b = rootView.findViewById(R.id.radio_maybe_true);
        b.setVisibility(View.GONE);
        b = rootView.findViewById(R.id.radio_maybe_false);
        b.setVisibility(View.GONE);
        b = rootView.findViewById(R.id.radio_not_sure);
        b.setVisibility(View.GONE);
        buttons.forEach(button -> button.setOnClickListener(this::onRadioButtonClicked));

        switch (answer) {
            case NONE:
                break;
            case FALSE:
                buttons.get(1).toggle();
                break;
            case TRUE:
                buttons.get(0).toggle();
                break;
        }

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
            case R.id.radio_false:
                if (checked)
                    answer = QuestionAnswer.FALSE;
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
