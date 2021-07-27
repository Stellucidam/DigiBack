package ch.heigvd.digiback.ui.data;

import java.io.IOException;

import ch.heigvd.digiback.ui.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(Long id, String username, String token) {
        try {
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            id,
                            username,
                            token);
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
}
