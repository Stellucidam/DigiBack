package ch.heigvd.digiback.business.api.activity;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ch.heigvd.digiback.business.model.Activity;
import ch.heigvd.digiback.business.utils.Backend;

public class GetActivity extends ActivityCallable {
    private final String TAG = "GetActivity";
    private final String date;
    private final iOnActivityFetched listener; //listener in fragment that shows and hides ProgressBar

    public GetActivity(String date, iOnActivityFetched onImageFetched) {
        this.date = date;
        this.listener = onImageFetched;
    }

    @Override
    public Activity call() throws Exception {
        String token  = "?token=" + loginRepository.getToken();
        URL url = new URL(Backend.getActivityURL() + loginRepository.getUserId().toString() + "/date/" + date + token);
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        // Read the result
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
        bufferedReader.close();

        int id = 0;
        Long nbrSteps = 0L;
        List<Long> exercises = new LinkedList<>();
        Long nbrQuiz = 0L;
        try {
            JSONObject c = new JSONObject(stringBuilder.toString());
            String date  = c.getString("date");
            nbrSteps  = c.getLong("nbrSteps");
            JSONArray exercicesJSON  = c.getJSONArray("exercices");
            for (int i = 0; i < exercicesJSON.length(); i++) {
                exercises.add(exercicesJSON.getLong(i));
            }
            nbrQuiz  = c.getLong("nbrQuiz");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return new Activity(id, Date.valueOf(date), nbrSteps, exercises, nbrQuiz);
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
