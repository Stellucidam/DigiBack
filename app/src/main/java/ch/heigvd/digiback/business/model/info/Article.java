package ch.heigvd.digiback.business.model.info;

public class Article {
    private final int id;
    private final int imageId;
    private final String title;
    private final ArticleType type;
    private final int readDuration;
    private final String content;

    public Article(int id, int imageId, String title, String content, ArticleType type) {
        this.id = id;
        this.imageId = imageId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.readDuration = 0;
    }

    public Article(int id, int imageId, String title, String content, ArticleType type, int readDuration) {
        this.id = id;
        this.imageId = imageId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.readDuration = readDuration;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
