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

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = "RegisterActivity";
    private int error;

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameTextEdit = findViewById(R.id.username);
        final EditText emailTextEdit = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText passwordConfirmationEditText = findViewById(R.id.confirm_password);

        final Button registerAndLoginButton = findViewById(R.id.register_button);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        final TextView errorText = findViewById(R.id.login_error);

        try {
            error = getIntent().getIntExtra("error", -1);
            if (error > 0) {
                errorText.setText(R.string.register_failed);
            }
        } catch (Exception e) {
            Log.e(TAG, "No extras in intent");
        }

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            //registerAndLoginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameTextEdit.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
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
                loginViewModel.loginDataChanged(usernameTextEdit.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameTextEdit.addTextChangedListener(afterTextChangedListener);
        emailTextEdit.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE &&
                    passwordConfirmationEditText.getText().equals(passwordEditText.getText())) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                try {
                    loginViewModel.register(
                            usernameTextEdit.getText().toString(),
                            emailTextEdit.getText().toString(),
                            passwordConfirmationEditText.getText().toString(), this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        });

        registerAndLoginButton.setOnClickListener(v -> {
            Log.i(TAG, "Compare " + passwordConfirmationEditText.getText().toString() +
                    " and " +
                    passwordEditText.getText().toString());
            if(passwordConfirmationEditText.getText().toString().equals(
                    passwordEditText.getText().toString())) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                try {
                    Log.i(TAG, "Register " + usernameTextEdit.getText().toString());
                    loginViewModel.register(
                            usernameTextEdit.getText().toString(),
                            emailTextEdit.getText().toString(),
                            passwordConfirmationEditText.getText().toString(), this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                errorText.setText(getString(R.string.wrong_confirmation_password));
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        // Initiate successful logged in experience
        String welcome = getString(R.string.welcome) + " " + model.getUsername();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT).show();

        // Go to MainActivty
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Log.d(TAG, "Failed ");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("error", getString(errorString));
        startActivity(intent);
    }
}