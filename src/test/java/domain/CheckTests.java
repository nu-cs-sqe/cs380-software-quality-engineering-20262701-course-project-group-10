package domain;

import org.junit.jupiter.api.Test;

public class CheckTests {
    private static final int FILE_E = 4;
    private static final int RANK_1 = 0;
    private static final Position E1 = new Position(FILE_E, RANK_1);

    // CK01: a lone king is not in check.
    @Test
    public void CK01LoneKingIsNotInCheck() {
        Board board = new Board();
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), E1);
        MoveRuleAssertions.assertCheck(board, Color.WHITE, false);
    }
}
