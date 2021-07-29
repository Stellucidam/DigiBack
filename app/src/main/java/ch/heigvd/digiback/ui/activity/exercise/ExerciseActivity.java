package ch.heigvd.digiback.ui.activity.exercise;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ch.heigvd.digiback.R;

public class ExerciseActivity extends AppCompatActivity {
    private final String TAG = "ExerciseActivity";
    private Long exerciseId;
    private String exerciseTitle, exerciseImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        exerciseId = getIntent().getLongExtra("id", -1);
        exerciseTitle = getIntent().getStringExtra("title");
        exerciseImageURL = getIntent().getStringExtra("imageURL");

        TextView title = findViewById(R.id.exercise_title);
        title.setText(exerciseTitle);

        InstructionViewModel instructionViewModel =
                ViewModelProviders.of(this).get(InstructionViewModel.class);

        InstructionAdapter instructionAdapter = new InstructionAdapter(exerciseId, instructionViewModel, this, this);
        ImageView exerciseImage = findViewById(R.id.exercise_image);
        String imageName = "exercise_" + exerciseId;
        exerciseImage.setImageDrawable(getResources()
                .getDrawable(getResources()
                        .getIdentifier(imageName, "mipmap", getPackageName())));

        RecyclerView exerciseList = findViewById(R.id.exercise_instructions_view);
        exerciseList.setLayoutManager(new LinearLayoutManager(this));
        exerciseList.setAdapter(instructionAdapter);

        FloatingActionButton back = findViewById(R.id.floating_back_button);
        back.setOnClickListener(view -> super.onBackPressed());
    }
}