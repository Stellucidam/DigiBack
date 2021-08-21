package ch.heigvd.digiback.business.api.auth;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.ui.activity.login.LoginResult;

public interface iOnTokenFetched extends iOnDataFetched<LoginResult> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(LoginResult result);
}