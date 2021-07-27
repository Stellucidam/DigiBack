package ch.heigvd.digiback.ui.fragment.quiz;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

public class QuizViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new QuizViewModel();
    }
}
