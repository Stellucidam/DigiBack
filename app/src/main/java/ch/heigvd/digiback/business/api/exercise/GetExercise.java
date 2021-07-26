package ch.heigvd.digiback.business.api.exercise;

import android.util.Log;

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
import ch.heigvd.digiback.business.model.Instruction;
import ch.heigvd.digiback.business.utils.Backend;

public class GetExercise extends ExerciseCallable {
    private final String TAG = "GetExercise";
    private final iOnExerciseFetched listener; //listener in fragment that shows and hides ProgressBar
    private final Long exerciseId;

    public GetExercise(Long exerciseId, iOnExerciseFetched listener) {
        this.exerciseId = exerciseId;
        this.listener = listener;
    }

    @Override
    public Exercise call() throws Exception {
        URL url = new URL(Backend.getExerciseURL() + exerciseId);
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

        String imageURL = "";
        String title = "";
        List<Instruction> instructions = new LinkedList<>();
        Log.d(TAG, stringBuilder.toString());
        try {
            JSONObject exercise = new JSONObject(stringBuilder.toString());

            imageURL = exercise.getString("imageURL");
            title = exercise.getString("title");
            JSONArray instructionsArray = exercise.getJSONArray("instructions");
            for (int i = 0; i < instructionsArray.length(); i++) {
                JSONObject c = instructionsArray.getJSONObject(i);

                int instrPos = c.getInt("position");
                String instruction = c.getString("instruction");
                String instrTitle = c.getString("title");

                instructions.add(Instruction.builder()
                        .position(instrPos)
                        .instruction(instruction)
                        .title(instrTitle)
                        .build());
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        isr.close();
        br.close();
        return new Exercise(exerciseId, imageURL, title, instructions);
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(Exercise result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
