package ch.heigvd.digiback.ui.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.heigvd.digiback.business.model.info.Article;

public class AllInfoViewModel extends ViewModel {
    private final Article article;
    private MutableLiveData<String> mText;

    public AllInfoViewModel(Article article) {
        this.article = article;
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}
