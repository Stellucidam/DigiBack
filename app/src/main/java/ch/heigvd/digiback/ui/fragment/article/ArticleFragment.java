package ch.heigvd.digiback.ui.fragment.article;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.digiback.R;

public class ArticleFragment extends Fragment {
    private ArticleViewModel state;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_article, container, false);
        ProgressBar progressBar = root.findViewById(R.id.progress_bar);

        state = new ViewModelProvider(this, new ArticleViewModelFactory()).get(ArticleViewModel.class);

        ArticleAdapter articleAdapter = new ArticleAdapter(progressBar, state, this, this);

        // LinearLayoutManager manager = new LinearLayoutManager(getContext());
        // RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        // itemAnimator.setAddDuration(1000);
        // itemAnimator.setRemoveDuration(1000);

        RecyclerView articleList = root.findViewById(R.id.info_view);
        articleList.setLayoutManager(new LinearLayoutManager(getContext()));
        //articleList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        //articleList.setItemAnimator(itemAnimator);
        articleList.setAdapter(articleAdapter);

        return root;
    }
}
