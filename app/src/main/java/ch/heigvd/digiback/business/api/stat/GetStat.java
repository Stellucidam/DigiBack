package ch.heigvd.digiback.business.api.stat;


import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ch.heigvd.digiback.business.model.MovementType;
import ch.heigvd.digiback.business.model.Stat;
import ch.heigvd.digiback.business.utils.Backend;


public class GetStat extends StatCallable {
    private final String TAG = "GetStat";
    private final int days;
    private final iOnStatFetched listener; //listener in fragment that shows and hides ProgressBar

    public GetStat(int days, iOnStatFetched onImageFetched) {
        this.days = days;
        this.listener = onImageFetched;
    }

    public GetStat(iOnStatFetched onImageFetched) {
        this.days = -1;
        this.listener = onImageFetched;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Stat call() throws Exception {
        Log.d(TAG, "Call GetStat...");
        String token  = "token=" + loginRepository.getToken();
        URL url;
        if (days < 0) {
            url = new URL(Backend.getStatURL() + loginRepository.getUserId().toString() + "?" + token);
        } else {
            url = new URL(Backend.getStatURL() + loginRepository.getUserId().toString() + "/days/" + days + "?" + token);
        }

        //Log.d(TAG, "URL : " + url.toString());
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

        //Log.d(TAG, "URL : " + url.toString());
        //Log.d(TAG, "Result : " + stringBuilder.toString());
        JSONObject c = new JSONObject(stringBuilder.toString());
        float highestAngle = (float) c.getDouble("highestAngle");
        float angleAverage = (float) c.getDouble("angleAverage");
        JSONArray angleEvolutionJSON = c.getJSONArray("angleEvolution");
        List<Float> angleEvolution = new LinkedList<>();
        for (int i = 0; i < angleEvolutionJSON.length(); i++) {
            angleEvolution.add((float) angleEvolutionJSON.getDouble(i));
        }
        float painAverage = (float) c.getDouble("painAverage");
        JSONArray painEvolutionJSON = c.getJSONArray("painEvolution");
        List<Integer> painEvolution = new LinkedList<>();
        for (int i = 0; i < painEvolutionJSON.length(); i++) {
            painEvolution.add(painEvolutionJSON.getInt(i));
        }

        JSONObject statByMovementTypeJSON = c.getJSONObject("statByMovementType");
        Map<MovementType, Stat> statByMovementType = new HashMap<>();
        MovementType.getTypes().forEach(movementType -> {
            JSONObject s;
            try {
                s = statByMovementTypeJSON.getJSONObject(movementType.name());
                JSONArray aeJSON = s.getJSONArray("angleEvolution");
                List<Float> ae = new LinkedList<>();
                for (int i = 0; i < aeJSON.length(); i++) {
                    ae.add((float) aeJSON.getDouble(i));
                }
                JSONArray peJSON = s.getJSONArray("painEvolution");
                List<Integer> pe = new LinkedList<>();
                for (int i = 0; i < peJSON.length(); i++) {
                    pe.add(peJSON.getInt(i));
                }
                statByMovementType.put(
                        movementType,
                        Stat.builder()
                                .angleAverage((float) s.getDouble("angleAverage"))
                                .angleEvolution(ae)
                                .highestAngle((float) s.getDouble("highestAngle"))
                                .painAverage((float) s.getDouble("painAverage"))
                                .painEvolution(pe)
                                .build()
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        Log.d(TAG, "Finished");

        return Stat.builder()
                .angleAverage(angleAverage)
                .angleEvolution(angleEvolution)
                .highestAngle(highestAngle)
                .painAverage(painAverage)
                .painEvolution(painEvolution)
                .statByMovementType(statByMovementType)
                .build();
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(Stat stat) {
        if (listener != null) {
            listener.setDataInPageWithResult(stat);
            listener.hideProgressBar();
        }
    }
}
