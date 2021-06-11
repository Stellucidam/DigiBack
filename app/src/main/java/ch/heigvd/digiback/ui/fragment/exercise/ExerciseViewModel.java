package ch.heigvd.digiback.ui.fragment.exercise;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExerciseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExerciseViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}