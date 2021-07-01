package ch.heigvd.digiback.business.api.activity;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import ch.heigvd.digiback.business.model.activity.Step;

public class PostStep extends StepCallable {
    private static final String TAG = "PostActivity";
    private final Long userId;
    private final Step step;
    private final iOnStepFetched listener; //listener in fragment that shows and hides ProgressBar

    public PostStep(Step step, Long userId, iOnStepFetched onStepFetched) {
        this.userId = userId;
        this.step = step;
        this.listener = onStepFetched;
    }

    @Override
    public Step call() throws Exception {
        URL url = new URL(stepsURL + userId.toString() + stepsURLEnd);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            JSONObject obj = new JSONObject();
            obj.put("date", step.getDate().toString());
            obj.put("nbrSteps", step.getNbrSteps());

            byte[] out = obj.toString().getBytes();
            int length = out.length;
            http.setFixedLengthStreamingMode(length);
            os.write(out);

        }
        //http.getResponseMessage();
        // Do something with http.getInputStream()

        Log.d(TAG, http.getResponseMessage());

        return new Step();
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(Step step) {
        if (listener != null) {
            listener.setDataInPageWithResult(step);
            listener.hideProgressBar();
        }
    }
}
