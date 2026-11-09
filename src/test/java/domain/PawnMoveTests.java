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
}
