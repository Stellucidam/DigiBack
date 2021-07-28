package ch.heigvd.digiback.business.api.quiz;

import java.util.Map;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.QuestionAnswer;

public class AnswersCallable implements CustomCallable<Map<Long, QuestionAnswer>> {
    @Override
    public void setDataAfterLoading(Map<Long, QuestionAnswer> result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Map<Long, QuestionAnswer> call() throws Exception {
        return null;
    }
}
