package ch.heigvd.digiback.business.api.article;

import java.util.LinkedList;

import ch.heigvd.digiback.business.model.article.Article;

public abstract class ArticleClient extends ArticleCallable {
    protected final String postsUrl = "https://infomaldedos.ch/wp-json/wp/v2/posts";
    protected final String mediaUrl = "https://infomaldedos.ch/wp-json/wp/v2/media/";

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

