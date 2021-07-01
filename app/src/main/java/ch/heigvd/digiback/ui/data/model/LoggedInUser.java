package ch.heigvd.digiback.ui.data.model;

import lombok.Getter;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Getter
public class LoggedInUser {

    private Long userId;
    private String username;
    private String token;

    public LoggedInUser(Long userId, String username, String token) {
        this.userId = userId;
        this.username = username;
        this.token = token;
    }

    public LoggedInUser() {
        this.userId = 0L;
        this.username = null;
        this.token = null;
    }
}
