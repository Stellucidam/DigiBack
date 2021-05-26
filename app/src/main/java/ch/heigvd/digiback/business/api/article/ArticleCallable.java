package ch.heigvd.digiback.business.api.article;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.info.Article;

import java.util.LinkedList;

public class ArticleCallable implements CustomCallable<LinkedList<Article>> {
    @Override
    public void setDataAfterLoading(LinkedList<Article> result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public LinkedList<Article> call() throws Exception {
        return null;
    }
}
