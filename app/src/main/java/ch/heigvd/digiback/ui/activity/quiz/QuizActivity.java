package ch.heigvd.digiback.ui.activity.quiz;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Map;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.quiz.GetAnswers;
import ch.heigvd.digiback.business.api.quiz.GetQuiz;
import ch.heigvd.digiback.business.api.quiz.iOnAnswersFetched;
import ch.heigvd.digiback.business.api.quiz.iOnQuizFetched;
import ch.heigvd.digiback.business.model.Question;
import ch.heigvd.digiback.business.model.QuestionAnswer;
import ch.heigvd.digiback.business.model.Quiz;
import lombok.Getter;

public class QuizActivity extends AppCompatActivity {
    private Long idQuiz;
    private String titleQuiz;

    private ViewPager questionsPager;
    private PagerAdapter pagerAdapter;

    private MutableLiveData<Integer> nbrPages = new MutableLiveData<>(0);

    @Getter
    private MutableLiveData<List<Question>> questions = new MutableLiveData<>();
    @Getter
    private MutableLiveData<Map<Long, QuestionAnswer>> answers = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        idQuiz = getIntent().getLongExtra("id", -1);
        titleQuiz = getIntent().getStringExtra("title");

        TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeAsync(new GetAnswers(idQuiz, new iOnAnswersFetched() {
            @Override
            public void showProgressBar() {

            }

            @Override
            public void hideProgressBar() {

            }

            @Override
            public void setDataInPageWithResult(Map<Long, QuestionAnswer> result) {
                answers.postValue(result);
            }
        }));

        taskRunner.executeAsync(new GetQuiz(idQuiz, new iOnQuizFetched() {
            @Override
            public void showProgressBar() {

            }

            @Override
            public void hideProgressBar() {

            }

            @Override
            public void setDataInPageWithResult(Quiz quiz) {
                questions.postValue(quiz.getQuestions());
            }
        }));

        TextView title = findViewById(R.id.quiz_title);
        title.setText(titleQuiz);

        // Instantiate a ViewPager and a PagerAdapter.
        questionsPager = findViewById(R.id.questions_pager);

        answers.observe(this, newAnswers -> {
            questions.observe(this, newQuestions -> {
                pagerAdapter = new ScreenSlidePagerAdapter(idQuiz, titleQuiz, answers, newQuestions, getSupportFragmentManager());
                questionsPager.setAdapter(pagerAdapter);
            });
        });

        FloatingActionButton back = findViewById(R.id.floating_back_button);
        back.setOnClickListener(view -> super.onBackPressed());
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<Question> questions;
        private MutableLiveData<Map<Long, QuestionAnswer>> answers;
        private final Long idQuiz;
        private final String quizTitle;
        private final String TAG = "ScreenSlidePagerAdapter";

        public ScreenSlidePagerAdapter(
                Long idQuiz,
                String quizTitle,
                MutableLiveData<Map<Long, QuestionAnswer>> answers,
                List<Question> questions,
                FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.questions = questions;
            this.answers = answers;
            this.idQuiz = idQuiz;
            this.quizTitle = quizTitle;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Fragment getItem(int position) {
            if (position < questions.size()) {
                if (!quizTitle.equals("STarT Back Screening Tool")) {
                    return new ScreenSlideQuestionPageFragment(
                            idQuiz,
                            answers.getValue().getOrDefault(questions.get(position).getIdQuestion(), QuestionAnswer.NONE),
                            questions.get(position),
                            position,
                            questions.size());
                } else {
                    if (questions.get(position).getTitle().equals("Globalement, à quel point votre mal de dos vous a-t-il gêné(e) au cours des 2 dernières semaines ?")) {
                        return new StateSpecialQuestionPageFragment(
                                idQuiz,
                                answers.getValue().getOrDefault(questions.get(position).getIdQuestion(), QuestionAnswer.NONE),
                                questions.get(position),
                                position,
                                questions.size());
                    }
                    return new StateQuestionPageFragment(
                            idQuiz,
                            answers.getValue().getOrDefault(questions.get(position).getIdQuestion(), QuestionAnswer.NONE),
                            questions.get(position),
                            position,
                            questions.size());
                }
            } else {
                if (!quizTitle.equals("STarT Back Screening Tool")) {
                    return new ScreenSlideScorePageFragment(idQuiz);
                }
                return new StateScorePageFragment(idQuiz);
            }
        }

        @Override
        public int getCount() {
            return questions.size() + 1;
        }
    }
}