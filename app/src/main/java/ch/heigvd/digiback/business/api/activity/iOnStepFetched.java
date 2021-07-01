package ch.heigvd.digiback.business.api.activity;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.activity.Step;

public interface iOnStepFetched extends iOnDataFetched<Step> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Step step);
}
