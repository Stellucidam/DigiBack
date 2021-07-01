package ch.heigvd.digiback.business.api.auth;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.ui.data.model.LoggedInUser;

public class AuthCallable implements CustomCallable<LoggedInUser> {
    protected final String authURL = "https://localhost:8080/auth/"; // TODO set to backend link

    @Override
    public void setDataAfterLoading(LoggedInUser result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public LoggedInUser call() throws Exception {
        return null;
    }
}
