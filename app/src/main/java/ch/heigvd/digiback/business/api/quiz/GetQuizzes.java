package ch.heigvd.digiback.business.api.quiz;

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

import ch.heigvd.digiback.business.model.Quiz;
import ch.heigvd.digiback.business.utils.Backend;

public class GetQuizzes extends QuizzesCallable {
    private final String TAG = "GetQuizzes";
    private final iOnQuizzesFetched listener;

    public GetQuizzes(iOnQuizzesFetched listener) {
        this.listener = listener;
    }


    @Override
    public List<Quiz> call() throws Exception {
        URL url = new URL(Backend.getQuizURL());
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
        List<Quiz> quizzes = new LinkedList<>();
        Log.d(TAG, stringBuilder.toString());
        try {
            JSONArray quizArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < quizArray.length(); i++) {
                JSONObject quiz = quizArray.getJSONObject(i);
                quizzes.add(Quiz.builder()
                        .title(quiz.getString("title"))
                        .idQuiz(quiz.getLong("idQuiz"))
                        .build());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        isr.close();
        br.close();
        return quizzes;
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(List<Quiz> result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
