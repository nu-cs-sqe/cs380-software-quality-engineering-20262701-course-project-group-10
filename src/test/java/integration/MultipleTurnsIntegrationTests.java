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
}