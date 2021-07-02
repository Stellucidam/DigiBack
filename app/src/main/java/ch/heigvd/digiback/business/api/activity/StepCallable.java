package ch.heigvd.digiback.business.api.activity;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.activity.Step;

public class StepCallable implements CustomCallable<Step> {
    protected final String stepsURL = "https://localhost:8080/activity/user/"; // TODO set to backend link
    protected final String stepsURLEnd = "/upload/steps/";

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