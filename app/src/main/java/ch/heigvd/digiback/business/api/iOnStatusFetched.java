package ch.heigvd.digiback.business.api;

import ch.heigvd.digiback.business.model.Status;

public interface iOnStatusFetched extends iOnDataFetched<Status> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Status status);
}
