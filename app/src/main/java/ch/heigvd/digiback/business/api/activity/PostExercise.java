package ch.heigvd.digiback.business.api.activity;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

import ch.heigvd.digiback.business.api.StatusCallable;
import ch.heigvd.digiback.business.api.iOnStatusFetched;
import ch.heigvd.digiback.business.model.DoneExercise;
import ch.heigvd.digiback.business.model.Status;
import ch.heigvd.digiback.business.utils.Backend;

public class PostExercise extends StatusCallable {

    private static final String TAG = "PostExercise";
    private final DoneExercise doneExercise;
    private final iOnStatusFetched listener; //listener in fragment that shows and hides ProgressBar

    public PostExercise(DoneExercise doneExercise, iOnStatusFetched iOnStatusFetched) {
        this.doneExercise = doneExercise;
        this.listener = iOnStatusFetched;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Status call() throws Exception {
        String token  = "?token=" + loginRepository.getToken();
        URL url = new URL(Backend.getActivityURL() + loginRepository.getUserId().toString() + "/upload/exercises" + token);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        try(OutputStream os = http.getOutputStream()) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            JSONObject obj = new JSONObject();
            obj.put("date", simpleDateFormat.format(doneExercise.getDate()));
            JSONArray ex = new JSONArray();
            doneExercise.getIdExercises().forEach(ex::put);
            obj.put("idExercises", ex);

            byte[] out = obj.toString().getBytes();
            Log.d(TAG, obj.toString());
            os.write(out);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, http.getResponseMessage());

        http.connect();
        return Status.builder().status(http.getResponseMessage()).build();
    }

    @Override
    public void setUiForLoading() {}

    @Override
    public void setDataAfterLoading(Status status) {
        if (listener != null) {
            listener.setDataInPageWithResult(status);
            listener.hideProgressBar();
        }
    }
}
