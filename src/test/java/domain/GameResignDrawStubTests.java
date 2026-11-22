package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Covers the temporary resign and agreeDraw stubs; #54 replaces both.
public class GameResignDrawStubTests {
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
}
