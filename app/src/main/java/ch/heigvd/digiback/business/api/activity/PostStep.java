package ch.heigvd.digiback.business.api.activity;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

import ch.heigvd.digiback.business.api.StatusCallable;
import ch.heigvd.digiback.business.api.iOnStatusFetched;
import ch.heigvd.digiback.business.model.Status;
import ch.heigvd.digiback.business.model.Step;
import ch.heigvd.digiback.business.utils.Backend;

public class PostStep extends StatusCallable {

    private static final String TAG = "PostStep";
    private final Step step;
    private final iOnStatusFetched listener; //listener in fragment that shows and hides ProgressBar

    public PostStep(Step step, iOnStatusFetched iOnStatusFetched) {
        this.step = step;
        this.listener = iOnStatusFetched;
    }

    @Override
    public Status call() throws Exception {
        String token  = "?token=" + loginRepository.getToken();
        URL url = new URL(Backend.getActivityURL() + loginRepository.getUserId().toString() + "/upload/steps" + token);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            JSONObject obj = new JSONObject();
            obj.put("date", simpleDateFormat.format(step.getDate()));
            obj.put("nbrSteps", step.getNbrSteps());

            byte[] out = obj.toString().getBytes();
            Log.d(TAG, obj.toString());
            os.write(out);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        //http.getResponseMessage();
        // Do something with http.getInputStream()

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
