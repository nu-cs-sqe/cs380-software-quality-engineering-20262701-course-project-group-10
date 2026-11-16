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

    @Test
    public void GE06CheckQueryFollowsTheCurrentPlayer() {
        setUp();
        game.startNewGame();
        assertFalse(game.isCurrentPlayerInCheck());
        move("e2", "e4");
        Board before = board.copy();
        assertFalse(game.isCurrentPlayerInCheck());
        assertSame(Color.BLACK, game.getCurrentPlayer());
        MoveRuleAssertions.assertSameSquares(before, board);
    }

    @Test
    public void GE07EmptyBoardHasNoLegalMove() {
        setUp();
        Board before = board.copy();
        assertFalse(game.hasAnyLegalMove());
        MoveRuleAssertions.assertSameSquares(before, board);
    }

    @Test
    public void GE08OneKingHasLegalRepliesWithoutMoving() {
        setUp("WHITE KING a1", "BLACK KING h8");
        Board before = board.copy();
        assertTrue(game.hasAnyLegalMove());
        MoveRuleAssertions.assertSameSquares(before, board);
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    private boolean[] rights() {
        return new boolean[] {game.canCastleKingSide(Color.WHITE),
            game.canCastleQueenSide(Color.WHITE), game.canCastleKingSide(Color.BLACK),
            game.canCastleQueenSide(Color.BLACK)};
    }

    private void assertSearch(boolean expected) {
        Board before = board.copy();
        Color player = game.getCurrentPlayer();
        GameStatus status = game.getStatus();
        java.util.Optional<Color> winner = game.getWinner();
        boolean[] beforeRights = rights();
        assertEquals(expected, game.hasAnyLegalMove());
        assertEquals(expected, game.hasAnyLegalMove());
        MoveRuleAssertions.assertSameSquares(before, board);
        assertSame(player, game.getCurrentPlayer());
        assertSame(status, game.getStatus());
        assertEquals(winner, game.getWinner());
        org.junit.jupiter.api.Assertions.assertArrayEquals(beforeRights, rights());
    }

    @Test
    public void GE09OpponentPiecesDoNotCountAsReplies() {
        setUp("BLACK KING h8");
        assertSearch(false);
    }

    @Test
    public void GE10StalemateHasNoLegalReply() {
        setUp("WHITE KING a1", "BLACK KING c3", "BLACK QUEEN b3");
        assertFalse(game.isCurrentPlayerInCheck());
        assertSearch(false);
    }

    @Test
    public void GE11CheckmateHasNoLegalReply() {
        setUp("WHITE KING a1", "BLACK KING c3", "BLACK QUEEN b2");
        assertTrue(game.isCurrentPlayerInCheck());
        assertSearch(false);
    }

    @Test
    public void GE12OneSafeKingEscapeIsEnough() {
        setUp("WHITE KING a1", "BLACK KING c3", "BLACK ROOK a3");
        assertTrue(game.isCurrentPlayerInCheck());
        assertSearch(true);
    }

    @Test
    public void GE13AnotherPieceCanBlockForATrappedKing() {
        setUp("WHITE KING a1", "WHITE BISHOP b3", "BLACK KING c2", "BLACK ROOK a8");
        assertTrue(game.isCurrentPlayerInCheck());
        assertSearch(true);
    }

    @Test
    public void GE14AnotherPieceCanCaptureForATrappedKing() {
        setUp("WHITE KING a1", "WHITE ROOK h8", "BLACK KING c2", "BLACK ROOK a8");
        assertTrue(game.isCurrentPlayerInCheck());
        assertSearch(true);
    }

    @Test
    public void GE15AnApparentCaptureCannotExposeTheKing() {
        setUp("WHITE KING a1", "WHITE ROOK b1", "BLACK KING c3",
                "BLACK QUEEN b2", "BLACK ROOK h1");
        assertTrue(game.isCurrentPlayerInCheck());
        assertSearch(false);
    }

    @Test
    public void GE16RepeatedOpeningSearchLeavesEveryStateValueUnchanged() {
        setUp();
        game.startNewGame();
        assertSearch(true);
    }

    @Test
    public void GE17SearchIncludesTheLastSourceSquare() {
        setUp("WHITE KING h8", "BLACK KING a1");
        assertSearch(true);
    }

    @Test
    public void GE18SearchIncludesRankZeroOnTheLastFile() {
        setUp("WHITE KING h1", "BLACK KING a8");
        assertSearch(true);
    }

    @Test
    public void GE20SearchingDoesNotConsumeTheRightToCastle() {
        setUp("WHITE KING e1", "WHITE ROOK h1", "BLACK KING a8");
        assertSearch(true);
        move("e1", "g1");
        assertSame(PieceType.KING, board.getPiece(square("g1")).getType());
        assertSame(PieceType.ROOK, board.getPiece(square("f1")).getType());
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void GE21SearchingDoesNotRestoreLostCastlingRights() {
        setUp("WHITE KING e1", "WHITE ROOK h1", "BLACK KING a8");
        move("e1", "f1");
        move("a8", "a7");
        assertFalse(game.canCastleKingSide(Color.WHITE));
        assertSearch(true);
    }
}
