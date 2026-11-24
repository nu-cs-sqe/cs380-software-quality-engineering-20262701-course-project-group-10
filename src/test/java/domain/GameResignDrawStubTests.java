package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// GR01 to GR03 cover the temporary resign and agreeDraw stubs, which #54 replaces.
// GR04 tests startNewGame and stays after #54 merges.
public class GameResignDrawStubTests {
    private static final int FILE_E = 4;
    private static final int RANK_2 = 1;
    private static final int RANK_4 = 3;
    private static final Position E2 = new Position(FILE_E, RANK_2);
    private static final Position E4 = new Position(FILE_E, RANK_4);

    private Game game;

    @BeforeEach
    public void startGame() {
        game = new Game(new Board());
        game.startNewGame();
    }

    // GR01: White resigns, so Black wins.
    @Test
    public void GR01WhiteResignsAndBlackWins() {
        game.resign(Color.WHITE);

        assertEquals(GameStatus.RESIGNED, game.getStatus());
        assertEquals(Optional.of(Color.BLACK), game.getWinner());
        assertTrue(game.isGameOver());
    }

    // GR02: Black resigns on its turn, so White wins.
    @Test
    public void GR02BlackResignsAndWhiteWins() {
        game.makeMove(E2, E4);

        game.resign(Color.BLACK);

        assertEquals(GameStatus.RESIGNED, game.getStatus());
        assertEquals(Optional.of(Color.WHITE), game.getWinner());
    }

    // GR03: an agreed draw ends the game with no winner.
    @Test
    public void GR03AgreedDrawEndsGameWithNoWinner() {
        game.agreeDraw();

        assertEquals(GameStatus.DRAW_AGREED, game.getStatus());
        assertEquals(Optional.empty(), game.getWinner());
        assertTrue(game.isGameOver());
    }

    // GR04: New Game after a finished game starts a fresh, unfinished game.
    @Test
    public void GR04NewGameAfterResignationIsInProgress() {
        game.resign(Color.WHITE);

        game.startNewGame();

        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertEquals(Optional.empty(), game.getWinner());
        assertFalse(game.isGameOver());
        assertEquals(Color.WHITE, game.getCurrentPlayer());
    }
}
