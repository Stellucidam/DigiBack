package ch.heigvd.digiback.ui.activity.exercise;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.model.Instruction;

public class InstructionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int VIEW_TYPE_INSTRUCTION = 1;
    private static final String TAG = "InstructionAdapter";

    private InstructionViewModel state;
    private LifecycleOwner lifecycleOwner;

    private List<Instruction> instructions = new LinkedList<>();

    private ExerciseActivity exerciseActivity;

    public InstructionAdapter(
            Long exerciseId,
            InstructionViewModel state,
            LifecycleOwner lifecycleOwner,
            ExerciseActivity exerciseActivity) {
        this.state = state;
        this.lifecycleOwner = lifecycleOwner;
        this.exerciseActivity = exerciseActivity;
        setHasStableIds(true);
        state.getInstructionsFrom(exerciseId);

        state.getInstructions().observe(lifecycleOwner, newInstructions -> {
            instructions.clear();
            instructions.addAll(newInstructions);
            Collections.sort(instructions, (o1, o2) -> Double.compare(o1.getPosition(), o2.getPosition()));
            this.notifyDataSetChanged();
        });
    }

    @Override
    public long getItemId(int position) {
        return instructions.get(position).getPosition();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_INSTRUCTION:
                return new InstructionViewHolder(parent, lifecycleOwner, exerciseActivity);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((InstructionViewHolder) holder).bindInstruction(instructions.get(position));
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_INSTRUCTION;
    }

    private static class InstructionViewHolder extends RecyclerView.ViewHolder {
        private final CardView exerciseCard;
        private final TextView instructionTitle, instructionText;
        private final LifecycleOwner lifecycleOwner;
        private final ExerciseActivity exerciseActivity;

        private InstructionViewHolder(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner, ExerciseActivity exerciseActivity) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_instruction_element, parent, false));

            this.exerciseActivity = exerciseActivity;
            exerciseCard = itemView.findViewById(R.id.exercise_card);
            instructionTitle = itemView.findViewById(R.id.instruction_title);
            instructionText = itemView.findViewById(R.id.instruction_text);
            this.lifecycleOwner = lifecycleOwner;
        }

        private void bindInstruction(Instruction instruction) {
            instructionTitle.setText(instruction.getTitle());
            instructionText.setText(instruction.getInstruction());
        }
    }
}
