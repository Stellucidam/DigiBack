package ch.heigvd.digiback.ui.activity.exercise;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        FloatingActionButton back = findViewById(R.id.floating_back_button);
        back.setOnClickListener(view -> super.onBackPressed());
    }
}