package ch.heigvd.digiback.business.api.exercise;

import java.util.List;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.Exercise;

public interface iOnExercisesFetched extends iOnDataFetched<List<Exercise>> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(List<Exercise> exercises);
}
