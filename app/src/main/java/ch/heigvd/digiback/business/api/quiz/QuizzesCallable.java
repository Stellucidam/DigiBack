package ch.heigvd.digiback.business.api.quiz;

import java.util.List;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.Quiz;

public class QuizzesCallable implements CustomCallable<List<Quiz>> {
    @Override
    public void setDataAfterLoading(List<Quiz> result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public List<Quiz> call() throws Exception {
        return null;
    }
}
