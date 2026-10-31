package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertSame;

public class GameTests {
    // G01: non-null pointer; the constructor must not touch the board.
    @Test
    public void newGameKeepsTheSameBoard() {
        Board board = EasyMock.createMock(Board.class);
        EasyMock.replay(board);

        Game game = new Game(board);

        assertSame(board, game.getBoard());
        assertSame(board, game.getBoard());
        EasyMock.verify(board);
    }
}
