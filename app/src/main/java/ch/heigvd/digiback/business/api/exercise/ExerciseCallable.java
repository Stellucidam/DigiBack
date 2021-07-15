package ch.heigvd.digiback.business.api.exercise;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.exercise.Exercise;

public class ExerciseCallable implements CustomCallable<Exercise> {
    protected final String exercisesURL = "https://localhost:8080/exercise/user/"; // TODO set to backend link

    @Override
    public void setDataAfterLoading(Exercise result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Exercise call() throws Exception {
        return null;
    }
}
