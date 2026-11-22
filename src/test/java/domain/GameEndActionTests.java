package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class GameEndActionTests {
    private static final Position E2 = new Position(4, 1);
    private static final Position E4 = new Position(4, 3);

    private final Board board = EasyMock.createStrictMock(Board.class);
    private final Game game = new Game(board);

    @Test
    public void GE42WhiteCanResignOnItsTurn() {
        EasyMock.replay(board);

        game.resign(Color.WHITE);

        assertResult(game, GameStatus.RESIGNED, Color.BLACK);
        assertSame(Color.WHITE, game.getCurrentPlayer());
        EasyMock.verify(board);
    }

    @Test
    public void GE43BlackCanResignOnItsTurn() {
        Game secondTurn = afterWhiteMove(true, false);
        final Board before = secondTurn.getBoard().copy();

        secondTurn.resign(Color.BLACK);

        assertResult(secondTurn, GameStatus.RESIGNED, Color.WHITE);
        assertSame(Color.BLACK, secondTurn.getCurrentPlayer());
        MoveRuleAssertions.assertSameSquares(before, secondTurn.getBoard());
        EasyMock.verify(secondTurn);
    }

    @Test
    public void GE44OnlyTheCurrentPlayerCanResign() {
        EasyMock.replay(board);

        assertError(MoveError.NOT_YOUR_TURN, () -> game.resign(Color.BLACK));

        assertInProgress(game);
        EasyMock.verify(board);
    }

    @Test
    public void GE45NullCannotResign() {
        EasyMock.replay(board);

        assertError(MoveError.NOT_YOUR_TURN, () -> game.resign(null));

        assertInProgress(game);
        EasyMock.verify(board);
    }

    @Test
    public void GE46AgreedDrawHasNoWinner() {
        EasyMock.replay(board);

        game.agreeDraw();

        assertResult(game, GameStatus.DRAW_AGREED, null);
        assertSame(Color.WHITE, game.getCurrentPlayer());
        EasyMock.verify(board);
    }

    @Test
    public void GE47NormalMoveIsBlockedAfterResignation() {
        EasyMock.replay(board);
        game.resign(Color.WHITE);

        assertError(MoveError.GAME_OVER, () -> game.makeMove(E2, E4));

        assertResult(game, GameStatus.RESIGNED, Color.BLACK);
        assertSame(Color.WHITE, game.getCurrentPlayer());
        EasyMock.verify(board);
    }

    @Test
    public void GE49ResignationIsBlockedAfterDraw() {
        EasyMock.replay(board);
        game.agreeDraw();

        assertError(MoveError.GAME_OVER, () -> game.resign(Color.BLACK));

        assertResult(game, GameStatus.DRAW_AGREED, null);
        EasyMock.verify(board);
    }

    @Test
    public void GE50DrawIsBlockedAfterResignation() {
        EasyMock.replay(board);
        game.resign(Color.WHITE);

        assertError(MoveError.GAME_OVER, game::agreeDraw);

        assertResult(game, GameStatus.RESIGNED, Color.BLACK);
        EasyMock.verify(board);
    }

    @Test
    public void GE51NewGameClearsResignationAndWinner() {
        board.setUpStartingPosition();
        EasyMock.replay(board);
        game.resign(Color.WHITE);
        assertResult(game, GameStatus.RESIGNED, Color.BLACK);

        game.startNewGame();

        assertInProgress(game);
        EasyMock.verify(board);
    }

    @Test
    public void GE52NewGameClearsDraw() {
        board.setUpStartingPosition();
        EasyMock.replay(board);
        game.agreeDraw();
        assertResult(game, GameStatus.DRAW_AGREED, null);

        game.startNewGame();

        assertInProgress(game);
        EasyMock.verify(board);
    }

    private Game afterWhiteMove(boolean hasReply, boolean inCheck) {
        Board startingBoard = new Board();
        startingBoard.setUpStartingPosition();
        Game nextTurn = EasyMock.partialMockBuilder(Game.class)
                .withConstructor(startingBoard)
                .addMockedMethod("hasAnyLegalMove")
                .addMockedMethod("isCurrentPlayerInCheck")
                .createMock();
        EasyMock.expect(nextTurn.hasAnyLegalMove()).andReturn(hasReply);
        if (!hasReply) {
            EasyMock.expect(nextTurn.isCurrentPlayerInCheck()).andReturn(inCheck);
        }
        EasyMock.replay(nextTurn);
        nextTurn.makeMove(E2, E4);
        return nextTurn;
    }

    private void assertError(MoveError error, Executable action) {
        IllegalMoveException exception = assertThrows(IllegalMoveException.class, action);
        assertSame(error, exception.getError());
    }

    private void assertResult(Game finished, GameStatus status, Color winner) {
        assertSame(status, finished.getStatus());
        assertEquals(Optional.ofNullable(winner), finished.getWinner());
        assertTrue(finished.isGameOver());
    }

    private void assertInProgress(Game fresh) {
        assertSame(GameStatus.IN_PROGRESS, fresh.getStatus());
        assertSame(Color.WHITE, fresh.getCurrentPlayer());
        assertTrue(fresh.getWinner().isEmpty());
        assertFalse(fresh.isGameOver());
    }
}
