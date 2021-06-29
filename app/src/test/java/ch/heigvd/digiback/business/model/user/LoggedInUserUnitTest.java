package ch.heigvd.digiback.business.model.user;

import org.junit.Before;
import org.junit.Test;

import ch.heigvd.digiback.ui.data.model.LoggedInUser;

import static org.junit.Assert.assertEquals;

public class LoggedInUserUnitTest {
    private LoggedInUser loggedInUser;
    private final String name = "Users name",
            id = "Id";

    @Before
    public void setUp() {
        loggedInUser = new LoggedInUser(id, name);
    }

    @Test
    public void nameShouldBeCorrect() {
        assertEquals(name, loggedInUser.getDisplayName());
    }

    @Test
    public void idShouldBeCorrect() {
        assertEquals(id, loggedInUser.getUserId());
    }
}
