package ch.heigvd.digiback.business.model.article;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

public class Article {
    private final int id;
    private final String imageURL;
    private MutableLiveData<Bitmap> imageBM = new MutableLiveData<>();
    private final String title;
    private final String link;
    private final int category;
    private MutableLiveData<String> categoyName = new MutableLiveData<>();

    public Article(int id, String imageURL, String title, int category, String link) {
        this.id = id;
        this.imageURL = imageURL;
        this.title = title;
        this.category = category;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public MutableLiveData<Bitmap> getImageBM() {
        return imageBM;
    }

    public int getCategory() {
        return category;
    }

    public MutableLiveData<String> getCategoyName() {
        return categoyName;
    }

    public String getLink() {
        return link;
    }
}
