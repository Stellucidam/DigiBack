package ch.heigvd.digiback.business.api.tip;

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

import ch.heigvd.digiback.business.model.Tip;
import ch.heigvd.digiback.business.model.TipType;
import ch.heigvd.digiback.business.utils.Backend;

public class GetTips extends TipsCallable {
    private final String TAG = "GetTip";
    private final iOnTipsFetched listener;

    public GetTips(iOnTipsFetched onTipFetched) {
        this.listener = onTipFetched;
    }

    @Override
    public List<Tip> call() throws Exception {
        String token  = "?token=" + loginRepository.getToken();
        URL getTipUrl = new URL(Backend.getTipURL() + loginRepository.getUserId() + token);
        HttpsURLConnection urlConnection = (HttpsURLConnection)getTipUrl.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
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
        Log.d(TAG, stringBuilder.toString());
        List<Tip> tips = new LinkedList<>();
        try {
            JSONArray tipsArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < tipsArray.length(); i++) {
                JSONObject tip = tipsArray.getJSONObject(i);
                float duration = 0f;
                int repetition = 0;
                try {
                    duration = (float) tip.getDouble("duration");
                } catch (Exception e) {
                    Log.e(TAG, "No duration");
                }
                try {
                    repetition = tip.getInt("repetition");
                } catch (Exception e) {
                    Log.e(TAG, "No repetition");
                }

                tips.add(Tip.builder()
                        .type(TipType.getType(tip.getString("type")))
                        .duration(duration)
                        .repetition(repetition)
                        .build());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return tips;
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(List<Tip> result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
