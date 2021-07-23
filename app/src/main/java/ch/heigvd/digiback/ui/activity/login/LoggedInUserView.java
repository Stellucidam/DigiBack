package ch.heigvd.digiback.ui.activity.login;

import lombok.Getter;

/**
 * Class exposing authenticated user details to the UI.
 */
@Getter
public class LoggedInUserView {
    private String username;
    private String token;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
