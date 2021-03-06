package ch.heigvd.digiback.ui.activity.login;

import android.util.Patterns;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.auth.Login;
import ch.heigvd.digiback.business.api.auth.Register;
import ch.heigvd.digiback.business.api.auth.iOnTokenFetched;
import ch.heigvd.digiback.ui.data.LoginRepository;

public class LoginViewModel extends ViewModel {
    private final String TAG = "LoginViewModel";
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, LifecycleOwner lifecycleOwner) throws Exception {
        // can be launched in a separate asynchronous job
        final TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeAsync(new Login(username, password, new iOnTokenFetched() {
            @Override
            public void showProgressBar() {

            }

            @Override
            public void hideProgressBar() {

            }

            @Override
            public void setDataInPageWithResult(LoginResult result) {
                loginResult.setValue(result);
            }
        }));
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public void register(
            String username, String email, String password,
            LifecycleOwner lifecycleOwner) {
        final TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeAsync(new Register(username, email, password, new iOnTokenFetched() {
            @Override
            public void showProgressBar() {

            }

            @Override
            public void hideProgressBar() {

            }

            @Override
            public void setDataInPageWithResult(LoginResult result) {
                if (result.getSuccess() != null) {
                    try {
                        login(username, password, lifecycleOwner);
                    } catch (Exception e) {
                        e.printStackTrace();
                        loginResult.setValue(new LoginResult(R.string.login_failed, e.getMessage() + " login"));
                    }
                } else {
                    loginResult.setValue(result);
                }
            }
        }));
    }
}
