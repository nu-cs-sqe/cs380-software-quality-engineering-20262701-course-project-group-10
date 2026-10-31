package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    // G02: null pointer boundary.
    @Test
    public void nullBoardIsRejected() {
        assertThrows(NullPointerException.class, () -> new Game(null));
    }

    // G03: zero startNewGame() calls; White is to move from construction.
    @Test
    public void whiteMovesFirstBeforeStartNewGame() {
        Board board = EasyMock.createMock(Board.class);
        EasyMock.replay(board);

        Game game = new Game(board);

        assertSame(Color.WHITE, game.getCurrentPlayer());
        assertSame(Color.WHITE, game.getCurrentPlayer());
        EasyMock.verify(board);
    }

    // G04: one startNewGame() call sets up the board exactly once.
    @Test
    public void startNewGameSetsUpTheBoardOnce() {
        Board board = EasyMock.createMock(Board.class);
        board.setUpStartingPosition();
        EasyMock.expectLastCall().once();
        EasyMock.replay(board);

        Game game = new Game(board);
        game.startNewGame();

        assertSame(Color.WHITE, game.getCurrentPlayer());
        EasyMock.verify(board);
    }

    // G05: more than one startNewGame() call sets up the board each time.
    @Test
    public void startNewGameTwiceSetsUpTheBoardTwice() {
        Board board = EasyMock.createMock(Board.class);
        board.setUpStartingPosition();
        EasyMock.expectLastCall().times(2);
        EasyMock.replay(board);

        Game game = new Game(board);
        game.startNewGame();
        game.startNewGame();

        assertSame(Color.WHITE, game.getCurrentPlayer());
        EasyMock.verify(board);
    }
}
