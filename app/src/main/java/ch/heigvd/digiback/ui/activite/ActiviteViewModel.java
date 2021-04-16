package ch.heigvd.digiback.ui.activite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActiviteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ActiviteViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}