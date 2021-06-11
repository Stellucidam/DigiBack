package ch.heigvd.digiback.ui.fragment.back_state;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BackStateViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public BackStateViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}