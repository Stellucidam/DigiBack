package ch.heigvd.digiback.business.api.auth;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import ch.heigvd.digiback.business.utils.Backend;
import ch.heigvd.digiback.ui.data.model.LoggedInUser;

public class Login extends AuthCallable {
    private static final String TAG = "Login";
    private final String username, password;
    private final iOnTokenFetched listener; //listener in fragment that shows and hides ProgressBar

    public Login(String username, String password, iOnTokenFetched iOnTokenFetched) {
        this.username = username;
        this.password = password;
        this.listener = iOnTokenFetched;
    }

    @Override
    public LoggedInUser call() throws Exception {
        URL url = new URL(Backend.getAuthURL() + "/login");
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
            int length = out.length;
            http.setFixedLengthStreamingMode(length);
            os.write(out);

        }
        //http.getResponseMessage();
        // Do something with http.getInputStream()

        Log.d(TAG, http.getResponseMessage());

        // TODO set to the return value
        return new LoggedInUser(1L, "username", "token");
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(LoggedInUser activity) {
        if (listener != null) {
            listener.setDataInPageWithResult(activity);
            listener.hideProgressBar();
        }
    }
}
