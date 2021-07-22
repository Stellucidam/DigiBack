package ch.heigvd.digiback.business.api.article;

import java.util.List;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.Article;

public interface iOnArticlesFetched extends iOnDataFetched<List<Article>> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(List<Article> articles);
}
