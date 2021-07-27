package ch.heigvd.digiback.business.api.quiz;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import ch.heigvd.digiback.business.api.StatusCallable;
import ch.heigvd.digiback.business.api.iOnStatusFetched;
import ch.heigvd.digiback.business.model.QuestionAnswer;
import ch.heigvd.digiback.business.model.Status;
import ch.heigvd.digiback.business.utils.Backend;

public class PostAnswer extends StatusCallable {
    private final String TAG = "PostAnswer";
    private final iOnStatusFetched listener;
    private final Long idQuiz;
    private final Long idQuestion;
    private final QuestionAnswer answer;

    public PostAnswer(Long idQuiz, Long idQuestion, QuestionAnswer answer, iOnStatusFetched listener) {
        this.listener = listener;
        this.idQuiz = idQuiz;
        this.idQuestion = idQuestion;
        this.answer = answer;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Status call() throws Exception {
        String token  = "?token=" + loginRepository.getToken();
        String u = Backend.getQuizURL() + "user/" + loginRepository.getUserId().toString() + token;
        URL url = new URL(u);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;

        http.setDoOutput(true);
        http.setInstanceFollowRedirects(false);
        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.setRequestProperty( "charset", "utf-8");
        http.setUseCaches(false);

        // Write the content of the request
        try(OutputStream os = http.getOutputStream()) {
            // Write token
            http.connect();
            JSONObject obj = new JSONObject();
            obj.put("idQuestion", idQuestion);
            obj.put("idQuiz", idQuiz);
            obj.put("answer", answer);

            byte[] out = obj.toString().getBytes();
            //Log.i(TAG, "Send movement...\n" + obj.toString());
            os.write(out);
        } catch (Exception e) {
            Log.e(TAG + " write", e.getMessage());
        }

        String status = "", message = "";
        try(InputStream is = http.getInputStream()) {
            int bufferSize = 1024;
            char[] buffer = new char[bufferSize];
            StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
            for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
                out.append(buffer, 0, numRead);
            }
            Log.i(TAG, "Reading results...");
            try {
                JSONObject c = new JSONObject(out.toString());
                status  = c.getString("status");
                message  = c.getString("message");
            } catch (Exception e) {
                Log.e(TAG + " get JSON", e.getMessage());
            }
        } catch (Exception e) {
            Log.e(TAG + " read", e.getMessage());
        }

        http.disconnect();
        return Status.builder().message(message).status(status).build();
    }

    @Override
    public void setUiForLoading() {}

    @Override
    public void setDataAfterLoading(Status result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
