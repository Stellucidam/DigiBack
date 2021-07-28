package ch.heigvd.digiback.ui.activity.quiz;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.quiz.GetScore;
import ch.heigvd.digiback.business.api.quiz.iOnScoreFetched;
import ch.heigvd.digiback.business.model.Score;

public class StateScorePageFragment extends Fragment {
    private final String TAG = "StateScorePageFragment";
    private final Long idQuiz;

    public StateScorePageFragment(Long idQuiz) {
        this.idQuiz = idQuiz;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_score_page, container, false);

        TextView scoreValue = rootView.findViewById(R.id.score_value);
        TextView scoreInterpretation = rootView.findViewById(R.id.score_text);
        scoreInterpretation.setText(getString(R.string.state_score_text));

        TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeAsync(new GetScore(idQuiz, new iOnScoreFetched() {
            @Override
            public void showProgressBar() {

            }

            @Override
            public void hideProgressBar() {

            }

            @Override
            public void setDataInPageWithResult(Score score) {
                if (score != null) {
                    String comment = "";
                    if (score.getTotal() <= 3) {
                        comment = getString(R.string.state_small);
                    } else {
                        if (score.getScore() <= 3) {
                            comment = getString(R.string.state_moderate);
                        } else {
                            comment = getString(R.string.state_high);
                        }
                    }
                    scoreValue.setText(getString(R.string.score) + " : " + score.toString() + "\n" + comment);
                }
            }
        }));

        return rootView;
    }
}
