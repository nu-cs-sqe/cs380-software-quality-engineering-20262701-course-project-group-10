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

    @Test
    public void M10ErrorNoPieceIsTranslated() {
        assertEquals("There is no piece on that square.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.NO_PIECE));
        assertEquals("No hay ninguna pieza en esa casilla.",
                new Messages(new Locale("es")).errorText(domain.MoveError.NO_PIECE));
    }

    @Test
    public void M10ErrorNotYourTurnIsTranslated() {
        assertEquals("That is not your piece.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.NOT_YOUR_TURN));
        assertEquals("Esa pieza no es tuya.",
                new Messages(new Locale("es")).errorText(domain.MoveError.NOT_YOUR_TURN));
    }

    @Test
    public void M10ErrorIllegalMoveIsTranslated() {
        assertEquals("That piece cannot move there.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.ILLEGAL_MOVE));
        assertEquals("Esa pieza no puede moverse allí.",
                new Messages(new Locale("es")).errorText(domain.MoveError.ILLEGAL_MOVE));
    }

    @Test
    public void M10ErrorOwnPieceIsTranslated() {
        assertEquals("You cannot capture your own piece.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.OWN_PIECE));
        assertEquals("No puedes capturar tu propia pieza.",
                new Messages(new Locale("es")).errorText(domain.MoveError.OWN_PIECE));
    }

    @Test
    public void M10ErrorCaptureKingIsTranslated() {
        assertEquals("You cannot capture a king.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.CAPTURE_KING));
        assertEquals("No puedes capturar un rey.",
                new Messages(new Locale("es")).errorText(domain.MoveError.CAPTURE_KING));
    }

    @Test
    public void M10ErrorKingInCheckIsTranslated() {
        assertEquals("That move would leave your king in check.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.KING_IN_CHECK));
        assertEquals("Ese movimiento dejaría a tu rey en jaque.",
                new Messages(new Locale("es")).errorText(domain.MoveError.KING_IN_CHECK));
    }

    @Test
    public void M10ErrorNoCastlingRightIsTranslated() {
        assertEquals("You can no longer castle on that side.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.NO_CASTLING_RIGHT));
        assertEquals("Ya no puedes enrocar en ese lado.",
                new Messages(new Locale("es")).errorText(domain.MoveError.NO_CASTLING_RIGHT));
    }

    @Test
    public void M10ErrorNoCastlingRookIsTranslated() {
        assertEquals("There is no rook to castle with.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.NO_CASTLING_ROOK));
        assertEquals("No hay una torre con la que enrocar.",
                new Messages(new Locale("es")).errorText(domain.MoveError.NO_CASTLING_ROOK));
    }

    @Test
    public void M10ErrorCastlingBlockedIsTranslated() {
        assertEquals("Pieces are in the way of castling.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.CASTLING_BLOCKED));
        assertEquals("Hay piezas que impiden el enroque.",
                new Messages(new Locale("es")).errorText(domain.MoveError.CASTLING_BLOCKED));
    }

    @Test
    public void M10ErrorCastlingOutOfCheckIsTranslated() {
        assertEquals("You cannot castle out of check.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.CASTLING_OUT_OF_CHECK));
        assertEquals("No puedes enrocar estando en jaque.",
                new Messages(new Locale("es")).errorText(domain.MoveError.CASTLING_OUT_OF_CHECK));
    }

    @Test
    public void M10ErrorCastlingThroughAttackIsTranslated() {
        assertEquals("Your king cannot castle through an attacked square.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.CASTLING_THROUGH_ATTACK));
        assertEquals("Tu rey no puede enrocar pasando por una casilla atacada.",
                new Messages(new Locale("es")).errorText(domain.MoveError.CASTLING_THROUGH_ATTACK));
    }

    @Test
    public void M10ErrorPromotionRequiredIsTranslated() {
        assertEquals("A promotion choice is required.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.PROMOTION_REQUIRED));
        assertEquals("Debes elegir una pieza para la promoción.",
                new Messages(new Locale("es")).errorText(domain.MoveError.PROMOTION_REQUIRED));
    }

    @Test
    public void M10ErrorNotAPawnIsTranslated() {
        assertEquals("Only pawns can promote.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.NOT_A_PAWN));
        assertEquals("Solo los peones pueden promocionar.",
                new Messages(new Locale("es")).errorText(domain.MoveError.NOT_A_PAWN));
    }

    @Test
    public void M10ErrorNotLastRankIsTranslated() {
        assertEquals("The pawn has not reached the last rank.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.NOT_LAST_RANK));
        assertEquals("El peón no ha llegado a la última fila.",
                new Messages(new Locale("es")).errorText(domain.MoveError.NOT_LAST_RANK));
    }

    @Test
    public void M10ErrorInvalidPromotionIsTranslated() {
        assertEquals("Invalid promotion type.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.INVALID_PROMOTION));
        assertEquals("La pieza de promoción no es válida.",
                new Messages(new Locale("es")).errorText(domain.MoveError.INVALID_PROMOTION));
    }

    @Test
    public void M10ErrorGameOverIsTranslated() {
        assertEquals("The game is over.",
                new Messages(Locale.ENGLISH).errorText(domain.MoveError.GAME_OVER));
        assertEquals("La partida ha terminado.",
                new Messages(new Locale("es")).errorText(domain.MoveError.GAME_OVER));
    }

    @Test
    public void M11NullErrorIsRejected() {
        assertThrows(NullPointerException.class,
                () -> new Messages(Locale.ENGLISH).errorText(null));
    }

    @Test
    public void M12ResultInProgressIsTranslated() {
        assertEquals("Game in progress.",
                new Messages(Locale.ENGLISH).resultText(domain.GameStatus.IN_PROGRESS,
                        java.util.Optional.empty()));
        assertEquals("Partida en curso.",
                new Messages(new Locale("es")).resultText(domain.GameStatus.IN_PROGRESS,
                        java.util.Optional.empty()));
    }

    @Test
    public void M12ResultCheckmateWhiteIsTranslated() {
        assertEquals("White wins by checkmate.",
                new Messages(Locale.ENGLISH).resultText(domain.GameStatus.CHECKMATE,
                        java.util.Optional.of(domain.Color.WHITE)));
        assertEquals("Blancas ganan por jaque mate.",
                new Messages(new Locale("es")).resultText(domain.GameStatus.CHECKMATE,
                        java.util.Optional.of(domain.Color.WHITE)));
    }

    @Test
    public void M12ResultCheckmateBlackIsTranslated() {
        assertEquals("Black wins by checkmate.",
                new Messages(Locale.ENGLISH).resultText(domain.GameStatus.CHECKMATE,
                        java.util.Optional.of(domain.Color.BLACK)));
        assertEquals("Negras ganan por jaque mate.",
                new Messages(new Locale("es")).resultText(domain.GameStatus.CHECKMATE,
                        java.util.Optional.of(domain.Color.BLACK)));
    }

    @Test
    public void M12ResultStalemateIsTranslated() {
        assertEquals("Draw by stalemate.",
                new Messages(Locale.ENGLISH).resultText(domain.GameStatus.STALEMATE,
                        java.util.Optional.empty()));
        assertEquals("Tablas por ahogado.",
                new Messages(new Locale("es")).resultText(domain.GameStatus.STALEMATE,
                        java.util.Optional.empty()));
    }

    @Test
    public void M12ResultResignedWhiteIsTranslated() {
        assertEquals("White wins by resignation.",
                new Messages(Locale.ENGLISH).resultText(domain.GameStatus.RESIGNED,
                        java.util.Optional.of(domain.Color.WHITE)));
        assertEquals("Blancas ganan por abandono.",
                new Messages(new Locale("es")).resultText(domain.GameStatus.RESIGNED,
                        java.util.Optional.of(domain.Color.WHITE)));
    }

    @Test
    public void M12ResultResignedBlackIsTranslated() {
        assertEquals("Black wins by resignation.",
                new Messages(Locale.ENGLISH).resultText(domain.GameStatus.RESIGNED,
                        java.util.Optional.of(domain.Color.BLACK)));
        assertEquals("Negras ganan por abandono.",
                new Messages(new Locale("es")).resultText(domain.GameStatus.RESIGNED,
                        java.util.Optional.of(domain.Color.BLACK)));
    }

    @Test
    public void M12ResultDrawAgreedIsTranslated() {
        assertEquals("Draw by agreement.",
                new Messages(Locale.ENGLISH).resultText(domain.GameStatus.DRAW_AGREED,
                        java.util.Optional.empty()));
        assertEquals("Tablas por acuerdo.",
                new Messages(new Locale("es")).resultText(domain.GameStatus.DRAW_AGREED,
                        java.util.Optional.empty()));
    }

    @Test
    public void M13InvalidResultInputsAreRejected() {
        Messages messages = new Messages(Locale.ENGLISH);
        assertThrows(NullPointerException.class,
                () -> messages.resultText(null, java.util.Optional.empty()));
        assertThrows(NullPointerException.class,
                () -> messages.resultText(domain.GameStatus.STALEMATE, null));
        for (domain.GameStatus status : new domain.GameStatus[] {
                domain.GameStatus.CHECKMATE, domain.GameStatus.RESIGNED}) {
            assertThrows(IllegalArgumentException.class,
                    () -> messages.resultText(status, java.util.Optional.empty()));
        }
    }

    @Test
    public void M14DrawAndProgressTextDoNotUseAWinner() {
        Messages messages = new Messages(Locale.ENGLISH);
        for (domain.GameStatus status : new domain.GameStatus[] {
                domain.GameStatus.IN_PROGRESS, domain.GameStatus.STALEMATE,
                domain.GameStatus.DRAW_AGREED}) {
            assertEquals(messages.resultText(status, java.util.Optional.empty()),
                    messages.resultText(status, java.util.Optional.of(domain.Color.WHITE)));
        }
    }

    @Test
    public void M16LanguageOptionsComeFromAnImmutableRegistry() {
        java.util.Map<Locale, String> languages = Messages.languageOptions();
        java.util.List<String> tags = new java.util.ArrayList<>();
        for (Locale locale : languages.keySet()) {
            tags.add(locale.toLanguageTag());
        }
        java.util.List<String> sorted = new java.util.ArrayList<>(tags);
        java.util.Collections.sort(sorted);
        assertEquals(sorted, tags);
        assertEquals("English", languages.get(Locale.ENGLISH));
        assertEquals("Español", languages.get(new Locale("es")));
        assertThrows(UnsupportedOperationException.class,
                () -> languages.put(Locale.FRENCH, "Français"));
    }

    @Test
    public void M15RawBundlesHaveExactlyTheSameNonemptyKeys() throws java.io.IOException {
        java.util.Set<String> expected = null;
        for (String file : bundleFiles()) {
            java.util.Properties bundle = new java.util.Properties();
            try (java.io.Reader reader = new java.io.InputStreamReader(
                    java.util.Objects.requireNonNull(getClass().getResourceAsStream("/" + file)),
                    java.nio.charset.StandardCharsets.UTF_8)) {
                bundle.load(reader);
            }
            if (expected == null) {
                expected = bundle.stringPropertyNames();
            } else {
                assertEquals(expected, bundle.stringPropertyNames(), file);
            }
            for (String key : bundle.stringPropertyNames()) {
                org.junit.jupiter.api.Assertions.assertFalse(bundle.getProperty(key).isBlank(),
                        key);
            }
        }
    }

    @Test
    public void M17ChangingMessagesDoesNotChangeTheGame() {
        domain.Board board = new domain.Board();
        domain.Game game = new domain.Game(board);
        game.startNewGame();
        domain.Piece pawn = board.getPiece(new domain.Position(4, 1));
        Messages english = new Messages(Locale.ENGLISH);
        Messages spanish = new Messages(new Locale("es"));
        assertEquals("White to move", english.turnText(game.getCurrentPlayer(), false));
        assertEquals("Juegan las blancas", spanish.turnText(game.getCurrentPlayer(), false));
        org.junit.jupiter.api.Assertions.assertSame(pawn,
                board.getPiece(new domain.Position(4, 1)));
        org.junit.jupiter.api.Assertions.assertSame(domain.Color.WHITE, game.getCurrentPlayer());
        org.junit.jupiter.api.Assertions.assertSame(domain.GameStatus.IN_PROGRESS,
                game.getStatus());
    }

    @Test
    public void M18PromotionLabelsAreTranslatedRatherThanEnumNames() {
        assertEquals("King",
                new Messages(Locale.ENGLISH).get("piece.KING"));
        assertEquals("Rey",
                new Messages(new Locale("es")).get("piece.KING"));
        assertEquals("Queen",
                new Messages(Locale.ENGLISH).get("piece.QUEEN"));
        assertEquals("Dama",
                new Messages(new Locale("es")).get("piece.QUEEN"));
        assertEquals("Rook",
                new Messages(Locale.ENGLISH).get("piece.ROOK"));
        assertEquals("Torre",
                new Messages(new Locale("es")).get("piece.ROOK"));
        assertEquals("Bishop",
                new Messages(Locale.ENGLISH).get("piece.BISHOP"));
        assertEquals("Alfil",
                new Messages(new Locale("es")).get("piece.BISHOP"));
        assertEquals("Knight",
                new Messages(Locale.ENGLISH).get("piece.KNIGHT"));
        assertEquals("Caballo",
                new Messages(new Locale("es")).get("piece.KNIGHT"));
        assertEquals("Pawn",
                new Messages(Locale.ENGLISH).get("piece.PAWN"));
        assertEquals("Peón",
                new Messages(new Locale("es")).get("piece.PAWN"));
    }

    @Test
    public void M15RawBundlesDoNotHideDuplicateKeys() throws java.io.IOException {
        for (String file : bundleFiles()) {
            try (java.io.InputStream input = java.util.Objects.requireNonNull(
                    getClass().getResourceAsStream("/" + file))) {
                String text = new String(input.readAllBytes(),
                        java.nio.charset.StandardCharsets.UTF_8);
                java.util.Set<String> keys = new java.util.HashSet<>();
                for (String line : text.split("\n")) {
                    if (!line.isBlank()) {
                        String key = line.split("=", 2)[0];
                        org.junit.jupiter.api.Assertions.assertTrue(keys.add(key), key);
                    }
                }
            }
        }
    }

    private java.util.List<String> bundleFiles() {
        java.util.List<String> files = new java.util.ArrayList<>();
        files.add("messages.properties");
        for (Locale locale : Messages.languageOptions().keySet()) {
            files.add("messages_" + locale.toLanguageTag().replace('-', '_') + ".properties");
        }
        return files;
    }

    @Test
    public void M10EveryRegisteredLanguageIncludesEveryErrorCode() {
        for (Locale locale : Messages.languageOptions().keySet()) {
            Messages messages = new Messages(locale);
            for (domain.MoveError error : domain.MoveError.values()) {
                org.junit.jupiter.api.Assertions.assertFalse(messages.errorText(error).isBlank());
            }
        }
    }
}
