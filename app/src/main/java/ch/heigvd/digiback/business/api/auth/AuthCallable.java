package ch.heigvd.digiback.business.api.auth;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.ui.activity.login.LoginResult;

public class AuthCallable implements CustomCallable<LoginResult> {
    @Override
    public void setDataAfterLoading(LoginResult result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public LoginResult call() throws Exception {
        return null;
    }
}
