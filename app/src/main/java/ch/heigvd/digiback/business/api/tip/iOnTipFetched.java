package ch.heigvd.digiback.business.api.tip;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.Tip;

public interface iOnTipFetched extends iOnDataFetched<Tip> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Tip tip);
}
