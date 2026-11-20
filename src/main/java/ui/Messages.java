package ui;

import domain.Color;
import domain.GameStatus;
import domain.MoveError;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeSet;

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

    public String turnText(Color player, boolean inCheck) {
        String turn = get("turn." + Objects.requireNonNull(player, "player").name());
        return inCheck ? new MessageFormat(get("turn.check"), locale).format(new Object[] {turn})
                : turn;
    }

    public String errorText(MoveError error) {
        return get("error." + Objects.requireNonNull(error, "error").name());
    }

    public String resultText(GameStatus status, Optional<Color> winner) {
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(winner, "winner");
        String pattern = get("result." + status.name());
        if (status == GameStatus.CHECKMATE || status == GameStatus.RESIGNED) {
            Color color = winner.orElseThrow(
                    () -> new IllegalArgumentException("A winning result requires a winner"));
            return new MessageFormat(pattern, locale)
                    .format(new Object[] {get("color." + color.name())});
        }
        return pattern;
    }

    public static Map<Locale, String> languageOptions() {
        ResourceBundle languages = ResourceBundle.getBundle("languages", Locale.ROOT);
        Map<Locale, String> options = new LinkedHashMap<>();
        for (String tag : new TreeSet<>(languages.keySet())) {
            options.put(Locale.forLanguageTag(tag), languages.getString(tag));
        }
        return Collections.unmodifiableMap(options);
    }
}
