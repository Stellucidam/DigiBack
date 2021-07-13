package ch.heigvd.digiback.business.model.exercise;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import lombok.Getter;

@Getter
public class Exercise {
    private final int id;
    private final String imageURL;
    private final String title;
    private final String link;

    private MutableLiveData<Bitmap> imageBM = new MutableLiveData<>();
    private MutableLiveData<String> categoryName = new MutableLiveData<>();

    public Exercise(int id, String imageURL, String title, String link) {
        this.id = id;
        this.imageURL = imageURL;
        this.title = title;
        this.link = link;
    }
}
