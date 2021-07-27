package ch.heigvd.digiback.ui.fragment.tip;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.model.Tip;

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
        //private final CardView tipCard;
        private final TextView tipTitle;
        private final LifecycleOwner lifecycleOwner;
        private final TipsFragment tipsFragment;

        private TipViewHolder(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner, TipsFragment tipsFragment) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_tip_element, parent, false));

            this.tipsFragment = tipsFragment;
            //tipCard = itemView.findViewById(R.id.tip_card);
            tipTitle = itemView.findViewById(R.id.tip_title);
            this.lifecycleOwner = lifecycleOwner;
        }

        private void bindTip(Tip tip) {
            tipTitle.setText(tip.getType().toString());
        }
    }
}
