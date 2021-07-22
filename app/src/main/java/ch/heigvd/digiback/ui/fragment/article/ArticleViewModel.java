package ch.heigvd.digiback.ui.fragment.article;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.article.GetArticles;
import ch.heigvd.digiback.business.api.article.iOnArticlesFetched;
import ch.heigvd.digiback.business.api.category.GetCategory;
import ch.heigvd.digiback.business.api.category.iOnCategoryFetched;
import ch.heigvd.digiback.business.api.image.GetImage;
import ch.heigvd.digiback.business.api.image.iOnImageFetched;
import ch.heigvd.digiback.business.model.Article;
import ch.heigvd.digiback.business.model.Category;

public class ArticleViewModel extends ViewModel {
    private MutableLiveData<List<Article>> articles = new MutableLiveData<>();

    private final TaskRunner runner = new TaskRunner();

    public ArticleViewModel() {
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
            public void setDataInPageWithResult(List<Article> articles) {
                for (Article article :
                        articles) {
                    final TaskRunner taskRunner = new TaskRunner();
                    taskRunner.executeAsync(new GetImage(article.getImageURL(), new iOnImageFetched() {
                        @Override
                        public void showProgressBar() {
                            return;
                        }

                        @Override
                        public void hideProgressBar() {
                            return;
                        }

                        @Override
                        public void setDataInPageWithResult(Bitmap bitmap) {
                            article.getImageBM().postValue(bitmap);
                        }
                    }));

                    taskRunner.executeAsync(new GetCategory(article.getCategory(), new iOnCategoryFetched() {
                        @Override
                        public void showProgressBar() {
                            return;
                        }

                        @Override
                        public void hideProgressBar() {
                            return;
                        }

                        @Override
                        public void setDataInPageWithResult(Category category) {
                            article.getCategoryName().postValue(category.getName());
                        }
                    }));
                }

                ArticleViewModel.this.articles.setValue(articles);
            }
        }));
    }

    public LiveData<List<Article>> getArticles() {
        return articles;
    }
}
