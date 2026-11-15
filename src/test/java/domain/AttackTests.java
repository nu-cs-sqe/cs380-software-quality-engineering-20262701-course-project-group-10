package domain;

import org.junit.jupiter.api.Test;

public class AttackTests {
    private static final int FILE_D = 3;
    private static final int FILE_E = 4;
    private static final int RANK_4 = 3;
    private static final int RANK_5 = 4;
    private static final Position D4 = new Position(FILE_D, RANK_4);
    private static final Position E5 = new Position(FILE_E, RANK_5);

    // AT01: an empty board has no attackers.
    @Test
    public void AT01EmptyBoardHasNoAttacks() {
        MoveRuleAssertions.assertAttack(new Board(), D4, Color.WHITE, false);
    }

    // AT02: a piece does not attack the square it stands on.
    @Test
    public void AT02PieceDoesNotAttackItsOwnSquare() {
        Board board = boardWith(PieceType.ROOK, Color.WHITE, D4);
        MoveRuleAssertions.assertAttack(board, D4, Color.WHITE, false);
    }

    // AT03: a White pawn attacks the empty forward-right diagonal.
    @Test
    public void AT03WhitePawnAttacksEmptyForwardRightDiagonal() {
        Board board = boardWith(PieceType.PAWN, Color.WHITE, D4);
        MoveRuleAssertions.assertAttack(board, E5, Color.WHITE, true);
    }

    private Board boardWith(PieceType type, Color color, Position position) {
        Board board = new Board();
        board.placePiece(new Piece(type, color), position);
        return board;
    }
}
