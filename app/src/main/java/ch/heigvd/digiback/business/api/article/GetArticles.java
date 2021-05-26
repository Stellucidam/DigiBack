package ch.heigvd.digiback.business.api.article;

import ch.heigvd.digiback.business.model.info.Article;
import ch.heigvd.digiback.business.model.info.ArticleType;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;


public class GetArticles extends ArticleClient {
    private final iOnArticlesFetched listener; //listener in fragment that shows and hides ProgressBar

    public GetArticles(iOnArticlesFetched onArticlesFetched) {
        this.listener = onArticlesFetched;
    }

    @Override
    public LinkedList<Article> call() throws Exception {
        LinkedList<Article> returnedArticles = new LinkedList<>();
        URL myUrl = new URL(baseUrl);
        HttpsURLConnection conn = (HttpsURLConnection)myUrl.openConnection();
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        // Read the result
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }

        JSONArray articles = new JSONArray(stringBuilder.toString());

        // Todo si on récup' tout, on prend pas le content
        for (int i = 0; i < articles.length(); i++) {
            JSONObject c = articles.getJSONObject(i);

            int id = c.getInt("id");
            int imageId = c.getInt("featured_media");
            String title = c.getJSONObject("title").getString("rendered");
            //int category = c.getInt("categories");
            String content = c.getJSONObject("content").getString("rendered");
            ArticleType type = ArticleType.COMPREHENSION;

            returnedArticles.add(new Article(id, imageId, title, content, type));
        }

        br.close();
        return returnedArticles; // result;
    }

    @Override
    public void setUiForLoading() {
        if (listener != null) {
            listener.showProgressBar();
        }
    }

    @Override
    public void setDataAfterLoading(LinkedList<Article> result) {
        if (listener != null) {
            listener.setDataInPageWithResult(result);
            listener.hideProgressBar();
        }
    }
}
