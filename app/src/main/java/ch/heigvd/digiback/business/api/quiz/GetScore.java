package ch.heigvd.digiback.business.api.quiz;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ch.heigvd.digiback.business.model.Score;
import ch.heigvd.digiback.business.utils.Backend;

public class GetScore extends ScoreCallable {
    private final String TAG = "GetScore";
    private final iOnScoreFetched listener;
    private final Long idQuiz;

    public GetScore(Long idQuiz, iOnScoreFetched listener) {
        this.listener = listener;
        this.idQuiz = idQuiz;
    }

    @Override
    public Score call() throws Exception {
        String token  = "?token=" + loginRepository.getToken();
        URL url = new URL(Backend.getQuizURL() + idQuiz + "/score/user/" + loginRepository.getUserId() + token);
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

        Score score = null;
        Log.d(TAG, stringBuilder.toString());
        try {
            JSONObject scoreJSON = new JSONObject(stringBuilder.toString());
            score = Score.builder()
                    .score(scoreJSON.getInt("score"))
                    .total(scoreJSON.getInt("total"))
                    .build();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        isr.close();
        br.close();
        return score;
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(Score result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
