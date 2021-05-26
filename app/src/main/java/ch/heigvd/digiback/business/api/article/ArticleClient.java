package ch.heigvd.digiback.business.api.article;

import ch.heigvd.digiback.business.model.info.Article;

import java.util.LinkedList;

public abstract class ArticleClient extends ArticleCallable {
    protected final String baseUrl = "https://infomaldedos.ch/wp-json/wp/v2/posts";

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

