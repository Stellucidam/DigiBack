package ch.heigvd.digiback.business.api.movement;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

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
        String urlParameters  = "token=" + loginRepository.getToken();
        byte[] postData       = urlParameters.getBytes(StandardCharsets.UTF_8);
        String u = Backend.getMovementURL() + loginRepository.getUserId().toString() + "/upload";
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
            os.write(postData);
            http.connect();
            JSONObject obj = new JSONObject();
            obj.put("id", movement.getId());
            obj.put("type", movement.getType());
            obj.put("date", movement.getDate().toString());
            JSONArray angles = new JSONArray();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                movement.getAngles().forEach(angles::put);
            }

            obj.put("angles", angles);

            byte[] out = obj.toString().getBytes();
            int length = out.length;
            Log.i(TAG, "Send object : " + obj.toString());
            // http.setFixedLengthStreamingMode(length);
            os.write(out);
        } catch (Exception e) {
            Log.e(TAG + " line 61", e.getMessage());
        }
        // http.getResponseMessage();
        //todo Do something with http.getInputStream()

        Log.d(TAG + " line 70", http.getResponseMessage());
        try(InputStream is = http.getInputStream()) {
            Log.i(TAG, "Reading results...");
            int bufferSize = 1024;
            char[] buffer = new char[bufferSize];
            StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
            for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
                out.append(buffer, 0, numRead);
            }
            Log.d(TAG, out.toString());
        } catch (Exception e) {
            Log.e(TAG + " line 82", e.getMessage());
        }
        http.disconnect();

        return new Movement();
    }
}
