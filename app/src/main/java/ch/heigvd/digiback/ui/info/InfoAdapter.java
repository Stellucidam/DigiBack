package ch.heigvd.digiback.ui.info;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.model.info.Article;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int VIEW_TYPE_ARTICLE = 1;
    private static final String TAG = "InfoAdapter";

    private InfoViewModel state;
    private LifecycleOwner lifecycleOwner;

    private List<Article> articles = new LinkedList<>();

    public InfoAdapter(InfoViewModel state, LifecycleOwner lifecycleOwner) {
        this.state = state;
        this.lifecycleOwner = lifecycleOwner;
        //setHasStableIds(true);

        state.getArticles().observe(lifecycleOwner, newArticles -> {
            articles.clear();
            articles.addAll(newArticles);
            Collections.sort(articles, (o1, o2) -> Double.compare(o1.getId(), o2.getId()));

            for (Article a :
                    articles) {
                System.out.println("Article id : " + a.getId() + " -> " + a.getTitle());
            }
            this.notifyDataSetChanged();
        });
    }

    @Override
    public long getItemId(int position) {
        return articles.get(position).getId();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ARTICLE:
                return new ArticleViewHolder(parent);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() position: " + position);
        ((ArticleViewHolder) holder).bindArticle(articles.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() : " + articles.size());
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ARTICLE;
    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final TextView articleTitle;

        private ArticleViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_info_element, parent, false));

            articleTitle = itemView.findViewById(R.id.info_title);
            Log.d(TAG, "ArticleViewHolder()");
        }

        private void bindArticle(Article article) {
            articleTitle.setText(article.getTitle());
        }
    }
}
