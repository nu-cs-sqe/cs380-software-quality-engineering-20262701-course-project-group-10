package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;
import org.junit.jupiter.api.Test;

public class MessagesTests {
    @Test
    public void M01EnglishTitleComesFromTheBundle() {
        assertEquals("Chess", new Messages(Locale.ENGLISH).get("app.title"));
    }
}
