package ch.heigvd.digiback.business.api.quiz;

import java.util.List;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.Quiz;

public interface iOnQuizFetched extends iOnDataFetched<List<Quiz>> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(List<Quiz> quiz);
}
