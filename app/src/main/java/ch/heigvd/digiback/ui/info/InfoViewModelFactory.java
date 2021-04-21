package ch.heigvd.digiback.ui.info;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider.Factory;

import ch.heigvd.digiback.business.model.info.Article;
import ch.heigvd.digiback.business.model.info.ArticleType;
import org.jetbrains.annotations.NotNull;

public class InfoViewModelFactory implements Factory {
    private final Article article;

    private InfoViewModelFactory(Article article) {
        this.article = article;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        try {
            return (T)modelClass.getDeclaredConstructors()[0].newInstance(article);
        } catch (Exception e) {
            System.out.println("An error occured : " + e);
        }
        return null;
    }
}
