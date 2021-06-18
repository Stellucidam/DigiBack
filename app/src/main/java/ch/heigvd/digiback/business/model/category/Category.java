package ch.heigvd.digiback.business.model.category;

import lombok.Getter;

@Getter
public class Category {
    private final int id;
    private final String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
