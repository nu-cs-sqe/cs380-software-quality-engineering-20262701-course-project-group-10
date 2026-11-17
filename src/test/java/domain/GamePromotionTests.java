package domain;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

/** Tests pawn promotion and its boundary conditions. */
public class GamePromotionTests {

    @Test
    public void PR01WhitePawnRequiresPromotionOnLastRank() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 6)); // e7

        Game game = new Game(board);

        // White pawn moves from e7 to e8.
        boolean result = game.requiresPromotion(
                new Position(4, 6),
                new Position(4, 7));

        assertTrue(result);

        // Checking promotion must not move the pawn.
        assertSame(whitePawn, board.getPiece(new Position(4, 6)));

        // Checking promotion must not change the turn.
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    public void PR02BlackPawnRequiresPromotionOnLastRank() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(blackPawn, new Position(4, 1)); // e2

        Game game = new Game(board);

        // White passes the turn.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black pawn moves from e2 to e1.
        boolean result = game.requiresPromotion(
                new Position(4, 1),
                new Position(4, 0));

        assertTrue(result);

        // Checking promotion must not move the pawn.
        assertSame(blackPawn, board.getPiece(new Position(4, 1)));

        // Checking promotion must not change the turn.
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void PR03NormalPawnMoveDoesNotRequirePromotion() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 1)); // e2

        Game game = new Game(board);

        // White pawn moves from e2 to e3.
        boolean result = game.requiresPromotion(
                new Position(4, 1),
                new Position(4, 2));

        assertFalse(result);

        // Checking must not change the board.
        assertSame(whitePawn, board.getPiece(new Position(4, 1)));
        assertNull(board.getPiece(new Position(4, 2)));

        // Checking must not change the turn.
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    public void PR05WhitePawnPromotesToQueen() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 6)); // e7

        Game game = new Game(board);

        // White pawn moves from e7 to e8 and promotes to Queen.
        game.makeMove(
                new Position(4, 6),
                new Position(4, 7),
                PieceType.QUEEN);

        // Original square must be empty.
        assertNull(board.getPiece(new Position(4, 6)));

        // Destination must contain a White Queen.
        Piece promoted = board.getPiece(new Position(4, 7));

        assertNotNull(promoted);
        assertSame(PieceType.QUEEN, promoted.getType());
        assertSame(Color.WHITE, promoted.getColor());

        // Promotion must replace the pawn with a new piece.
        assertNotSame(whitePawn, promoted);

        // Turn switches to Black.
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void PR06BlackPawnPromotesToQueen() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(blackPawn, new Position(4, 1)); // e2

        Game game = new Game(board);

        // White moves first: a1 -> a2.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black pawn promotes: e2 -> e1.
        game.makeMove(
                new Position(4, 1),
                new Position(4, 0),
                PieceType.QUEEN);

        // Original square must be empty.
        assertNull(board.getPiece(new Position(4, 1)));

        // Destination must contain a Black Queen.
        Piece promoted = board.getPiece(new Position(4, 0));

        assertNotNull(promoted);
        assertSame(PieceType.QUEEN, promoted.getType());
        assertSame(Color.BLACK, promoted.getColor());

        // Promotion creates a new piece.
        assertNotSame(blackPawn, promoted);

        // Turn switches to White.
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }
}