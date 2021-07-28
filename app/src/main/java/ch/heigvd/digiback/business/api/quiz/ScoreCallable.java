package ch.heigvd.digiback.business.api.quiz;


import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.Score;

public class ScoreCallable implements CustomCallable<Score> {
    @Override
    public void setDataAfterLoading(Score result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Score call() throws Exception {
        return null;
    }
}
