package domain;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

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
}