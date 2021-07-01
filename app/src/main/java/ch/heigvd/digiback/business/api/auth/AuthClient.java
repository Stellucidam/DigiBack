package ch.heigvd.digiback.business.api.auth;

import ch.heigvd.digiback.ui.data.model.LoggedInUser;

public abstract class AuthClient extends AuthCallable {
    protected final String postsUrl = "https://infomaldedos.ch/wp-json/wp/v2/posts";
    protected final String mediaUrl = "https://infomaldedos.ch/wp-json/wp/v2/media/";

    @Override
    public void setUiForLoading() {

    }

    @Override
    public void setDataAfterLoading(LoggedInUser result) {

    }

    @Override
    public LoggedInUser call() throws Exception {
        return null;
    }
}

