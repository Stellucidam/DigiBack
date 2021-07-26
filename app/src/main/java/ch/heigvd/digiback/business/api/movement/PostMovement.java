package ch.heigvd.digiback.business.api.movement;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import ch.heigvd.digiback.business.api.StatusCallable;
import ch.heigvd.digiback.business.api.iOnStatusFetched;
import ch.heigvd.digiback.business.model.Movement;
import ch.heigvd.digiback.business.model.Status;
import ch.heigvd.digiback.business.utils.Backend;

public class PostMovement extends StatusCallable {
    private final String TAG = "PostMovement";
    private final Movement movement;
    private final int painLevel;
    private final iOnStatusFetched listener;

    public PostMovement(int painLevel, Movement movement, iOnStatusFetched iOnStatusFetched) {
        this.movement = movement;
        this.painLevel = painLevel;
        this.listener = iOnStatusFetched;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Status call() throws Exception {
        String token  = "token=" + loginRepository.getToken();
        String pain = "/level/" + painLevel;
        String u = Backend.getMovementURL() + loginRepository.getUserId().toString() + "/upload" + (painLevel > -1 ? pain : "") + "?" + token;
        URL url = new URL(u);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;

        http.setDoOutput(true);
        http.setInstanceFollowRedirects(false);
        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.setRequestProperty( "charset", "utf-8");
        http.setUseCaches(false);

        // Write the content of the request
        try(OutputStream os = http.getOutputStream()) {
            // Write token
            http.connect();
            JSONObject obj = new JSONObject();
            obj.put("type", movement.getType());
            obj.put("date", movement.getDate().toString());
            JSONArray angles = new JSONArray();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                AtomicInteger n = new AtomicInteger();
                movement.getAngles().forEach(a -> {
                    JSONObject angle = new JSONObject();
                    try {
                        angle.put("position", n.getAndIncrement());
                        angle.put("angle", a.floatValue());
                        angles.put(angle);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            obj.put("angleCredentials", angles);

            byte[] out = obj.toString().getBytes();
            //Log.i(TAG, "Send movement...\n" + obj.toString());
            os.write(out);
        } catch (Exception e) {
            Log.e(TAG + " write", e.getMessage());
        }

        String status = "", message = "";
        try(InputStream is = http.getInputStream()) {
            int bufferSize = 1024;
            char[] buffer = new char[bufferSize];
            StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
            for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
                out.append(buffer, 0, numRead);
            }
            Log.i(TAG, "Reading results...");
            try {
                JSONObject c = new JSONObject(out.toString());
                status  = c.getString("status");
                message  = c.getString("message");
            } catch (Exception e) {
                Log.e(TAG + " get JSON", e.getMessage());
            }
        } catch (Exception e) {
            Log.e(TAG + " read", e.getMessage());
        }

        http.disconnect();
        return Status.builder().message(message).status(status).build();
    }

    @Override
    public void setUiForLoading() {}

    @Override
    public void setDataAfterLoading(Status result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
