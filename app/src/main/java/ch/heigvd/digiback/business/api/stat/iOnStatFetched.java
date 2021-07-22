package ch.heigvd.digiback.business.api.stat;


import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.Stat;

public interface iOnStatFetched extends iOnDataFetched<Stat> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Stat stat);
}
