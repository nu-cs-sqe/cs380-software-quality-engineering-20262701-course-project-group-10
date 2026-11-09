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
}
