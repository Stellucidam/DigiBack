package ch.heigvd.digiback.business.api.quiz;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.Quiz;

public class QuizCallable implements CustomCallable<Quiz> {
    @Override
    public void setDataAfterLoading(Quiz result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Quiz call() throws Exception {
        return null;
    }
}
