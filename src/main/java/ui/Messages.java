package ui;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/** Loads localized chess text without changing game state. */
public class Messages {
    private final ResourceBundle bundle;
    private final Locale locale;

    public Messages(Locale locale) {
        this.locale = Objects.requireNonNull(locale, "locale");
        bundle = ResourceBundle.getBundle("messages", locale,
                ResourceBundle.Control.getNoFallbackControl(
                        ResourceBundle.Control.FORMAT_PROPERTIES));
    }

    public String get(String key) {
        return bundle.getString(Objects.requireNonNull(key, "key"));
    }
}
