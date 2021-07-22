package ch.heigvd.digiback.business.api.movement;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

import ch.heigvd.digiback.business.model.movement.Movement;
import ch.heigvd.digiback.business.utils.Backend;

public class PostMovement extends MovementCallable {
    private final String TAG = "PostMovement";
    private final Movement movement;

    public PostMovement(Movement movement) {
        this.movement = movement;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Movement call() throws Exception {
        String token  = "token=" + loginRepository.getToken();
        String u = Backend.getMovementURL() + loginRepository.getUserId().toString() + "/upload?" + token;
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
            Log.i(TAG, "Send movement...");
            os.write(out);
        } catch (Exception e) {
            Log.e(TAG + " line 61", e.getMessage());
        }

        /*
        Log.d(TAG, http.getResponseMessage());
        try(InputStream is = http.getInputStream()) {
            int bufferSize = 1024;
            char[] buffer = new char[bufferSize];
            StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
            for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
                out.append(buffer, 0, numRead);
            }
            Log.i(TAG, "Reading results...");
            Log.d(TAG, out.toString());
        } catch (Exception e) {
            Log.e(TAG + " line 82", e.getMessage());
        }
        */
        http.disconnect();
        return null;
    }
}
