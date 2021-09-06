package ch.heigvd.digiback.ui.fragment.article;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ch.heigvd.digiback.R;

public class ArticleFragment extends Fragment {
    private final String TAG = "ArticleFragment";
    private ArticleViewModel state;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_article, container, false);
        ProgressBar progressBar = root.findViewById(R.id.progress_bar);

        setArticles(root, progressBar);

        // Swipe to refresh
        Fragment frg = this;
        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.swipe_container);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction ft = frg.getParentFragmentManager().beginTransaction();
                // Reload current fragment
                ft.detach(frg);
                ft.remove(frg);
                ft.attach(frg);
                setArticles(root, progressBar);
                try {
                    ft.commit();
                } catch (Exception e) {
                    Log.e(TAG, "Commit already called !");
                }
                pullToRefresh.setRefreshing(false);
            }
        });

        return root;
    }

    private void setArticles(View root, ProgressBar progressBar) {
        state = new ViewModelProvider(this, new ArticleViewModelFactory()).get(ArticleViewModel.class);

        ArticleAdapter articleAdapter = new ArticleAdapter(progressBar, state, this, this);
        RecyclerView articleList = root.findViewById(R.id.info_view);
        articleList.setLayoutManager(new LinearLayoutManager(getContext()));
        articleList.setAdapter(articleAdapter);
    }
}
