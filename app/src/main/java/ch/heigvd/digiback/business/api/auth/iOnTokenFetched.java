package ch.heigvd.digiback.business.api.auth;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.ui.data.model.LoggedInUser;

public interface iOnTokenFetched extends iOnDataFetched<LoggedInUser> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(LoggedInUser loggedInUser);
}