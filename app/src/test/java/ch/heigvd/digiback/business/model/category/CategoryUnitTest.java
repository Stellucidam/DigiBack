package ch.heigvd.digiback.business.model.category;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryUnitTest {
    private Category category;
    private final int id = 1;
    private final String name = "Category";

    @Before
    public void setUp() {
        category = new Category(id, name);
    }

    @Test
    public void nameShouldBeCorrect() {
        assertEquals(name, category.getName());
    }

    @Test
    public void idShouldBeCorrect() {
        assertEquals(id, category.getId());
    }
}
