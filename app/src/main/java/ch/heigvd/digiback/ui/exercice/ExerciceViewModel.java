package ch.heigvd.digiback.ui.exercice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExerciceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExerciceViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}