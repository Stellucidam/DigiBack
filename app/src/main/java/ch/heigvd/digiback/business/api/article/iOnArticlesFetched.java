package ch.heigvd.digiback.business.api.article;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.info.Article;

import java.util.List;

public interface iOnArticlesFetched extends iOnDataFetched<List<Article>> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(List<Article> result);
}
