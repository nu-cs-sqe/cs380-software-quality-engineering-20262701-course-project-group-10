package domain;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameMakeMoveTests {
    private Board board;
    private Game game;

    @BeforeEach
    public void createGame() {
        board = new Board();
        game = new Game(board);
    }

    // G08: null from.
    @Test
    public void nullFromIsRejected() {
        game.startNewGame();

        assertThrows(NullPointerException.class,
                () -> game.makeMove(null, new Position(4, 3)));

        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G09: null to.
    @Test
    public void nullToIsRejected() {
        game.startNewGame();

        assertThrows(NullPointerException.class,
                () -> game.makeMove(new Position(4, 1), null));

        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G10: empty source square (e3 is empty at the start).
    @Test
    public void movingFromAnEmptySquareIsRejected() {
        game.startNewGame();

        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Position(4, 2), new Position(4, 3)));

        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G11: White to move, Black pawn selected. The move itself is a legal
    // Black pawn move, so only the turn rule can reject it.
    @Test
    public void whiteCannotMoveABlackPiece() {
        game.startNewGame();
        Piece blackPawn = board.getPiece(new Position(4, 6));

        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Position(4, 6), new Position(4, 5)));

        assertSame(blackPawn, board.getPiece(new Position(4, 6)));
        assertTrue(board.isEmpty(new Position(4, 5)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G12: Black to move, White pawn selected (d2 to d4 is legal for White).
    @Test
    public void blackCannotMoveAWhitePiece() {
        game.startNewGame();
        game.makeMove(new Position(4, 1), new Position(4, 3));
        Piece whitePawn = board.getPiece(new Position(3, 1));

        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Position(3, 1), new Position(3, 3)));

        assertSame(whitePawn, board.getPiece(new Position(3, 1)));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G13: a pawn cannot jump three squares.
    @Test
    public void illegalMoveIsRejectedAndNothingChanges() {
        game.startNewGame();
        Piece pawn = board.getPiece(new Position(4, 1));

        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Position(4, 1), new Position(4, 4)));

        assertSame(pawn, board.getPiece(new Position(4, 1)));
        assertTrue(board.isEmpty(new Position(4, 4)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G14: legal White move.
    @Test
    public void legalWhiteMoveMovesPieceAndPassesTurnToBlack() {
        game.startNewGame();
        Piece pawn = board.getPiece(new Position(4, 1));

        game.makeMove(new Position(4, 1), new Position(4, 3));

        assertSame(pawn, board.getPiece(new Position(4, 3)));
        assertTrue(board.isEmpty(new Position(4, 1)));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G15: turns alternate.
    @Test
    public void legalBlackMovePassesTurnBackToWhite() {
        game.startNewGame();
        Piece blackPawn = board.getPiece(new Position(4, 6));

        game.makeMove(new Position(4, 1), new Position(4, 3));
        game.makeMove(new Position(4, 6), new Position(4, 4));

        assertSame(blackPawn, board.getPiece(new Position(4, 4)));
        assertTrue(board.isEmpty(new Position(4, 6)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G16: a rook may not capture its own pawn.
    @Test
    public void capturingOwnPieceIsRejected() {
        Piece rook = new Piece(PieceType.ROOK, Color.WHITE);
        Piece pawn = new Piece(PieceType.PAWN, Color.WHITE);
        board.placePiece(rook, new Position(3, 3));
        board.placePiece(pawn, new Position(3, 5));

        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Position(3, 3), new Position(3, 5)));

        assertSame(rook, board.getPiece(new Position(3, 3)));
        assertSame(pawn, board.getPiece(new Position(3, 5)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G17: a rook captures an opposing pawn.
    @Test
    public void capturingAnOpposingPieceReplacesIt() {
        Piece rook = new Piece(PieceType.ROOK, Color.WHITE);
        board.placePiece(rook, new Position(3, 3));
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), new Position(3, 5));

        game.makeMove(new Position(3, 3), new Position(3, 5));

        assertSame(rook, board.getPiece(new Position(3, 5)));
        assertTrue(board.isEmpty(new Position(3, 3)));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G07: startNewGame after a move restores the opening position and White.
    @Test
    public void startNewGameAfterAMoveResetsBoardAndTurn() {
        game.startNewGame();
        game.makeMove(new Position(4, 1), new Position(4, 3));

        game.startNewGame();

        assertSame(PieceType.PAWN, board.getPiece(new Position(4, 1)).getType());
        assertTrue(board.isEmpty(new Position(4, 3)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }
}