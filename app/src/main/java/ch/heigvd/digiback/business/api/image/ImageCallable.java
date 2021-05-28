package ch.heigvd.digiback.business.api.image;

import android.graphics.Bitmap;

import ch.heigvd.digiback.business.api.CustomCallable;

public class ImageCallable implements CustomCallable<Bitmap> {
    protected final String mediaUrl = "https://infomaldedos.ch/wp-json/wp/v2/media/";

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
