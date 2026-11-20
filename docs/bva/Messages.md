# Messages

## Contract and catalog analysis

All visible text comes from UTF-8 ResourceBundles, as described in the i18n slides
and the Canvas requirement supplied by the project owner: choose at least two
languages before play, and add new locales without editing existing Java code.
`messages.properties` is the English fallback; `messages_en.properties` and
`messages_es.properties` are complete translations. Region-specific locales use
Java's language fallback; unsupported/root locales use the base bundle, without
consulting the machine's default locale. New languages need a bundle and an entry
in `languages.properties`, not an enum or Java-language switch.

`Messages(Locale)` rejects null. `get(String)` rejects null; empty/unknown keys
throw MissingResourceException. `turnText(Color, boolean)` rejects null color.
`errorText(MoveError)` rejects null error. `resultText(GameStatus, Optional<Color>)`
rejects null status/Optional; CHECKMATE and RESIGNED require a winner; other results
ignore a present winner. `languageOptions()` returns a sorted, unmodifiable map of
locales to native display names from the language registry.

Catalog clues: Cases (every locale, player, error, result, promotion label),
Boolean (check on/off), Pointers (null/real), Strings (empty/unknown/exact keys,
accented Unicode), Collections (equal bundle key sets, nonempty values, language
registry), and Searching (missing keys, locale fallback). Locale switching creates
another Messages object without changing the game. Use MessageFormat placeholders
for composed text so translators can reorder names.

## Step 4: tests

| ID | Methods and state | Expected output | Implemented? |
| --- | --- | --- | --- |
| M01 | construct en; get app.title | Chess | yes |
| M02 | construct es; get app.title | Ajedrez | yes |
| M03 | null locale | NullPointerException | yes |
| M04 | get null, empty, unknown keys | NPE for null; MissingResourceException otherwise | yes |
| M05 | en_US / en_GB / es_MX / es_ES | language bundle selected | yes |
| M06 | root/unsupported locale with Spanish JVM default | English base fallback, global locale restored | yes |
| M07 | test-only fr bundle | French text without production Java changes | yes |
| M08 | both colors, both check states, en/es | exact localized turn/check text | no |
| M09 | null player | NullPointerException | no |
| M10 | every MoveError, en/es | exact corresponding translated error | no |
| M11 | null error | NullPointerException | no |
| M12 | every GameStatus, both winners for decisive results, en/es | exact formatted result | no |
| M13 | null status / null Optional / decisive status without winner | NPE / NPE / IllegalArgumentException | no |
| M14 | draw/in-progress with a present winner | winner ignored; same nondecisive text | no |
| M15 | raw base/en/es bundles | identical keys; no blank/duplicate entries; readable Unicode | no |
| M16 | languageOptions | en/English and es/Español; sorted and unmodifiable | no |
| M17 | two Messages instances for en/es with real game | text changes, game state unchanged | no |
| M18 | all promotion piece names, en/es | localized choices; enum names never shown to players | no |

## Domain error boundary

`IllegalMoveException` extends IllegalArgumentException and retains a non-null
MoveError plus an English diagnostic for logs; the GUI translates the error code,
never the diagnostic. Its tests cover all codes, getter identity, superclass
compatibility and null rejection. Each Game rejection is checked for its precise
error code while the existing BVA tests retain board/state rejection coverage.
Game-over actions themselves remain with the separate lifecycle task.

## Swing review

Manually review startup language selection; menus/title/turn/check refresh;
localized error and confirmation buttons; promotion labels/cancellation; and
result text. Switching languages must preserve the board, player, selection and
result. View classes remain excluded under the existing project policy.
