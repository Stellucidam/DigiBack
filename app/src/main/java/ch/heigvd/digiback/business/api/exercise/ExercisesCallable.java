package ch.heigvd.digiback.business.api.exercise;

import java.util.List;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.Exercise;

public class ExercisesCallable implements CustomCallable<List<Exercise>> {
    @Override
    public void setDataAfterLoading(List<Exercise> result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public List<Exercise> call() throws Exception {
        return null;
    }
}
