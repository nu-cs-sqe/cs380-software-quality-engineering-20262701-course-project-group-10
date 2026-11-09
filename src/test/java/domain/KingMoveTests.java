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
}
