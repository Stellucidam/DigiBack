package ch.heigvd.digiback.ui.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ch.heigvd.digiback.MainActivity;
import ch.heigvd.digiback.R;
import ch.heigvd.digiback.ui.data.LoginDataSource;
import ch.heigvd.digiback.ui.data.LoginRepository;
import ch.heigvd.digiback.ui.data.model.LoggedInUser;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";
    private int error;
    private Button registerButton;

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText emailOrUsernameTextEdit = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);

        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final TextView errorText = findViewById(R.id.login_error);

        try {
            error = getIntent().getIntExtra("error", -1);
            if (error > 0) {
                errorText.setText(R.string.login_failed);
            }
        } catch (Exception e) {
            Log.e(TAG, "No extras in intent");
        }

        registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(view -> {
            Log.i(TAG, "Set on click listener for register button.");
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
            }
            setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful
            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(emailOrUsernameTextEdit.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailOrUsernameTextEdit.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                try {
                    loginViewModel.login(emailOrUsernameTextEdit.getText().toString(),
                        passwordEditText.getText().toString(), this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            try {
                loginViewModel.login(emailOrUsernameTextEdit.getText().toString(),
                        passwordEditText.getText().toString(), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        // Initiate successful logged in experience
        String welcome = getString(R.string.welcome) + " " + model.getUsername();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT).show();

        LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
        loginRepository.setUser(new LoggedInUser(model.getId(), model.getUsername(), model.getToken()));

        // Go to MainActivty
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Log.d(TAG, "Failed ");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("error", errorString);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
