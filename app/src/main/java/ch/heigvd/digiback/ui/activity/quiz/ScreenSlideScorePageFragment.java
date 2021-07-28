package ch.heigvd.digiback.ui.activity.quiz;

import android.graphics.Color;
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

public class ScreenSlideScorePageFragment extends Fragment {
    private final String TAG = "ScorePageFragment";
    private final Long idQuiz;

    public ScreenSlideScorePageFragment(Long idQuiz) {
        this.idQuiz = idQuiz;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_score_page, container, false);

        TextView scoreValue = rootView.findViewById(R.id.score_value);

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
                scoreValue.setText(getString(R.string.score) + " : " + score.toString());
                if (score.getScore() >= score.getTotal() * 2 / 3) {
                    scoreValue.setTextColor(Color.RED);
                } else if (score.getScore() >= score.getTotal() / 3) {
                    scoreValue.setTextColor(Color.rgb(238, 197, 132));
                }
            }
        }));

        return rootView;
    }
}
