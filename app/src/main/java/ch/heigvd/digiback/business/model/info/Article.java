package ch.heigvd.digiback.business.model.info;

public class Article {
    private final String title;
    private final ArticleType type;
    private final int readDuration;
    private final String content;

    public Article(String title, String content, ArticleType type, int readDuration) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.readDuration = readDuration;
    }

}
