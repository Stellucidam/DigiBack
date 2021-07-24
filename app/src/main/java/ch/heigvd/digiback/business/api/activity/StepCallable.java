package ch.heigvd.digiback.business.api.activity;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.Step;

public class StepCallable implements CustomCallable<Step> {
    protected final String stepsURLEnd = "/upload/steps";

    @Override
    public void setDataAfterLoading(Step step) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Step call() throws Exception {
        return null;
    }
}
