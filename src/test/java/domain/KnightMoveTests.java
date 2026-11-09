package domain;

import org.junit.jupiter.api.Test;

public class KnightMoveTests {

	@Test
	public void N01KnightAcceptsOffset1() {
		Board board = new Board();
		Position from = new Position(3, 3);
		Position to = new Position(4, 5);
		board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), from);
		MoveRuleAssertions.assertMove(board, from, to, true);
	}

	@Test
	public void N02KnightAcceptsOffset2() {
		Board board = new Board();
		Position from = new Position(3, 3);
		Position to = new Position(5, 4);
		board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), from);
		MoveRuleAssertions.assertMove(board, from, to, true);
	}

	@Test
	public void N03KnightAcceptsOffset3() {
		Board board = new Board();
		Position from = new Position(3, 3);
		Position to = new Position(2, 5);
		board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), from);
		MoveRuleAssertions.assertMove(board, from, to, true);
	}

	@Test
	public void N04KnightAcceptsOffset4() {
		Board board = new Board();
		Position from = new Position(3, 3);
		Position to = new Position(1, 4);
		board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), from);
		MoveRuleAssertions.assertMove(board, from, to, true);
	}

	@Test
	public void N05KnightAcceptsOffset5() {
		Board board = new Board();
		Position from = new Position(3, 3);
		Position to = new Position(4, 1);
		board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), from);
		MoveRuleAssertions.assertMove(board, from, to, true);
	}

	@Test
	public void N06KnightAcceptsOffset6() {
		Board board = new Board();
		Position from = new Position(3, 3);
		Position to = new Position(5, 2);
		board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), from);
		MoveRuleAssertions.assertMove(board, from, to, true);
	}

	@Test
	public void N07KnightAcceptsOffset7() {
		Board board = new Board();
		Position from = new Position(3, 3);
		Position to = new Position(2, 1);
		board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), from);
		MoveRuleAssertions.assertMove(board, from, to, true);
	}

	@Test
	public void N08KnightAcceptsOffset8() {
		Board board = new Board();
		Position from = new Position(3, 3);
		Position to = new Position(1, 2);
		board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), from);
		MoveRuleAssertions.assertMove(board, from, to, true);
	}
}
