package ch.heigvd.digiback.business.api.activity;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.Activity;

public class ActivityCallable implements CustomCallable<Activity> {
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
