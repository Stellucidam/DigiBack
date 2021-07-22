package ch.heigvd.digiback.business.model;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import lombok.Getter;

@Getter
public class Article {
    private final int id;
    private final String imageURL;
    private final String title;
    private final String link;
    private final int category;

    private MutableLiveData<Bitmap> imageBM = new MutableLiveData<>();
    private MutableLiveData<String> categoryName = new MutableLiveData<>();

    public Article(int id, String imageURL, String title, int category, String link) {
        this.id = id;
        this.imageURL = imageURL;
        this.title = title;
        this.category = category;
        this.link = link;
    }
}
