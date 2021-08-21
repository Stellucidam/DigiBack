package ch.heigvd.digiback.ui.activity.login;

import lombok.Getter;

/**
 * Class exposing authenticated user details to the UI.
 */
@Getter
public class LoggedInUserView {
    private String username;
    private String token;
    private Long id;
    //... other data fields that may be accessible to the UI

    public LoggedInUserView(String username, String token, Long id) {
        this.username = username;
        this.token = token;
        this.id = id;
    }
}
