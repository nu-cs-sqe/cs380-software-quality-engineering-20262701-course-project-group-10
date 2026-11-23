package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import domain.Board;
import domain.Color;
import domain.Game;
import domain.Piece;
import domain.PieceType;
import domain.Position;
import org.junit.jupiter.api.Test;

/** Integration tests for multiple turns using real game components. */
public class MultipleTurnsIntegrationTests {

    @Test
    public void IT01NormalMovesAndCaptures() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // 1. White: e2 -> e4.
        game.makeMove(new Position(4, 1), new Position(4, 3));

        Piece whitePawn = board.getPiece(new Position(4, 3));

        assertEquals(PieceType.PAWN, whitePawn.getType());
        assertEquals(Color.WHITE, whitePawn.getColor());
        assertNull(board.getPiece(new Position(4, 1)));
        assertSame(Color.BLACK, game.getCurrentPlayer());

        // 2. Black: d7 -> d5.
        game.makeMove(new Position(3, 6), new Position(3, 4));

        Piece blackPawn = board.getPiece(new Position(3, 4));

        assertEquals(PieceType.PAWN, blackPawn.getType());
        assertEquals(Color.BLACK, blackPawn.getColor());
        assertSame(Color.WHITE, game.getCurrentPlayer());

        // 3. White captures Black pawn: e4 -> d5.
        game.makeMove(new Position(4, 3), new Position(3, 4));

        // The original White pawn now occupies d5.
        assertSame(whitePawn, board.getPiece(new Position(3, 4)));

        // Both original squares are empty.
        assertNull(board.getPiece(new Position(4, 3)));
        assertNull(board.getPiece(new Position(3, 6)));

        // Black pawn was captured.
        // The piece on d5 must not be the original Black pawn.
        org.junit.jupiter.api.Assertions.assertNotSame(
                blackPawn, board.getPiece(new Position(3, 4)));

        // Turn switches to Black.
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void IT02CastlingAfterMultipleMoves() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // 1. White moves the knight away from g1.
        game.makeMove(new Position(6, 0), new Position(5, 2)); // Ng1-f3

        // 2. Black moves a pawn.
        game.makeMove(new Position(0, 6), new Position(0, 5)); // a7-a6

        // 3. White moves the e-pawn to open the bishop's diagonal.
        game.makeMove(new Position(4, 1), new Position(4, 3)); // e2-e4

        // 4. Black moves another pawn.
        game.makeMove(new Position(0, 5), new Position(0, 4)); // a6-a5

        // 5. White moves the bishop away from f1.
        game.makeMove(new Position(5, 0), new Position(4, 1)); // Bf1-e2

        // 6. Black makes another legal move.
        game.makeMove(new Position(1, 6), new Position(1, 5)); // b7-b6

        // 7. White castles king-side: e1-g1.
        game.makeMove(new Position(4, 0), new Position(6, 0));

        // Verify the King moved to g1.
        Piece king = board.getPiece(new Position(6, 0));
        assertEquals(PieceType.KING, king.getType());
        assertEquals(Color.WHITE, king.getColor());

        // Verify the Rook moved from h1 to f1.
        Piece rook = board.getPiece(new Position(5, 0));
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals(Color.WHITE, rook.getColor());

        // Original squares are now empty.
        assertNull(board.getPiece(new Position(4, 0)));
        assertNull(board.getPiece(new Position(7, 0)));

        // Castling rights are permanently lost after castling.
        org.junit.jupiter.api.Assertions.assertFalse(
                game.canCastleKingSide(Color.WHITE));
        org.junit.jupiter.api.Assertions.assertFalse(
                game.canCastleQueenSide(Color.WHITE));

        // Turn switches to Black.
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }
}