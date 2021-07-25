package ch.heigvd.digiback.business.api;

import ch.heigvd.digiback.business.model.Status;

public class StatusCallable implements CustomCallable<Status> {
    @Override
    public void setDataAfterLoading(Status status) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Status call() throws Exception {
        return null;
    }
}
