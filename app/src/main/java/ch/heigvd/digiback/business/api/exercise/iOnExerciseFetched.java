package ch.heigvd.digiback.business.api.exercise;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.Exercise;

public interface iOnExerciseFetched extends iOnDataFetched<Exercise> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Exercise exercise);
}
