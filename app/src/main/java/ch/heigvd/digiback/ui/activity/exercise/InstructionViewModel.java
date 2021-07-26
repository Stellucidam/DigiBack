package ch.heigvd.digiback.ui.activity.exercise;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.exercise.GetExercise;
import ch.heigvd.digiback.business.api.exercise.iOnExerciseFetched;
import ch.heigvd.digiback.business.model.Exercise;
import ch.heigvd.digiback.business.model.Instruction;

public class InstructionViewModel extends ViewModel {

    private MutableLiveData<List<Instruction>> instructions = new MutableLiveData<>();

    private final TaskRunner runner = new TaskRunner();

    public InstructionViewModel() {}

    public LiveData<List<Instruction>> getInstructions() {
        return instructions;
    }

    public void getInstructionsFrom(Long exerciseId) {
        runner.executeAsync(new GetExercise(exerciseId, new iOnExerciseFetched() {
            @Override
            public void showProgressBar() {
                return;
            }

            @Override
            public void hideProgressBar() {
                return;
            }

            @Override
            public void setDataInPageWithResult(Exercise exercise) {
                InstructionViewModel.this.instructions.setValue(exercise.getInstructions());
            }
        }));
    }
}