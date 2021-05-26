package ch.heigvd.digiback.ui.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.article.GetArticles;
import ch.heigvd.digiback.business.api.article.iOnArticlesFetched;
import ch.heigvd.digiback.business.model.info.Article;

import java.util.List;

public class InfoViewModel extends ViewModel {
    private MutableLiveData<List<Article>> articles = new MutableLiveData<>();

    private final TaskRunner runner = new TaskRunner();

    public InfoViewModel() {
        runner.executeAsync(new GetArticles(new iOnArticlesFetched() {
            @Override
            public void showProgressBar() {
                return;
            }

            @Override
            public void hideProgressBar() {
                return;
            }

            @Override
            public void setDataInPageWithResult(List<Article> result) {
                articles.setValue(result);
            }
        }));
    }

    public LiveData<List<Article>> getArticles() {
        return articles;
    }
}
