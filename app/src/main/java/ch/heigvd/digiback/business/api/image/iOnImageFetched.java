package ch.heigvd.digiback.business.api.image;

import android.graphics.Bitmap;

import ch.heigvd.digiback.business.api.iOnDataFetched;

public interface iOnImageFetched extends iOnDataFetched<Bitmap> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Bitmap bitmap);
}
