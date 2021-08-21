package ch.heigvd.digiback.business.api.auth;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.utils.Backend;
import ch.heigvd.digiback.ui.activity.login.LoggedInUserView;
import ch.heigvd.digiback.ui.activity.login.LoginResult;

public class Login extends AuthCallable {
    private static final String TAG = "Login";
    private final String username, password;
    private final iOnTokenFetched listener; //listener in fragment that shows and hides ProgressBar

    public Login(String username, String password, iOnTokenFetched iOnTokenFetched) {
        this.username = username;
        this.password = password;
        this.listener = iOnTokenFetched;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public LoginResult call() throws Exception {
        URL url = new URL(Backend.getAuthURL() + "login");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            JSONObject obj = new JSONObject();
            obj.put("username", username);
            obj.put("password", password);

            byte[] out = obj.toString().getBytes();
            os.write(out);
        }  catch (Exception e) {
            Log.e(TAG + " line 48", e.getMessage());
        }

        Log.d(TAG, http.getResponseMessage());
        String result = null;
        try(InputStream is = http.getInputStream()) {
            int bufferSize = 1024;
            char[] buffer = new char[bufferSize];
            StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
            for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
                out.append(buffer, 0, numRead);
            }
            result = out.toString();
            Log.i(TAG, "Reading results...");
        } catch (Exception e) {
            Log.e(TAG + " line 69", e.getMessage());
        }
        http.disconnect();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode resObj = objectMapper.readTree(result);

            return new LoginResult(new LoggedInUserView(this.username, resObj.get("token").asText(), resObj.get("idUser").asLong()));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return new LoginResult(R.string.login_failed, e.getMessage());
        }
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(LoginResult loginResult) {
        if (listener != null) {
            listener.setDataInPageWithResult(loginResult);
            listener.hideProgressBar();
        }
    }

    public void get() {

    }
}
