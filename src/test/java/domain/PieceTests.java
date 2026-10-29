package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PieceTests {
    // P01: Cases and non-null pointers.
    @Test
    public void whiteKingKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.KING, Color.WHITE);
        assertSame(PieceType.KING, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
        assertSame(PieceType.KING, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
    }
}
