package ch.heigvd.digiback.business.api;

public interface iOnDataFetched<R> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(R result);
}
