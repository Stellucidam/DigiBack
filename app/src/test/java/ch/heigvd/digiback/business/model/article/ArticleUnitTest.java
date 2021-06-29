package ch.heigvd.digiback.business.model.article;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArticleUnitTest {
    private Article article;
    private final int id = 1;
    private final String imageURL = "imageURL",
            title = "Title",
            link = "Link";
    private final int category = 2;

    @Before
    public void setUp() {
        article = new Article(id, imageURL, title, category, link);
    }

    @Test
    public void imageURLShouldBeCorrect() {
        assertEquals(imageURL, article.getImageURL());
    }

    @Test
    public void idShouldBeCorrect() {
        assertEquals(id, article.getId());
    }

    @Test
    public void titleShouldBeCorrect() {
        assertEquals(title, article.getTitle());
    }

    @Test
    public void linkShouldBeCorrect() {
        assertEquals(link, article.getLink());
    }

    @Test
    public void categoryShouldBeCorrect() {
        assertEquals(category, article.getCategory());
    }
}
