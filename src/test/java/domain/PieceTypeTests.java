package domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class PieceTypeTests {
    @Test
    public void valuesContainExactlyTheSixPieceTypes() {
        assertArrayEquals(new PieceType[] {
                PieceType.KING, PieceType.QUEEN, PieceType.ROOK,
                PieceType.BISHOP, PieceType.KNIGHT, PieceType.PAWN
        }, PieceType.values());
    }
}
