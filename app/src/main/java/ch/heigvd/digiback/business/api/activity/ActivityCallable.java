package ch.heigvd.digiback.business.api.activity;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.activity.Activity;

public class ActivityCallable implements CustomCallable<Activity> {
    protected final String activitiesURL = "https://localhost:8080/activity/user/"; // TODO set to backend link

    @Override
    public void setDataAfterLoading(Activity activity) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Activity call() throws Exception {
        return null;
    }
}
