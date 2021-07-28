package ch.heigvd.digiback.business.api.quiz;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.Score;

public interface iOnScoreFetched extends iOnDataFetched<Score> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Score quiz);
}
