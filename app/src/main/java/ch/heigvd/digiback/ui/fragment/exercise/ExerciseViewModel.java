package ch.heigvd.digiback.ui.fragment.exercise;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.model.Exercise;

public class ExerciseViewModel extends ViewModel {

    private MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>();

    private final TaskRunner runner = new TaskRunner();

    public ExerciseViewModel() {
        /*
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

                    taskRunner.executeAsync(new GetCategory(exercise.getCategory(), new iOnCategoryFetched() {
                        @Override
                        public void showProgressBar() {
                            return;
                        }

                        @Override
                        public void hideProgressBar() {
                            return;
                        }

                        @Override
                        public void setDataInPageWithResult(Category category) {
                            exercise.getCategoryName().postValue(category.getName());
                        }
                    }));
                }

                ExerciseViewModel.this.exercises.setValue(exercises);
            }
        }));
        */
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }
}