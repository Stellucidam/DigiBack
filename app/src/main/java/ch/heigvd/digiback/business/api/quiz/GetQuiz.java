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

import ch.heigvd.digiback.business.model.Question;
import ch.heigvd.digiback.business.model.Quiz;
import ch.heigvd.digiback.business.utils.Backend;

public class GetQuiz extends QuizCallable {
    private final String TAG = "GetQuiz";
    private final iOnQuizFetched listener;
    private final Long idQuiz;

    public GetQuiz(Long idQuiz, iOnQuizFetched listener) {
        this.listener = listener;
        this.idQuiz = idQuiz;
    }

    @Override
    public Quiz call() throws Exception {
        URL url = new URL(Backend.getQuizURL() + idQuiz);
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

        Quiz quiz = null;
        Log.d(TAG, stringBuilder.toString());
        try {
            JSONObject quizJSON = new JSONObject(stringBuilder.toString());
            List<Question> questions = new LinkedList<>();
            JSONArray questionArray = quizJSON.getJSONArray("questions");
            for (int i = 0; i < questionArray.length(); i++) {
                JSONObject question = questionArray.getJSONObject(i);
                questions.add(Question.builder()
                        .idQuestion(question.getLong("idQuestion"))
                        .title(question.getString("title"))
                        .build());
            }

            quiz = Quiz.builder()
                    .title(quizJSON.getString("title"))
                    .questions(questions)
                    .build();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        isr.close();
        br.close();
        return quiz;
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(Quiz result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
