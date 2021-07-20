package ch.heigvd.digiback.business.api.image;

import android.graphics.Bitmap;

import ch.heigvd.digiback.business.api.CustomCallable;

public class ImageCallable implements CustomCallable<Bitmap> {
    @Override
    public void setDataAfterLoading(Bitmap result) {

    }

    @Override
    public void setUiForLoading() {

    }

    @Override
    public Bitmap call() throws Exception {
        return null;
    }
}
