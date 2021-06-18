package ch.heigvd.digiback.ui.data.model;

import lombok.Getter;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Getter
public class LoggedInUser {

    private String userId;
    private String displayName;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }
}
