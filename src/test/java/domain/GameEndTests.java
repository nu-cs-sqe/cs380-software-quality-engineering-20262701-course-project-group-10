package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class GameEndTests {
    private Board board;
    private Game game;

    private Position square(String name) {
        return new Position(name.charAt(0) - 'a', name.charAt(1) - '1');
    }

    private void setUp(String... pieces) {
        board = new Board();
        for (String specification : pieces) {
            String[] parts = specification.split(" ");
            board.placePiece(new Piece(PieceType.valueOf(parts[1]), Color.valueOf(parts[0])),
                    square(parts[2]));
        }
        game = new Game(board);
    }

    private void move(String from, String to) {
        game.makeMove(square(from), square(to));
    }

    @Test
    public void GE02NewStatusReadsDoNotTouchTheBoard() {
        Board mock = EasyMock.createStrictMock(Board.class);
        EasyMock.replay(mock);
        Game fresh = new Game(mock);
        assertSame(GameStatus.IN_PROGRESS, fresh.getStatus());
        assertSame(GameStatus.IN_PROGRESS, fresh.getStatus());
        EasyMock.verify(mock);
    }

    @Test
    public void GE03NewWinnerReadsDoNotTouchTheBoard() {
        Board mock = EasyMock.createStrictMock(Board.class);
        EasyMock.replay(mock);
        Game fresh = new Game(mock);
        assertTrue(fresh.getWinner().isEmpty());
        assertTrue(fresh.getWinner().isEmpty());
        EasyMock.verify(mock);
    }

    @Test
    public void GE04NewGameIsNotOverAndDoesNotReadTheBoard() {
        Board mock = EasyMock.createStrictMock(Board.class);
        EasyMock.replay(mock);
        Game fresh = new Game(mock);
        assertFalse(fresh.isGameOver());
        assertFalse(fresh.isGameOver());
        EasyMock.verify(mock);
    }

    @Test
    public void GE05WhiteCheckQueryDoesNotMovePieces() {
        setUp("WHITE KING e1", "BLACK KING a8", "BLACK ROOK e8");
        Board before = board.copy();
        assertTrue(game.isCurrentPlayerInCheck());
        assertTrue(game.isCurrentPlayerInCheck());
        MoveRuleAssertions.assertSameSquares(before, board);
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }
}
