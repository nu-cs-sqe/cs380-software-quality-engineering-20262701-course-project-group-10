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

    // P02: Cases and non-null pointers.
    @Test
    public void whiteQueenKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.QUEEN, Color.WHITE);
        assertSame(PieceType.QUEEN, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
        assertSame(PieceType.QUEEN, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
    }

    // P03: Cases and non-null pointers.
    @Test
    public void whiteRookKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.ROOK, Color.WHITE);
        assertSame(PieceType.ROOK, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
        assertSame(PieceType.ROOK, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
    }

    // P04: Cases and non-null pointers.
    @Test
    public void whiteBishopKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.BISHOP, Color.WHITE);
        assertSame(PieceType.BISHOP, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
        assertSame(PieceType.BISHOP, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
    }

    // P05: Cases and non-null pointers.
    @Test
    public void whiteKnightKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.KNIGHT, Color.WHITE);
        assertSame(PieceType.KNIGHT, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
        assertSame(PieceType.KNIGHT, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
    }

    // P06: Cases and non-null pointers.
    @Test
    public void whitePawnKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.PAWN, Color.WHITE);
        assertSame(PieceType.PAWN, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
        assertSame(PieceType.PAWN, piece.getType());
        assertSame(Color.WHITE, piece.getColor());
    }

    // P07: Cases and non-null pointers.
    @Test
    public void blackKingKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.KING, Color.BLACK);
        assertSame(PieceType.KING, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
        assertSame(PieceType.KING, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
    }

    // P08: Cases and non-null pointers.
    @Test
    public void blackQueenKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.QUEEN, Color.BLACK);
        assertSame(PieceType.QUEEN, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
        assertSame(PieceType.QUEEN, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
    }

    // P09: Cases and non-null pointers.
    @Test
    public void blackRookKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.ROOK, Color.BLACK);
        assertSame(PieceType.ROOK, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
        assertSame(PieceType.ROOK, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
    }

    // P10: Cases and non-null pointers.
    @Test
    public void blackBishopKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.BISHOP, Color.BLACK);
        assertSame(PieceType.BISHOP, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
        assertSame(PieceType.BISHOP, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
    }

    // P11: Cases and non-null pointers.
    @Test
    public void blackKnightKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.KNIGHT, Color.BLACK);
        assertSame(PieceType.KNIGHT, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
        assertSame(PieceType.KNIGHT, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
    }

    // P12: Cases and non-null pointers.
    @Test
    public void blackPawnKeepsItsTypeAndColor() {
        Piece piece = new Piece(PieceType.PAWN, Color.BLACK);
        assertSame(PieceType.PAWN, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
        assertSame(PieceType.PAWN, piece.getType());
        assertSame(Color.BLACK, piece.getColor());
    }

    // P13: null pointer boundary.
    @Test
    public void nullTypeIsRejected() {
        assertThrows(NullPointerException.class, () -> new Piece(null, Color.WHITE));
    }
}
