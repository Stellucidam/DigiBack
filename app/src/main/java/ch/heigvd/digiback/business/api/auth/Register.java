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

public class Register extends AuthCallable {
    private static final String TAG = "Register";
    private final String username, email, password;
    private final iOnTokenFetched listener; //listener in fragment that shows and hides ProgressBar

    public Register(String username, String email, String password, iOnTokenFetched iOnTokenFetched) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.listener = iOnTokenFetched;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public LoginResult call() throws Exception {
        Log.i(TAG, "Register called...");
        URL url = new URL(Backend.getAuthURL() + "register");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            JSONObject obj = new JSONObject();
            obj.put("username", username);
            obj.put("email", email);
            obj.put("password", password);

            byte[] out = obj.toString().getBytes();
            os.write(out);
        }  catch (Exception e) {
            Log.e(TAG + " line 48", e.getMessage());
        }
        //http.getResponseMessage();
        // Do something with http.getInputStream()

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
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            try(InputStream is = http.getErrorStream()) {
                int bufferSize = 1024;
                char[] buffer = new char[bufferSize];
                StringBuilder out = new StringBuilder();
                Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
                for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
                    out.append(buffer, 0, numRead);
                }
                result = out.toString();
            } catch (Exception e1) {
                Log.e(TAG, e1.getMessage());
                result = http.getResponseMessage();
            }
        }
        http.disconnect();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode resObj = objectMapper.readTree(result);
            if (resObj.hasNonNull("error")) {
                throw new Exception(resObj.get("error").asText());
            }

            return new LoginResult(new LoggedInUserView(this.username, resObj.get("token").asText(), resObj.get("idUser").asLong()));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            JSONObject c = new JSONObject(result);
            return new LoginResult(R.string.register_failed, c.getString("error"));
        }
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(LoginResult result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
