package ch.heigvd.digiback.ui.data;

import ch.heigvd.digiback.ui.data.model.LoggedInUser;
import lombok.Setter;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    @Setter
    private LoggedInUser user = new LoggedInUser(
            1L,
            "username",
            "token");

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public String getToken() {
        return user.getToken();
    }

    public Long getUserId() {
        return user.getUserId();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void logout() {
        user = null;
    }
}
