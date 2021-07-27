package ch.heigvd.digiback.ui.activity.quiz;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ch.heigvd.digiback.R;

public class QuizActivity extends AppCompatActivity {
    private Long idQuiz;
    private String titleQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        idQuiz = getIntent().getLongExtra("idQuiz", -1);
        titleQuiz = getIntent().getStringExtra("title");
        TextView title = findViewById(R.id.quiz_title);
        title.setText(titleQuiz);


        FloatingActionButton back = findViewById(R.id.floating_back_button);
        back.setOnClickListener(view -> super.onBackPressed());
    }
}