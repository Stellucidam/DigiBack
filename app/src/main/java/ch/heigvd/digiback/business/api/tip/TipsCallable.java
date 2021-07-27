package ch.heigvd.digiback.business.api.tip;

import java.util.List;

import ch.heigvd.digiback.business.api.CustomCallable;
import ch.heigvd.digiback.business.model.Tip;

public class TipsCallable implements CustomCallable<List<Tip>> {
    @Override
    public void setDataAfterLoading(List<Tip> result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public List<Tip> call() throws Exception {
        return null;
    }
}
