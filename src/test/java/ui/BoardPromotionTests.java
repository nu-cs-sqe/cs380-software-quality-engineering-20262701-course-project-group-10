package ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import domain.Board;
import domain.Color;
import domain.Game;
import domain.Piece;
import domain.PieceType;
import domain.Position;
import org.junit.jupiter.api.Test;

/** Tests promotion choices in board click handling. */
public class BoardPromotionTests {

    @Test
    public void PR16PromotionChoiceIsPassedToGame() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 6)); // e7

        Game game = new Game(board);

        // Simulate the player choosing Queen in the promotion dialog.
        BoardClickHandler handler = new BoardClickHandler(
                game,
                () -> PieceType.QUEEN);

        // First click selects the pawn.
        handler.click(new Position(4, 6));

        // Second click selects the destination e8.
        handler.click(new Position(4, 7));

        // The original square is now empty.
        assertNull(board.getPiece(new Position(4, 6)));

        // The destination contains a White Queen.
        Piece promoted = board.getPiece(new Position(4, 7));

        assertNotNull(promoted);
        assertSame(PieceType.QUEEN, promoted.getType());
        assertSame(Color.WHITE, promoted.getColor());

        // Turn switches to Black.
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void PR17CancellingPromotionLeavesGameUnchanged() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 6)); // e7

        Game game = new Game(board);

        // Returning null simulates cancelling the promotion dialog.
        BoardClickHandler handler = new BoardClickHandler(
                game,
                () -> null);

        // Select the pawn and its promotion destination.
        handler.click(new Position(4, 6)); // e7
        handler.click(new Position(4, 7)); // e8

        // The pawn must remain in its original position.
        assertSame(whitePawn, board.getPiece(new Position(4, 6)));
        assertNull(board.getPiece(new Position(4, 7)));

        // The turn must remain White.
        assertSame(Color.WHITE, game.getCurrentPlayer());

        // The selection must be cleared.
        assertNull(handler.getSelected());

        // The player can still make a normal legal move.
        handler.click(new Position(0, 0)); // a1
        handler.click(new Position(0, 1)); // a2

        assertSame(whiteKing, board.getPiece(new Position(0, 1)));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }
}