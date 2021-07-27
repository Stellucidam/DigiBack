package ch.heigvd.digiback.business.api.tip;

import java.util.List;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.Tip;

public interface iOnTipsFetched extends iOnDataFetched<List<Tip>> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(List<Tip> tips);
}
