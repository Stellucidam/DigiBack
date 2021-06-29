package ch.heigvd.digiback.business.api.activity;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.activity.Activity;

public interface iOnActivityFetched extends iOnDataFetched<Activity> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Activity activity);
}
