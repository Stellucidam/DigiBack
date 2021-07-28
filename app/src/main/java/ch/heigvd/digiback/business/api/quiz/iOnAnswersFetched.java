package ch.heigvd.digiback.business.api.quiz;

import java.util.Map;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.QuestionAnswer;

public interface iOnAnswersFetched extends iOnDataFetched<Map<Long, QuestionAnswer>> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Map<Long, QuestionAnswer> result);
}
