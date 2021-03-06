package ch.heigvd.digiback.business.api.quiz;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.Quiz;

public interface iOnQuizFetched extends iOnDataFetched<Quiz> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Quiz quiz);
}
