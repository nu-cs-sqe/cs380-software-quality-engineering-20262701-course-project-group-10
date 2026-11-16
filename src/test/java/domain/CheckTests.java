package domain;

import org.junit.jupiter.api.Test;

public class CheckTests {
    private static final int FILE_A = 0;
    private static final int FILE_B = 1;
    private static final int FILE_E = 4;
    private static final int FILE_G = 6;
    private static final int FILE_H = 7;
    private static final int RANK_1 = 0;
    private static final int RANK_2 = 1;
    private static final int RANK_3 = 2;
    private static final int RANK_6 = 5;
    private static final int RANK_8 = 7;
    private static final Position A1 = new Position(FILE_A, RANK_1);
    private static final Position A8 = new Position(FILE_A, RANK_8);
    private static final Position B3 = new Position(FILE_B, RANK_3);
    private static final Position E1 = new Position(FILE_E, RANK_1);
    private static final Position E2 = new Position(FILE_E, RANK_2);
    private static final Position E8 = new Position(FILE_E, RANK_8);
    private static final Position G6 = new Position(FILE_G, RANK_6);
    private static final Position H8 = new Position(FILE_H, RANK_8);

    // CK01: a lone king is not in check.
    @Test
    public void CK01LoneKingIsNotInCheck() {
        Board board = new Board();
        place(board, PieceType.KING, Color.WHITE, E1);
        MoveRuleAssertions.assertCheck(board, Color.WHITE, false);
    }

    // CK02: an opponent's rook on the open file gives check.
    @Test
    public void CK02RookOnOpenFileGivesCheck() {
        Board board = new Board();
        place(board, PieceType.KING, Color.WHITE, E1);
        place(board, PieceType.ROOK, Color.BLACK, E8);
        MoveRuleAssertions.assertCheck(board, Color.WHITE, true);
    }

    // CK03: a piece between the attacker and the king blocks the check.
    @Test
    public void CK03BlockedRookDoesNotGiveCheck() {
        Board board = new Board();
        place(board, PieceType.KING, Color.WHITE, E1);
        place(board, PieceType.PAWN, Color.WHITE, E2);
        place(board, PieceType.ROOK, Color.BLACK, E8);
        MoveRuleAssertions.assertCheck(board, Color.WHITE, false);
    }

    // CK04: a king's own pieces never give check.
    @Test
    public void CK04OwnRookDoesNotGiveCheck() {
        Board board = new Board();
        place(board, PieceType.KING, Color.WHITE, E1);
        place(board, PieceType.ROOK, Color.WHITE, E8);
        MoveRuleAssertions.assertCheck(board, Color.WHITE, false);
    }

    // CK05: Black can be in check too.
    @Test
    public void CK05BlackKingInCheckFromQueen() {
        Board board = new Board();
        place(board, PieceType.KING, Color.BLACK, E8);
        place(board, PieceType.QUEEN, Color.WHITE, E1);
        MoveRuleAssertions.assertCheck(board, Color.BLACK, true);
    }

    // CK06: only the requested color's king matters; White's is attacked, Black's is safe.
    @Test
    public void CK06OnlyRequestedKingIsChecked() {
        Board board = new Board();
        place(board, PieceType.KING, Color.WHITE, A1);
        place(board, PieceType.KING, Color.BLACK, H8);
        place(board, PieceType.ROOK, Color.BLACK, A8);
        MoveRuleAssertions.assertCheck(board, Color.BLACK, false);
    }

    // CK07: a king on the first square searched (a1) is found.
    @Test
    public void CK07KingOnFirstSearchedSquareInCheck() {
        Board board = new Board();
        place(board, PieceType.KING, Color.WHITE, A1);
        place(board, PieceType.KNIGHT, Color.BLACK, B3);
        MoveRuleAssertions.assertCheck(board, Color.WHITE, true);
    }

    // CK08: a king on the last square searched (h8) is found.
    @Test
    public void CK08KingOnLastSearchedSquareInCheck() {
        Board board = new Board();
        place(board, PieceType.KING, Color.BLACK, H8);
        place(board, PieceType.KNIGHT, Color.WHITE, G6);
        MoveRuleAssertions.assertCheck(board, Color.BLACK, true);
    }

    private void place(Board board, PieceType type, Color color, Position position) {
        board.placePiece(new Piece(type, color), position);
    }
}
