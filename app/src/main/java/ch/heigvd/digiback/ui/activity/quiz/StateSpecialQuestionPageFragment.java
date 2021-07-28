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

public class StateSpecialQuestionPageFragment extends Fragment {
    private final String TAG = "StateQuestionPage";
    private final QuestionAnswer answer;
    private final Question question;
    private final Long idQuiz;
    private final int position, totalQuestions;

    public StateSpecialQuestionPageFragment(
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
        buttons.add(rootView.findViewById(R.id.radio_maybe_true));// 1
        buttons.add(rootView.findViewById(R.id.radio_maybe_false));// 2
        buttons.add(rootView.findViewById(R.id.radio_false));// 3
        buttons.add(rootView.findViewById(R.id.radio_not_sure));// 4
        for (int i = 0; i < buttons.size(); i++) {
            switch (i) {
                case 0:
                    buttons.get(i).setText(getString(R.string.not_at_all));
                    break;
                case 1:
                    buttons.get(i).setText(getString(R.string.a_bit));
                    break;
                case 2:
                    buttons.get(i).setText(getString(R.string.moderately));
                    break;
                case 3:
                    buttons.get(i).setText(getString(R.string.a_lot));
                    break;
                case 4:
                    buttons.get(i).setText(getString(R.string.extremely));
                    break;
            }
            buttons.get(i).setOnClickListener(this::onRadioButtonClicked);
        }

        switch (answer) {
            case NONE:
                break;
            case FALSE:
                buttons.get(3).toggle();
                break;
            case MAYBE_FALSE:
                buttons.get(2).toggle();
                break;
            case NOT_SURE:
                buttons.get(4).toggle();
                break;
            case MAYBE_TRUE:
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
