package ch.heigvd.digiback.ui.activity.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;
    @Nullable
    private String errorString;

    public LoginResult(@Nullable Integer error, @Nullable String errorString) {
        this.error = error;
        this.errorString = errorString;
    }

    public LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }

    @Nullable
    String getErrorString() {
        return errorString;
    }
}
