package domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class GameStatusTests {
    @Test
    public void GE01StatusValuesMatchTheSharedDesign() {
        assertArrayEquals(new GameStatus[] {
            GameStatus.IN_PROGRESS, GameStatus.CHECKMATE, GameStatus.STALEMATE,
            GameStatus.RESIGNED, GameStatus.DRAW_AGREED
        }, GameStatus.values());
    }
}
