package ch.heigvd.digiback.ui.fragment.article;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.model.Article;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int VIEW_TYPE_ARTICLE = 1;
    private static final String TAG = "ArticleAdapter";

    private ArticleViewModel state;
    private LifecycleOwner lifecycleOwner;

    private List<Article> articles = new LinkedList<>();

    private ArticleFragment articleFragment;

    public ArticleAdapter(ArticleViewModel state, LifecycleOwner lifecycleOwner, ArticleFragment articleFragment) {
        this.state = state;
        this.lifecycleOwner = lifecycleOwner;
        this.articleFragment = articleFragment;
        setHasStableIds(true);

        state.getArticles().observe(lifecycleOwner, newArticles -> {
            articles.clear();
            articles.addAll(newArticles);
            Collections.sort(articles, (o1, o2) -> Double.compare(o1.getId(), o2.getId()));
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
                return new ArticleViewHolder(parent, lifecycleOwner, articleFragment);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ArticleViewHolder) holder).bindArticle(articles.get(position));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ARTICLE;
    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final CardView articleCard;
        private final TextView articleTitle;
        private final TextView articleCategory;
        private final ImageView articleImage;
        private final LifecycleOwner lifecycleOwner;
        private final ArticleFragment articleFragment;

        private ArticleViewHolder(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner, ArticleFragment articleFragment) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_article_element, parent, false));

            this.articleFragment = articleFragment;
            articleCard = itemView.findViewById(R.id.article_card);
            articleTitle = itemView.findViewById(R.id.info_title);
            articleImage = itemView.findViewById(R.id.info_image);
            articleCategory = itemView.findViewById(R.id.info_duration);
            this.lifecycleOwner = lifecycleOwner;
        }

        private void bindArticle(Article article) {
            articleCard.setOnClickListener(view -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(article.getLink()));
                articleFragment.getActivity().startActivity(i);
            });
            articleTitle.setText(article.getTitle());
            article.getImageBM().observe(lifecycleOwner, articleImage::setImageBitmap);
            article.getCategoryName().observe(lifecycleOwner, articleCategory::setText);
        }
    }
}
