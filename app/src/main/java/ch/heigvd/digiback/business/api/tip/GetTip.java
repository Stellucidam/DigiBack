package ch.heigvd.digiback.business.api.tip;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ch.heigvd.digiback.business.model.Tip;
import ch.heigvd.digiback.business.model.TipType;
import ch.heigvd.digiback.business.utils.Backend;

public class GetTip extends TipCallable {
    private final iOnTipFetched listener;

    public GetTip(iOnTipFetched onTipFetched) {
        this.listener = onTipFetched;
    }

    @Override
    public Tip call() throws Exception {
        URL getTipUrl = new URL(Backend.getTipURL());
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
        JSONObject tip = new JSONObject(stringBuilder.toString());

        return Tip.builder()
                .type(TipType.getType(tip.getString("type")))
                .duration((float) tip.getDouble("duration"))
                .repetition(tip.getInt("repetition"))
                .build();
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(Tip result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
