package ch.heigvd.digiback.ui.fragment.conseils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConseilsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConseilsViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}