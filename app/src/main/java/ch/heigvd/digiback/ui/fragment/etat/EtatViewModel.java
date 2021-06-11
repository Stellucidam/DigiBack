package ch.heigvd.digiback.ui.fragment.etat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EtatViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public EtatViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}