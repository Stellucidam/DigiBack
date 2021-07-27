package ch.heigvd.digiback.ui.fragment.quiz;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.model.Quiz;
import ch.heigvd.digiback.ui.activity.quiz.QuizActivity;

public class QuizAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int VIEW_TYPE_QUIZ = 1;
    private static final String TAG = "QuizAdapter";

    private QuizViewModel state;
    private LifecycleOwner lifecycleOwner;

    private List<Quiz> quizzes = new LinkedList<>();

    private QuizFragment quizFragment;

    public QuizAdapter(QuizViewModel state, LifecycleOwner lifecycleOwner, QuizFragment quizFragment) {
        this.state = state;
        this.lifecycleOwner = lifecycleOwner;
        this.quizFragment = quizFragment;
        setHasStableIds(true);

        state.getQuiz().observe(lifecycleOwner, newQuizzes -> {
            quizzes.clear();
            quizzes.addAll(newQuizzes);
            this.notifyDataSetChanged();
        });
    }

    @Override
    public long getItemId(int position) {
        return quizzes.get(position).getIdQuiz();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_QUIZ:
                return new QuizViewHolder(parent, lifecycleOwner, quizFragment);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((QuizViewHolder) holder).bindQuiz(quizzes.get(position));
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_QUIZ;
    }

    private static class QuizViewHolder extends RecyclerView.ViewHolder {
        private final TextView quizTitle;
        private final CardView quizCard;
        private final LifecycleOwner lifecycleOwner;
        private final QuizFragment quizFragment;

        private QuizViewHolder(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner, QuizFragment quizFragment) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_quiz_element, parent, false));

            this.quizFragment = quizFragment;
            quizTitle = itemView.findViewById(R.id.quiz_element_title);
            quizCard = itemView.findViewById(R.id.quiz_card);
            this.lifecycleOwner = lifecycleOwner;
        }

        private void bindQuiz(Quiz quiz) {
            quizCard.setOnClickListener(view -> {
                Intent i = new Intent(quizFragment.getContext(), QuizActivity.class);
                i.putExtra("id", quiz.getIdQuiz());
                i.putExtra("title", quiz.getTitle());
                quizFragment.startActivity(i);
            });
            quizTitle.setText(quiz.getTitle());
        }
    }
}
