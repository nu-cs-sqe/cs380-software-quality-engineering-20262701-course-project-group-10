package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

final class MoveRuleAssertions {
    private static final int BOARD_SIZE = 8;

    private MoveRuleAssertions() {
    }

    static void assertMove(Board board, Position from, Position to, boolean expected) {
        Piece[][] before = new Piece[BOARD_SIZE][BOARD_SIZE];
        for (int file = 0; file < BOARD_SIZE; file++) {
            for (int rank = 0; rank < BOARD_SIZE; rank++) {
                before[file][rank] = board.getPiece(new Position(file, rank));
            }
        }
        assertEquals(expected, new MoveValidator().isLegalMove(board, from, to));
        for (int file = 0; file < BOARD_SIZE; file++) {
            for (int rank = 0; rank < BOARD_SIZE; rank++) {
                assertSame(before[file][rank], board.getPiece(new Position(file,
                        rank)));
            }
        }
    }

    // Uses Board.copy() as the snapshot, so the board must be unchanged afterward.
    static void assertAttack(Board board, Position square, Color attacker, boolean expected) {
        Board before = board.copy();
        assertEquals(expected, new MoveValidator().isSquareAttacked(board, square, attacker));
        assertSameSquares(before, board);
    }

    static void assertCheck(Board board, Color color, boolean expected) {
        Board before = board.copy();
        assertEquals(expected, new MoveValidator().isInCheck(board, color));
        assertSameSquares(before, board);
    }

    private static void assertSameSquares(Board before, Board after) {
        for (int file = 0; file < BOARD_SIZE; file++) {
            for (int rank = 0; rank < BOARD_SIZE; rank++) {
                Position position = new Position(file, rank);
                assertSame(before.getPiece(position), after.getPiece(position));
            }
        }
    }
}
