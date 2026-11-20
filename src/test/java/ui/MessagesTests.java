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

    @Test
    public void M08BothPlayersAndCheckStatesAreTranslated() {
        Messages en = new Messages(Locale.ENGLISH);
        final Messages es = new Messages(new Locale("es"));
        assertEquals("White to move", en.turnText(domain.Color.WHITE, false));
        assertEquals("Black to move", en.turnText(domain.Color.BLACK, false));
        assertEquals("White to move — check", en.turnText(domain.Color.WHITE, true));
        assertEquals("Black to move — check", en.turnText(domain.Color.BLACK, true));
        assertEquals("Juegan las blancas", es.turnText(domain.Color.WHITE, false));
        assertEquals("Juegan las negras", es.turnText(domain.Color.BLACK, false));
        assertEquals("Juegan las blancas — jaque", es.turnText(domain.Color.WHITE, true));
        assertEquals("Juegan las negras — jaque", es.turnText(domain.Color.BLACK, true));
    }

    @Test
    public void M09NullPlayerIsRejected() {
        assertThrows(NullPointerException.class,
                () -> new Messages(Locale.ENGLISH).turnText(null, false));
    }
}
