package ch.heigvd.digiback.business.api.quiz;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ch.heigvd.digiback.business.model.QuestionAnswer;
import ch.heigvd.digiback.business.utils.Backend;

public class GetAnswers extends AnswersCallable {
    private final String TAG = "GetAnswers";
    private final iOnAnswersFetched listener;
    private final Long idQuiz;

    public GetAnswers(Long idQuiz, iOnAnswersFetched listener) {
        this.listener = listener;
        this.idQuiz = idQuiz;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Map<Long, QuestionAnswer> call() throws Exception {
        String token  = "?token=" + loginRepository.getToken();
        URL url = new URL(Backend.getQuizURL() + idQuiz + "/answers/user/" + loginRepository.getUserId() + token);
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

        Map<Long, QuestionAnswer> answerMap = new HashMap<>();
        Log.d(TAG, stringBuilder.toString());

        try {
            JSONArray answersJSON = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < answersJSON.length(); i++) {
                JSONObject a = answersJSON.getJSONObject(i);
                answerMap.put(a.getLong("idQuestion"), QuestionAnswer.valueOf(a.getString("answer")));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        isr.close();
        br.close();
        return answerMap;
    }

    @Override
    public void setUiForLoading() {}

    @Override
    public void setDataAfterLoading(Map<Long, QuestionAnswer> result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
