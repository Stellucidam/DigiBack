package ch.heigvd.digiback.business.api.article;

import java.util.LinkedList;

import ch.heigvd.digiback.business.model.Article;

public abstract class ArticleClient extends ArticleCallable {
    @Override
    public void setUiForLoading() {

    }

    @Override
    public void setDataAfterLoading(LinkedList<Article> result) {

    }

    @Override
    public LinkedList<Article> call() throws Exception {
        return null;
    }
}

