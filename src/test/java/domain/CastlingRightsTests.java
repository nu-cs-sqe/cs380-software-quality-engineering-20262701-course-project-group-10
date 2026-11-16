package domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CastlingRightsTests {
    // G30: a new game starts with every castling right.
    @Test
    public void G30NewGameHasAllCastlingRights() {
        Game game = new Game(new Board());

        assertTrue(game.canCastleKingSide(Color.WHITE));
        assertTrue(game.canCastleQueenSide(Color.WHITE));
        assertTrue(game.canCastleKingSide(Color.BLACK));
        assertTrue(game.canCastleQueenSide(Color.BLACK));
    }
}
