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

    @Test
    public void M02SpanishTitleComesFromItsOwnBundle() {
        assertEquals("Ajedrez", new Messages(new Locale("es")).get("app.title"));
    }

    @Test
    public void M03NullLocaleIsRejected() {
        assertThrows(NullPointerException.class, () -> new Messages(null));
    }

    @Test
    public void M04MissingAndNullKeysAreRejected() {
        Messages messages = new Messages(Locale.ENGLISH);
        assertThrows(NullPointerException.class, () -> messages.get(null));
        assertThrows(java.util.MissingResourceException.class, () -> messages.get(""));
        assertThrows(java.util.MissingResourceException.class, () -> messages.get("missing.key"));
    }

    @Test
    public void M05CountryLocalesUseTheirLanguageBundles() {
        assertEquals("Chess", new Messages(Locale.US).get("app.title"));
        assertEquals("Chess", new Messages(Locale.UK).get("app.title"));
        assertEquals("Ajedrez", new Messages(new Locale("es", "MX")).get("app.title"));
        assertEquals("Ajedrez", new Messages(new Locale("es", "ES")).get("app.title"));
    }

    @Test
    public void M06FallbackDoesNotDependOnTheMachineLanguage() {
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(new Locale("es"));
            assertEquals("Chess", new Messages(Locale.ROOT).get("app.title"));
            assertEquals("Chess", new Messages(Locale.JAPANESE).get("app.title"));
        } finally {
            Locale.setDefault(previous);
        }
    }

    @Test
    public void M07AnotherBundleWorksWithoutChangingProductionJava() {
        assertEquals("Échecs", new Messages(Locale.FRENCH).get("app.title"));
    }
}
