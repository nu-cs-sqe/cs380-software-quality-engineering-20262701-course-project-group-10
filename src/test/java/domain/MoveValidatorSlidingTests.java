package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MoveValidatorSlidingTests {
	private final MoveValidator validator = new MoveValidator();

	private Board boardWith(PieceType type, Position from) {
		Board board = EasyMock.createMock(Board.class);
		EasyMock.expect(board.getPiece(from)).andStubReturn(new Piece(type, Color.WHITE));
		return board;
	}

	private void expectEmpty(Board board, Position position, boolean empty) {
		EasyMock.expect(board.isEmpty(position)).andReturn(empty);
	}

	private void assertMove(Board board, Position from, Position to, boolean legal) {
		EasyMock.replay(board);
		assertEquals(legal, validator.isLegalMove(board, from, to));
		EasyMock.verify(board);
	}

	@Test
	public void C01_rejectsNullBoard() {
		assertFalse(validator.isLegalMove(null, new Position(0, 0), new Position(0, 1)));
	}

	@Test
	public void C02_rejectsNullStart() {
		Board board = EasyMock.createMock(Board.class);
		assertMove(board, null, new Position(0, 1), false);
	}

	@Test
	public void C03_rejectsNullDestination() {
		Board board = EasyMock.createMock(Board.class);
		assertMove(board, new Position(0, 0), null, false);
	}

	@Test
	public void C04_rejectsEmptyStart() {
		Board board = EasyMock.createMock(Board.class);
		Position from = new Position(0, 0);
		EasyMock.expect(board.getPiece(from)).andStubReturn(null);
		assertMove(board, from, new Position(0, 1), false);
	}

	@Test
	public void C05_rejectsSameSquare() {
		Board board = EasyMock.createMock(Board.class);
		assertMove(board, new Position(0, 0), new Position(0, 0), false);
	}

	@Test
	public void R01_adjacentHorizontal() {
		Position from = new Position(0, 0);
		Board board = boardWith(PieceType.ROOK, from);
		assertMove(board, from, new Position(1, 0), true);
	}

	@Test
	public void R02_acrossRank() {
		Position from = new Position(7, 0);
		Board board = boardWith(PieceType.ROOK, from);
		expectEmpty(board, new Position(6, 0), true);
		expectEmpty(board, new Position(5, 0), true);
		expectEmpty(board, new Position(4, 0), true);
		expectEmpty(board, new Position(3, 0), true);
		expectEmpty(board, new Position(2, 0), true);
		expectEmpty(board, new Position(1, 0), true);
		assertMove(board, from, new Position(0, 0), true);
	}

	@Test
	public void R03_forwardFile() {
		Position from = new Position(0, 0);
		Board board = boardWith(PieceType.ROOK, from);
		expectEmpty(board, new Position(0, 1), true);
		assertMove(board, from, new Position(0, 2), true);
	}

	@Test
	public void R04_backwardFile() {
		Position from = new Position(0, 7);
		Board board = boardWith(PieceType.ROOK, from);
		expectEmpty(board, new Position(0, 6), true);
		assertMove(board, from, new Position(0, 5), true);
	}

	@Test
	public void R05_rejectsDiagonal() {
		Position from = new Position(0, 0);
		Board board = boardWith(PieceType.ROOK, from);
		assertMove(board, from, new Position(1, 1), false);
	}

	@Test
	public void R06_blockedNearStart() {
		Position from = new Position(0, 0);
		Board board = boardWith(PieceType.ROOK, from);
		expectEmpty(board, new Position(0, 1), false);
		assertMove(board, from, new Position(0, 3), false);
	}

	@Test
	public void R07_blockedNearEnd() {
		Position from = new Position(0, 0);
		Board board = boardWith(PieceType.ROOK, from);
		expectEmpty(board, new Position(0, 1), true);
		expectEmpty(board, new Position(0, 2), false);
		assertMove(board, from, new Position(0, 3), false);
	}

	@Test
	public void B01_adjacentDiagonal() {
		Position from = new Position(0, 0);
		Board board = boardWith(PieceType.BISHOP, from);
		assertMove(board, from, new Position(1, 1), true);
	}

	@Test
	public void B02_acrossDiagonal() {
		Position from = new Position(0, 0);
		Board board = boardWith(PieceType.BISHOP, from);
		expectEmpty(board, new Position(1, 1), true);
		expectEmpty(board, new Position(2, 2), true);
		expectEmpty(board, new Position(3, 3), true);
		expectEmpty(board, new Position(4, 4), true);
		expectEmpty(board, new Position(5, 5), true);
		expectEmpty(board, new Position(6, 6), true);
		assertMove(board, from, new Position(7, 7), true);
	}

	@Test
	public void B03_downLeft() {
		Position from = new Position(7, 7);
		Board board = boardWith(PieceType.BISHOP, from);
		expectEmpty(board, new Position(6, 6), true);
		assertMove(board, from, new Position(5, 5), true);
	}

	@Test
	public void B04_downRight() {
		Position from = new Position(0, 7);
		Board board = boardWith(PieceType.BISHOP, from);
		expectEmpty(board, new Position(1, 6), true);
		assertMove(board, from, new Position(2, 5), true);
	}

	@Test
	public void B05_upLeft() {
		Position from = new Position(7, 0);
		Board board = boardWith(PieceType.BISHOP, from);
		expectEmpty(board, new Position(6, 1), true);
		assertMove(board, from, new Position(5, 2), true);
	}

	@Test
	public void B06_rejectsUnequalSteps() {
		Position from = new Position(0, 0);
		Board board = boardWith(PieceType.BISHOP, from);
		assertMove(board, from, new Position(2, 1), false);
	}

	@Test
	public void B07_rejectsStraightLine() {
		Position from = new Position(0, 0);
		Board board = boardWith(PieceType.BISHOP, from);
		assertMove(board, from, new Position(0, 2), false);
	}

	@Test
	public void B08_blockedDiagonal() {
		Position from = new Position(0, 0);
		Board board = boardWith(PieceType.BISHOP, from);
		expectEmpty(board, new Position(1, 1), true);
		expectEmpty(board, new Position(2, 2), true);
		expectEmpty(board, new Position(3, 3), false);
		assertMove(board, from, new Position(4, 4), false);
	}
}
