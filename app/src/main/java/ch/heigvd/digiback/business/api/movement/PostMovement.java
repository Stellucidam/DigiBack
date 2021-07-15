package ch.heigvd.digiback.business.api.movement;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import ch.heigvd.digiback.business.model.movement.Movement;

public class PostMovement extends MovementCallable {
    private final String TAG = "PostMovement";
    private final Movement movement;

    public PostMovement(Movement movement) {
        this.movement = movement;
    }

    @Override
    public Movement call() throws Exception {

        URL url = new URL(movementsURL + loginRepository.getUserId().toString() + "/upload");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try(OutputStream os = http.getOutputStream()) {
            http.connect();
            JSONObject obj = new JSONObject();
            obj.put("id", movement.getId());
            obj.put("type", movement.getType());
            obj.put("date", movement.getDate().toString());
            obj.put("angles", movement.getAngles());

            byte[] out = obj.toString().getBytes();
            int length = out.length;
            http.setFixedLengthStreamingMode(length);
            os.write(out);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        // http.getResponseMessage();
        // Do something with http.getInputStream()

        Log.d(TAG, http.getResponseMessage());

        return new Movement();
    }
}
