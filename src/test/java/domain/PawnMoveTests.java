package domain;

import org.junit.jupiter.api.Test;

public class PawnMoveTests {

    @Test
    public void P01WhitePawnAdvancesOneSquare() {
        Board board = new Board();
        Position from = new Position(3, 1);
        Position to = new Position(3, 2);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P02BlackPawnAdvancesOneSquare() {
        Board board = new Board();
        Position from = new Position(3, 6);
        Position to = new Position(3, 5);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P03WhitePawnCannotMoveBackward() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(3, 2);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P04BlackPawnCannotMoveBackward() {
        Board board = new Board();
        Position from = new Position(3, 4);
        Position to = new Position(3, 5);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P05WhitePawnCannotMoveSideways() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P06PawnCannotAdvanceThreeSquares() {
        Board board = new Board();
        Position from = new Position(3, 1);
        Position to = new Position(3, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P07BlackPawnCannotAdvanceThreeSquares() {
        Board board = new Board();
        Position from = new Position(3, 6);
        Position to = new Position(3, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P08WhitePawnCannotAdvanceOntoWhite() {
        Board board = new Board();
        Position from = new Position(3, 1);
        Position to = new Position(3, 2);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), new Position(3, 2));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P09WhitePawnCannotAdvanceOntoBlack() {
        Board board = new Board();
        Position from = new Position(3, 1);
        Position to = new Position(3, 2);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.KNIGHT, Color.BLACK), new Position(3, 2));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P10BlackPawnCannotAdvanceOntoWhite() {
        Board board = new Board();
        Position from = new Position(3, 6);
        Position to = new Position(3, 5);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), new Position(3, 5));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P11BlackPawnCannotAdvanceOntoBlack() {
        Board board = new Board();
        Position from = new Position(3, 6);
        Position to = new Position(3, 5);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.KNIGHT, Color.BLACK), new Position(3, 5));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P12WhitePawnAdvancesTwoFromStart() {
        Board board = new Board();
        Position from = new Position(3, 1);
        Position to = new Position(3, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P13BlackPawnAdvancesTwoFromStart() {
        Board board = new Board();
        Position from = new Position(3, 6);
        Position to = new Position(3, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P14WhitePawnCannotDoubleFromRank0() {
        Board board = new Board();
        Position from = new Position(3, 0);
        Position to = new Position(3, 2);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P15WhitePawnCannotDoubleFromRank2() {
        Board board = new Board();
        Position from = new Position(3, 2);
        Position to = new Position(3, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P16BlackPawnCannotDoubleFromRank5() {
        Board board = new Board();
        Position from = new Position(3, 5);
        Position to = new Position(3, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P17BlackPawnCannotDoubleFromRank7() {
        Board board = new Board();
        Position from = new Position(3, 7);
        Position to = new Position(3, 5);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P18WhiteDoubleBlockedByWhiteMidway() {
        Board board = new Board();
        Position from = new Position(3, 1);
        Position to = new Position(3, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.BISHOP, Color.WHITE), new Position(3, 2));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P19WhiteDoubleBlockedByBlackMidway() {
        Board board = new Board();
        Position from = new Position(3, 1);
        Position to = new Position(3, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.BISHOP, Color.BLACK), new Position(3, 2));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P20WhiteDoubleBlockedByWhiteAtDestination() {
        Board board = new Board();
        Position from = new Position(3, 1);
        Position to = new Position(3, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.BISHOP, Color.WHITE), new Position(3, 3));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P21WhiteDoubleBlockedByBlackAtDestination() {
        Board board = new Board();
        Position from = new Position(3, 1);
        Position to = new Position(3, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.BISHOP, Color.BLACK), new Position(3, 3));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P22BlackDoubleBlockedByWhiteMidway() {
        Board board = new Board();
        Position from = new Position(3, 6);
        Position to = new Position(3, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.BISHOP, Color.WHITE), new Position(3, 5));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P23BlackDoubleBlockedByBlackMidway() {
        Board board = new Board();
        Position from = new Position(3, 6);
        Position to = new Position(3, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.BISHOP, Color.BLACK), new Position(3, 5));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P24BlackDoubleBlockedByWhiteAtDestination() {
        Board board = new Board();
        Position from = new Position(3, 6);
        Position to = new Position(3, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.BISHOP, Color.WHITE), new Position(3, 4));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P25BlackDoubleBlockedByBlackAtDestination() {
        Board board = new Board();
        Position from = new Position(3, 6);
        Position to = new Position(3, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.BISHOP, Color.BLACK), new Position(3, 4));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P26WhitePawnCapturesLeft() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(2, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.ROOK, Color.BLACK), new Position(2, 4));
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P27WhitePawnCapturesRight() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.ROOK, Color.BLACK), new Position(4, 4));
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P28BlackPawnCapturesLeft() {
        Board board = new Board();
        Position from = new Position(3, 4);
        Position to = new Position(2, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), new Position(2, 3));
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P29BlackPawnCapturesRight() {
        Board board = new Board();
        Position from = new Position(3, 4);
        Position to = new Position(4, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), new Position(4, 3));
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P30WhitePawnRejectsEmptyDiagonalLeft() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(2, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P31WhitePawnRejectsEmptyDiagonalRight() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P32BlackPawnRejectsEmptyDiagonalLeft() {
        Board board = new Board();
        Position from = new Position(3, 4);
        Position to = new Position(2, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P33BlackPawnRejectsEmptyDiagonalRight() {
        Board board = new Board();
        Position from = new Position(3, 4);
        Position to = new Position(4, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P34WhitePawnRejectsFriendlyDiagonalLeft() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(2, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), new Position(2, 4));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P35WhitePawnRejectsFriendlyDiagonalRight() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), new Position(4, 4));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P36BlackPawnRejectsFriendlyDiagonalLeft() {
        Board board = new Board();
        Position from = new Position(3, 4);
        Position to = new Position(2, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), new Position(2, 3));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P37BlackPawnRejectsFriendlyDiagonalRight() {
        Board board = new Board();
        Position from = new Position(3, 4);
        Position to = new Position(4, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), new Position(4, 3));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P38WhitePawnRejectsCaptureOffsetP38() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(5, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.ROOK, Color.BLACK), new Position(5, 4));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P39WhitePawnRejectsCaptureOffsetP39() {
        Board board = new Board();
        Position from = new Position(3, 1);
        Position to = new Position(4, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.ROOK, Color.BLACK), new Position(4, 3));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P40WhitePawnRejectsCaptureOffsetP40() {
        Board board = new Board();
        Position from = new Position(3, 3);
        Position to = new Position(4, 2);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.ROOK, Color.BLACK), new Position(4, 2));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P41BlackPawnRejectsCaptureOffsetP41() {
        Board board = new Board();
        Position from = new Position(3, 4);
        Position to = new Position(1, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), new Position(1, 3));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P42BlackPawnRejectsCaptureOffsetP42() {
        Board board = new Board();
        Position from = new Position(3, 6);
        Position to = new Position(2, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), new Position(2, 4));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P43BlackPawnRejectsCaptureOffsetP43() {
        Board board = new Board();
        Position from = new Position(3, 4);
        Position to = new Position(2, 5);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), new Position(2, 5));
        MoveRuleAssertions.assertMove(board, from, to, false);
    }

    @Test
    public void P44WhitePawnReachesBoardEdgeP44() {
        Board board = new Board();
        Position from = new Position(0, 6);
        Position to = new Position(0, 7);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P45WhitePawnReachesBoardEdgeP45() {
        Board board = new Board();
        Position from = new Position(7, 6);
        Position to = new Position(7, 7);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P46BlackPawnReachesBoardEdgeP46() {
        Board board = new Board();
        Position from = new Position(0, 1);
        Position to = new Position(0, 0);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P47BlackPawnReachesBoardEdgeP47() {
        Board board = new Board();
        Position from = new Position(7, 1);
        Position to = new Position(7, 0);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P48WhitePawnReachesBoardEdgeP48() {
        Board board = new Board();
        Position from = new Position(0, 1);
        Position to = new Position(0, 3);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P49BlackPawnReachesBoardEdgeP49() {
        Board board = new Board();
        Position from = new Position(7, 6);
        Position to = new Position(7, 4);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P50WhitePawnCapturesAtBoardEdgeP50() {
        Board board = new Board();
        Position from = new Position(0, 6);
        Position to = new Position(1, 7);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.QUEEN, Color.BLACK), new Position(1, 7));
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P51WhitePawnCapturesAtBoardEdgeP51() {
        Board board = new Board();
        Position from = new Position(7, 6);
        Position to = new Position(6, 7);
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), from);
        board.placePiece(new Piece(PieceType.QUEEN, Color.BLACK), new Position(6, 7));
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P52BlackPawnCapturesAtBoardEdgeP52() {
        Board board = new Board();
        Position from = new Position(0, 1);
        Position to = new Position(1, 0);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.QUEEN, Color.WHITE), new Position(1, 0));
        MoveRuleAssertions.assertMove(board, from, to, true);
    }

    @Test
    public void P53BlackPawnCapturesAtBoardEdgeP53() {
        Board board = new Board();
        Position from = new Position(7, 1);
        Position to = new Position(6, 0);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), from);
        board.placePiece(new Piece(PieceType.QUEEN, Color.WHITE), new Position(6, 0));
        MoveRuleAssertions.assertMove(board, from, to, true);
    }
}
