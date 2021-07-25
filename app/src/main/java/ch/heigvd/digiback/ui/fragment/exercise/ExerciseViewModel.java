package ch.heigvd.digiback.ui.fragment.exercise;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.exercise.GetExercises;
import ch.heigvd.digiback.business.api.exercise.iOnExercisesFetched;
import ch.heigvd.digiback.business.api.image.GetImage;
import ch.heigvd.digiback.business.api.image.iOnImageFetched;
import ch.heigvd.digiback.business.model.Exercise;

public class ExerciseViewModel extends ViewModel {

    private MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>();

    private final TaskRunner runner = new TaskRunner();

    public ExerciseViewModel() {
        runner.executeAsync(new GetExercises(new iOnExercisesFetched() {
            @Override
            public void showProgressBar() {
                return;
            }

            @Override
            public void hideProgressBar() {
                return;
            }

            @Override
            public void setDataInPageWithResult(List<Exercise> exercises) {
                for (Exercise exercise :
                        exercises) {
                    final TaskRunner taskRunner = new TaskRunner();
                    taskRunner.executeAsync(new GetImage(exercise.getImageURL(), new iOnImageFetched() {
                        @Override
                        public void showProgressBar() {
                            return;
                        }

                        @Override
                        public void hideProgressBar() {
                            return;
                        }

                        @Override
                        public void setDataInPageWithResult(Bitmap bitmap) {
                            exercise.getImageBM().postValue(bitmap);
                        }
                    }));
                }

                ExerciseViewModel.this.exercises.setValue(exercises);
            }
        }));
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }
}