package ch.heigvd.digiback.ui.fragment.tip;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.digiback.MainActivity;
import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.model.Tip;
import ch.heigvd.digiback.ui.CalendarFragment;
import ch.heigvd.digiback.ui.fragment.exercise.ExerciseFragment;
import ch.heigvd.digiback.ui.fragment.quiz.QuizFragment;

public class TipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int VIEW_TYPE_TIP = 1;
    private static final String TAG = "TipAdapter";

    private TipsViewModel state;
    private LifecycleOwner lifecycleOwner;

    private List<Tip> tips = new LinkedList<>();

    private TipsFragment tipsFragment;

    public TipAdapter(TipsViewModel state, LifecycleOwner lifecycleOwner, TipsFragment tipsFragment) {
        this.state = state;
        this.lifecycleOwner = lifecycleOwner;
        this.tipsFragment = tipsFragment;
        setHasStableIds(true);

        state.getTips().observe(lifecycleOwner, newTips -> {
            tips.clear();
            tips.addAll(newTips);
            this.notifyDataSetChanged();
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TIP:
                return new TipViewHolder(parent, lifecycleOwner, tipsFragment);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TipViewHolder) holder).bindTip(tips.get(position));
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_TIP;
    }

    private static class TipViewHolder extends RecyclerView.ViewHolder {
        private final TextView tipTitle, tipText;
        private final CardView tipCard;
        private final LifecycleOwner lifecycleOwner;
        private final TipsFragment tipsFragment;

        private TipViewHolder(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner, TipsFragment tipsFragment) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_tip_element, parent, false));

            this.tipsFragment = tipsFragment;
            tipTitle = itemView.findViewById(R.id.tip_title);
            tipText = itemView.findViewById(R.id.tip_text);
            tipCard = itemView.findViewById(R.id.tip_card);
            this.lifecycleOwner = lifecycleOwner;
        }

        private void bindTip(Tip tip) {
            tipTitle.setText(tip.getType().toString());
            StringBuilder stringBuilder = new StringBuilder();
            switch (tip.getType()) {
                case QUIZ:
                    stringBuilder.append("Testez vos connaissances avec nos quiz.\n");
                    break;
                case WALK:
                    stringBuilder.append("Allez vous promener un peu.\n");
                    break;
                case MUSCLE:
                    stringBuilder.append("Faites quelques exercices de musculation du dos.\n");
                    break;
                case STRETCH:
                    stringBuilder.append("Faites quelques exercices d'assouplissement du dos.\n");
                    break;
                case EXERCISE:
                    stringBuilder.append("Faites quelques uns des exercices dans la liste.\n");
                    break;
                case STILL_EXERCISE:
                    stringBuilder.append("---\n");
                    break;
                case MOVEMENT_EXERCISE:
                    stringBuilder.append("Bouger un peu votre dos.\n");
                    break;
            }

            if (tip.getDuration() != null && tip.getDuration() != 0f) {
                stringBuilder.append("\nDurée conseillée : " + tip.getDuration());
            }

            if (tip.getRepetition() != 0) {
                stringBuilder.append("\nNombre conseillé : " + tip.getRepetition());
            }
            tipText.setText(stringBuilder.toString());
            tipCard.setOnClickListener(view -> {
                if (tipsFragment.getFragmentManager() != null) {
                    FragmentTransaction transaction = tipsFragment.getFragmentManager().beginTransaction();
                    switch (tip.getType()) {
                        case QUIZ:
                            // Replace whatever is in the fragment_container view with this fragment,
                            // and add the transaction to the back stack
                            transaction.replace(R.id.nav_host_fragment, new QuizFragment());
                            ((MainActivity)tipsFragment.getActivity()).getToolbar().setTitle(R.string.menu_quiz);
                            break;
                        case WALK:
                            // Replace whatever is in the fragment_container view with this fragment,
                            // and add the transaction to the back stack
                            transaction.replace(R.id.nav_host_fragment, new CalendarFragment());
                            ((MainActivity)tipsFragment.getActivity()).getToolbar().setTitle(R.string.menu_calendar);
                            break;
                        case MUSCLE:
                        case STRETCH:
                        case STILL_EXERCISE:
                        case MOVEMENT_EXERCISE:
                        case EXERCISE:
                            // Replace whatever is in the fragment_container view with this fragment,
                            // and add the transaction to the back stack
                            transaction.replace(R.id.nav_host_fragment, new ExerciseFragment());
                            ((MainActivity)tipsFragment.getActivity()).getToolbar().setTitle(R.string.menu_exercise);
                            break;
                    }

                    transaction.addToBackStack(null);
                    // Commit the transaction
                    transaction.commit();
                }
            });
        }
    }
}
