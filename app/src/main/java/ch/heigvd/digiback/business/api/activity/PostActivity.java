package ch.heigvd.digiback.business.api.activity;


import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import ch.heigvd.digiback.business.model.activity.Activity;
import ch.heigvd.digiback.business.utils.Backend;

public class PostActivity extends ActivityCallable {
    private static final String TAG = "PostActivity";
    private final Activity activity;
    private final iOnActivityFetched listener; //listener in fragment that shows and hides ProgressBar

    public PostActivity(Activity activity, iOnActivityFetched onImageFetched) {
        this.activity = activity;
        this.listener = onImageFetched;
    }

    @Override
    public Activity call() throws Exception {
        URL url = new URL(Backend.getActivityURL() + loginRepository.getUserId().toString() + "/upload");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            JSONObject obj = new JSONObject();
            obj.put("id", activity.getId());
            obj.put("date", activity.getDate().toString());
            obj.put("nbrSteps", activity.getNbrSteps());
            obj.put("nbrExercises", activity.getNbrExercises());
            obj.put("nbrQuiz", activity.getNbrQuiz());

            byte[] out = obj.toString().getBytes();
            int length = out.length;
            http.setFixedLengthStreamingMode(length);
            os.write(out);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        //http.getResponseMessage();
        // Do something with http.getInputStream()

        Log.d(TAG, http.getResponseMessage());

        return new Activity();
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(Activity activity) {
        if (listener != null) {
            listener.setDataInPageWithResult(activity);
            listener.hideProgressBar();
        }
    }
}
