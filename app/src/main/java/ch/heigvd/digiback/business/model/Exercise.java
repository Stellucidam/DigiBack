package ch.heigvd.digiback.business.model;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import lombok.Getter;

@Getter
public class Exercise {
    private Long id;
    private String imageURL;
    private String title;
    private List<Instruction> instructions;

    private MutableLiveData<Bitmap> imageBM = new MutableLiveData<>();

    public Exercise(Long id, String imageURL, String title) {
        this.id = id;
        this.imageURL = imageURL;
        this.title = title;
    }

    public Exercise(Long id, String imageURL, String title, List<Instruction> instructions) {
        this.id = id;
        this.imageURL = imageURL;
        this.title = title;
        this.instructions = instructions;
    }
}
