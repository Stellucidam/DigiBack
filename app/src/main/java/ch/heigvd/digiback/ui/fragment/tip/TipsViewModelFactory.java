package ch.heigvd.digiback.ui.fragment.tip;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import ch.heigvd.digiback.ui.fragment.exercise.ExerciseViewModel;

public class TipsViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new TipsViewModel();
    }
}
