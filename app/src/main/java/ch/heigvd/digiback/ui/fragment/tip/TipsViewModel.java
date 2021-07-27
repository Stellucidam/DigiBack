package ch.heigvd.digiback.ui.fragment.tip;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.tip.GetTips;
import ch.heigvd.digiback.business.api.tip.iOnTipsFetched;
import ch.heigvd.digiback.business.model.Tip;

public class TipsViewModel extends ViewModel {
    private final String TAG = "TipsViewModel";

    private MutableLiveData<List<Tip>> tips = new MutableLiveData<>();
    private TaskRunner taskRunner = new TaskRunner();

    public TipsViewModel() {
        taskRunner.executeAsync(new GetTips(new iOnTipsFetched() {
            @Override
            public void showProgressBar() {

            }

            @Override
            public void hideProgressBar() {

            }

            @Override
            public void setDataInPageWithResult(List<Tip> tipList) {
                // todo
                Log.d(TAG, tips.toString());
                tips.postValue(tipList);
            }
        }));
    }

    public MutableLiveData<List<Tip>> getTips() {
        return tips;
    }
}