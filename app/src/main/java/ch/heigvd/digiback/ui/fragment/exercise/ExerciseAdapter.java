package ch.heigvd.digiback.ui.fragment.exercise;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.activity.PostExercise;
import ch.heigvd.digiback.business.api.iOnStatusFetched;
import ch.heigvd.digiback.business.model.DoneExercise;
import ch.heigvd.digiback.business.model.Exercise;
import ch.heigvd.digiback.business.model.Status;
import ch.heigvd.digiback.ui.activity.exercise.ExerciseActivity;

public class ExerciseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int VIEW_TYPE_EXERCISE = 1;
    private static final String TAG = "ExerciseAdapter";

    private ExerciseViewModel state;
    private LifecycleOwner lifecycleOwner;

    private List<Exercise> exercises = new LinkedList<>();
    private MutableLiveData<List<Long>> doneExercises = new MutableLiveData<>();

    private ExerciseFragment exerciseFragment;

    public ExerciseAdapter(ExerciseViewModel state, LifecycleOwner lifecycleOwner, ExerciseFragment exerciseFragment) {
        this.state = state;
        this.lifecycleOwner = lifecycleOwner;
        this.exerciseFragment = exerciseFragment;
        setHasStableIds(true);

        state.getExercises().observe(lifecycleOwner, newExercises -> {
            exercises.clear();
            exercises.addAll(newExercises);
            Collections.sort(exercises, (o1, o2) -> Double.compare(o1.getId(), o2.getId()));
            this.notifyDataSetChanged();
        });

        exerciseFragment.getExercises().observe(lifecycleOwner, newExercises -> {
            doneExercises.postValue(newExercises);
        });
    }

    @Override
    public long getItemId(int position) {
        return exercises.get(position).getId();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_EXERCISE:
                return new ExerciseViewHolder(parent, lifecycleOwner, exerciseFragment, doneExercises);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ExerciseViewHolder) holder).bindExercise(exercises.get(position));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_EXERCISE;
    }

    private static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final CardView exerciseCard;
        private final TextView exerciseTitle, doneXTimes;
        private final ImageView exerciseImage;
        private final LifecycleOwner lifecycleOwner;
        private final ExerciseFragment exerciseFragment;
        private final Button doExercise;
        private MutableLiveData<List<Long>> doneExercises;

        private ExerciseViewHolder(
                @NonNull ViewGroup parent,
                LifecycleOwner lifecycleOwner,
                ExerciseFragment exerciseFragment,
                MutableLiveData<List<Long>> doneExercises) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_exercise_element, parent, false));

            this.exerciseFragment = exerciseFragment;
            this.doneExercises = doneExercises;
            exerciseCard = itemView.findViewById(R.id.exercise_card);
            exerciseTitle = itemView.findViewById(R.id.exo_title);
            doneXTimes = itemView.findViewById(R.id.exo_done_x_times);
            exerciseImage = itemView.findViewById(R.id.exo_image);
            doExercise = itemView.findViewById(R.id.mark_exercise_as_done);
            doExercise.setBackgroundColor(exerciseFragment.getContext().getResources().getColor(R.color.colorSecondary));
            this.lifecycleOwner = lifecycleOwner;

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void bindExercise(Exercise exercise) {
            doneExercises.observe(lifecycleOwner, newExercises -> {
                Log.d(TAG, newExercises.toString());

                if (newExercises.contains(exercise.getId())) {
                    doExercise.setText(exerciseFragment.getString(R.string.mark_exercise_as_done_again));
                    doExercise.setBackgroundColor(exerciseFragment.getContext().getResources().getColor(R.color.colorAccent));
                    int i = (int) newExercises.stream().filter(ex -> ex.equals(exercise.getId())).count();
                    doneXTimes.setText(exerciseFragment.getString(R.string.done) +
                            " " + i + " " + exerciseFragment.getString(R.string.nbr_times));
                } else {
                    doneXTimes.setText(exerciseFragment.getString(R.string.not_done));
                }
            });

            exerciseCard.setOnClickListener(view -> {
                Intent i = new Intent(exerciseFragment.getContext(), ExerciseActivity.class);
                i.putExtra("id", exercise.getId());
                i.putExtra("title", exercise.getTitle());
                i.putExtra("imageURL", exercise.getImageURL());
                exerciseFragment.startActivity(i);
            });
            doExercise.setOnClickListener(view -> {
                TaskRunner taskRunner = new TaskRunner();
                List<Long> exerciseId = new LinkedList<>();
                exerciseId.add(exercise.getId());
                taskRunner.executeAsync(new PostExercise(
                        new DoneExercise(new java.sql.Date(new Date().getTime()), exerciseId),
                        new iOnStatusFetched() {
                            @Override
                            public void showProgressBar() {

                            }

                            @Override
                            public void hideProgressBar() {

                            }

                            @Override
                            public void setDataInPageWithResult(Status status) {
                                Log.d(TAG, status.getStatus() + " " + status.getMessage());
                                if (status.getStatus().equals("OK")) {
                                    Toast.makeText(
                                            exerciseFragment.getContext(),
                                            "L'execice est marqué comme fait.\nBravo !",
                                            Toast.LENGTH_LONG).show();
                                    doExercise.setText(exerciseFragment.getString(R.string.mark_exercise_as_done_again));
                                    doExercise.setBackgroundColor(exerciseFragment.getContext().getResources().getColor(R.color.colorAccent));
                                    doneExercises.getValue().add(exercise.getId());
                                    int i = (int) doneExercises.getValue().stream().filter(ex -> ex.equals(exercise.getId())).count();
                                    doneXTimes.setText(exerciseFragment.getString(R.string.done) +
                                            " " + i + " " + exerciseFragment.getString(R.string.nbr_times));
                                }
                            }
                }));
            });
            exerciseTitle.setText(exercise.getTitle());
            String imageName = "exercise_" + exercise.getId();
            exerciseImage.setImageDrawable(exerciseFragment.getActivity()
                    .getResources()
                    .getDrawable(exerciseFragment.getActivity()
                            .getResources()
                            .getIdentifier(imageName, "mipmap", exerciseFragment.getActivity().getPackageName())));
        }
    }
}
