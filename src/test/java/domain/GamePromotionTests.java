package domain;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}