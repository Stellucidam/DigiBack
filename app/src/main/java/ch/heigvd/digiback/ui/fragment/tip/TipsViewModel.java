package ch.heigvd.digiback.ui.fragment.tip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TipsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TipsViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}