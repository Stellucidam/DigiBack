package ch.heigvd.digiback.ui.activity.quiz;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.quiz.GetQuiz;
import ch.heigvd.digiback.business.api.quiz.iOnQuizFetched;
import ch.heigvd.digiback.business.model.Question;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        idQuiz = getIntent().getLongExtra("id", -1);
        titleQuiz = getIntent().getStringExtra("title");

        TaskRunner taskRunner = new TaskRunner();
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

        questions.observe(this, newQuestions -> {
            pagerAdapter = new ScreenSlidePagerAdapter(idQuiz, newQuestions, getSupportFragmentManager());
            questionsPager.setAdapter(pagerAdapter);
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
        private final Long idQuiz;

        public ScreenSlidePagerAdapter(Long idQuiz, List<Question> questions, FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.questions = questions;
            this.idQuiz = idQuiz;
            Log.d("ScreenSlidePagerAdapter", "Created with " + questions.size() + " questions");
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlideQuestionPageFragment(idQuiz, questions.get(position), position, questions.size());
        }

        @Override
        public int getCount() {
            return questions.size();
        }
    }
}