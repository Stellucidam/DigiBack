package ch.heigvd.digiback.business.api.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetImage extends ImageCallable {
    private final String url;
    private final iOnImageFetched listener; //listener in fragment that shows and hides ProgressBar

    public GetImage(String url, iOnImageFetched onImageFetched) {
        this.url = url;
        this.listener = onImageFetched;
    }


    @Override
    public Bitmap call() throws Exception {
        URL getImageUrl = new URL(url);
        HttpsURLConnection imageConn = (HttpsURLConnection)getImageUrl.openConnection();
        InputStream imageIs = imageConn.getInputStream();
        InputStreamReader imageIsr = new InputStreamReader(imageIs);
        BufferedReader imageBR = new BufferedReader(imageIsr);
        StringBuilder imageStringBuilder = new StringBuilder();
        String imageLine;
        // Read the result
        while ((imageLine = imageBR.readLine()) != null) {
            imageStringBuilder.append(imageLine);
        }
        imageIs.close();
        imageBR.close();
        JSONObject image = new JSONObject(imageStringBuilder.toString());
        String imageURL = image.getJSONObject("media_details").getJSONObject("sizes").getJSONObject("thumbnail").getString("source_url");

        return BitmapFactory.decodeStream(new URL(imageURL).openConnection().getInputStream());
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(Bitmap result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
