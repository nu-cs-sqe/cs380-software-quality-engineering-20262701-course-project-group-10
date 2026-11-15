package domain;

import org.junit.jupiter.api.Test;

public class AttackTests {
    private static final int FILE_C = 2;
    private static final int FILE_D = 3;
    private static final int FILE_E = 4;
    private static final int FILE_F = 5;
    private static final int RANK_3 = 2;
    private static final int RANK_4 = 3;
    private static final int RANK_5 = 4;
    private static final int RANK_6 = 5;
    private static final Position C5 = new Position(FILE_C, RANK_5);
    private static final Position D4 = new Position(FILE_D, RANK_4);
    private static final Position D5 = new Position(FILE_D, RANK_5);
    private static final Position E3 = new Position(FILE_E, RANK_3);
    private static final Position E4 = new Position(FILE_E, RANK_4);
    private static final Position E5 = new Position(FILE_E, RANK_5);
    private static final Position F6 = new Position(FILE_F, RANK_6);

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

    // AT04: a White pawn attacks the empty forward-left diagonal.
    @Test
    public void AT04WhitePawnAttacksEmptyForwardLeftDiagonal() {
        Board board = boardWith(PieceType.PAWN, Color.WHITE, D4);
        MoveRuleAssertions.assertAttack(board, C5, Color.WHITE, true);
    }

    // AT05: a pawn never attacks straight ahead.
    @Test
    public void AT05WhitePawnDoesNotAttackStraightAhead() {
        Board board = boardWith(PieceType.PAWN, Color.WHITE, D4);
        MoveRuleAssertions.assertAttack(board, D5, Color.WHITE, false);
    }

    // AT06: a pawn does not attack backward.
    @Test
    public void AT06WhitePawnDoesNotAttackBackwardDiagonal() {
        Board board = boardWith(PieceType.PAWN, Color.WHITE, D4);
        MoveRuleAssertions.assertAttack(board, E3, Color.WHITE, false);
    }

    // AT07: a pawn's attack reaches one square, not two.
    @Test
    public void AT07WhitePawnDoesNotAttackTwoSquaresDiagonally() {
        Board board = boardWith(PieceType.PAWN, Color.WHITE, D4);
        MoveRuleAssertions.assertAttack(board, F6, Color.WHITE, false);
    }

    // AT08: a Black pawn attacks toward rank 1.
    @Test
    public void AT08BlackPawnAttacksDownward() {
        Board board = boardWith(PieceType.PAWN, Color.BLACK, D5);
        MoveRuleAssertions.assertAttack(board, E4, Color.BLACK, true);
    }

    private Board boardWith(PieceType type, Color color, Position position) {
        Board board = new Board();
        board.placePiece(new Piece(type, color), position);
        return board;
    }
}
