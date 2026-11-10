package domain;

import org.junit.jupiter.api.Test;

public class KingMoveTests {

    @Test
    public void K01KingAcceptsAdjacentOffset1() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(3, 4);
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K02KingAcceptsAdjacentOffset2() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 3);
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K03KingAcceptsAdjacentOffset3() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K04KingAcceptsAdjacentOffset4() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(2, 3);
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K05KingAcceptsAdjacentOffset5() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(3, 2);
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K06KingAcceptsAdjacentOffset6() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(2, 2);
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K07KingAcceptsAdjacentOffset7() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(2, 4);
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K08KingAcceptsAdjacentOffset8() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 2);
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K09KingRejectsOffset9() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(3, 3);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void K10KingRejectsOffset10() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(3, 5);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void K11KingRejectsOffset11() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(5, 3);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void K12KingRejectsOffset12() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(5, 5);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void K13KingRejectsOffset13() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 5);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void K14KingRejectsOffset14() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(5, 4);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void K15KingRejectsOffset15() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(1, 3);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void K16KingRejectsOffset16() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(3, 1);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void K17KingRejectsOffset17() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(1, 1);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void K18KingRejectsOffset18() {
        Board board = new Board();
        Position from = new Position(0, 0);
        Position to = new Position(7, 7);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void K19KingMovesAtCorner19() {
        Board board = new Board();
        Position from = new Position(0, 0);
        Position to = new Position(1, 1);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K20KingMovesAtCorner20() {
        Board board = new Board();
        Position from = new Position(7, 7);
        Position to = new Position(6, 6);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K21KingMovesAtCorner21() {
        Board board = new Board();
        Position from = new Position(0, 7);
        Position to = new Position(1, 6);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K22KingMovesAtCorner22() {
        Board board = new Board();
        Position from = new Position(7, 0);
        Position to = new Position(6, 1);
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K23KingPatternIgnoresWhiteDestination() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), new Position(4, 4));
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void K24KingPatternIgnoresBlackDestination() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), new Position(4, 4));
        MoveRuleAssertions.assertMove(board, from, to, true);
    }
}
