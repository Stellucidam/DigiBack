package ch.heigvd.digiback.business.api.activity;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Date;

import javax.net.ssl.HttpsURLConnection;

import ch.heigvd.digiback.business.model.Activity;
import ch.heigvd.digiback.business.utils.Backend;

public class GetActivity extends ActivityCallable {
    private final String date;
    private final iOnActivityFetched listener; //listener in fragment that shows and hides ProgressBar

    public GetActivity(String date, iOnActivityFetched onImageFetched) {
        this.date = date;
        this.listener = onImageFetched;
    }

    @Override
    public Activity call() throws Exception {
        URL getImageUrl = new URL(Backend.getActivityURL() + loginRepository.getUserId().toString() + "/date/" + date);
        HttpsURLConnection imageConn = (HttpsURLConnection)getImageUrl.openConnection();
        InputStream inputStream = imageConn.getInputStream();
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

        JSONObject c = new JSONObject(stringBuilder.toString());
        int id  = c.getInt("id");
        String date  = c.getString("date");
        Long nbrSteps  = c.getLong("nbrSteps");
        Long nbrExercises  = c.getLong("nbrExercises");
        Long nbrQuiz  = c.getLong("nbrQuiz");

        return new Activity(id, Date.valueOf(date), nbrSteps, nbrExercises, nbrQuiz);
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
