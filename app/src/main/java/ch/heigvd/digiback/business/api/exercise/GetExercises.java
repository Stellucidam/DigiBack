package ch.heigvd.digiback.business.api.exercise;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ch.heigvd.digiback.business.model.Exercise;
import ch.heigvd.digiback.business.utils.Backend;

public class GetExercises extends ExercisesCallable   {
    private final iOnExercisesFetched listener; //listener in fragment that shows and hides ProgressBar

    public GetExercises(iOnExercisesFetched listener) {
        this.listener = listener;
    }

    @Override
    public List<Exercise> call() throws Exception {
        LinkedList<Exercise> returnedExercises = new LinkedList<>();
        URL url = new URL(Backend.getExerciseURL());
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        // Read the result
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }

        JSONArray exercises = new JSONArray(stringBuilder.toString());

        for (int i = 0; i < exercises.length(); i++) {
            JSONObject c = exercises.getJSONObject(i);

            Long id = c.getLong("idExercise");
            String imageURL = c.getString("imageURL");
            String title = c.getString("title");

            returnedExercises.add(new Exercise(id, imageURL, title));
        }

        isr.close();
        br.close();
        return returnedExercises; // result;
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(List<Exercise> result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
