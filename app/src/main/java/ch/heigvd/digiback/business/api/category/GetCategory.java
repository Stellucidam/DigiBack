package ch.heigvd.digiback.business.api.category;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ch.heigvd.digiback.business.model.Category;
import ch.heigvd.digiback.business.utils.Backend;

public class GetCategory extends CategoryCallable {
    private final int id;
    private final iOnCategoryFetched listener; //listener in fragment that shows and hides ProgressBar

    public GetCategory(int id, iOnCategoryFetched onImageFetched) {
        this.id = id;
        this.listener = onImageFetched;
    }


    @Override
    public Category call() throws Exception {
        URL getImageUrl = new URL(Backend.categoriesURL + id);
        HttpsURLConnection imageConn = (HttpsURLConnection)getImageUrl.openConnection();
        InputStream inputStream = imageConn.getInputStream();
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
        JSONObject c = new JSONObject(stringBuilder.toString());
        String name = c.getString("name");

        return new Category(id, name);
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(Category category) {
        if (listener != null) {
            listener.setDataInPageWithResult(category);
            listener.hideProgressBar();
        }
    }
}
