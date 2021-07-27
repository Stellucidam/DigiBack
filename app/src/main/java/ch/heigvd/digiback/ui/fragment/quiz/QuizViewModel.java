package ch.heigvd.digiback.ui.fragment.quiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.quiz.GetQuizzes;
import ch.heigvd.digiback.business.api.quiz.iOnQuizzesFetched;
import ch.heigvd.digiback.business.model.Quiz;

public class QuizViewModel extends ViewModel {

    private MutableLiveData<List<Quiz>> quiz = new MutableLiveData<>();

    private final TaskRunner runner = new TaskRunner();

    public QuizViewModel() {
        runner.executeAsync(new GetQuizzes(new iOnQuizzesFetched() {
            @Override
            public void showProgressBar() {
                return;
            }

            @Override
            public void hideProgressBar() {
                return;
            }

            @Override
            public void setDataInPageWithResult(List<Quiz> result) {
                quiz.setValue(result);
            }
        }));
    }

    public LiveData<List<Quiz>> getQuiz() {
        return quiz;
    }
}