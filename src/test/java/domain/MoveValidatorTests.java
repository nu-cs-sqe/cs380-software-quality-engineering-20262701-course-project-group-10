package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoveValidatorTests {
	@Test
	public void V01NullBoardIsRejected() {
		assertThrows(NullPointerException.class,
				() -> new MoveValidator().isLegalMove(null,
						new Position(3, 3), new Position(3, 4)));
	}

	@Test
	public void V02NullSourceIsRejected() {
		MoveValidator validator = new MoveValidator();
		Board board = new Board();
		Position to = new Position(3, 4);
		assertThrows(NullPointerException.class,
				() -> validator.isLegalMove(board, null, to));
	}

	@Test
	public void V03NullDestinationIsRejected() {
		MoveValidator validator = new MoveValidator();
		Board board = new Board();
		Position from = new Position(3, 3);
		assertThrows(NullPointerException.class,
				() -> validator.isLegalMove(board, from, null));
	}

	@Test
	public void V04AllNullArgumentsAreRejected() {
		assertThrows(NullPointerException.class,
				() -> new MoveValidator().isLegalMove(null, null, null));
	}

	@Test
	public void V05EmptySourceIsIllegal() {
		Board board = new Board();
		assertFalse(new MoveValidator().isLegalMove(board,
				new Position(3, 3), new Position(3, 4)));
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				assertTrue(board.isEmpty(new Position(file, rank)));
			}
		}
	}

	@Test
	public void V06SamePositionReferenceIsIllegalForEveryOwnedTypeAndColor() {
		for (PieceType type : new PieceType[] {
				PieceType.PAWN, PieceType.KNIGHT, PieceType.KING}) {
			for (Color color : Color.values()) {
				Board board = new Board();
				Position position = new Position(3, 3);
				board.placePiece(new Piece(type, color), position);
				MoveRuleAssertions.assertMove(board, position, position, false);
			}
		}
	}

	@Test
	public void V07EqualPositionObjectsAreIllegalForEveryOwnedTypeAndColor() {
		for (PieceType type : new PieceType[] {
				PieceType.PAWN, PieceType.KNIGHT, PieceType.KING}) {
			for (Color color : Color.values()) {
				Board board = new Board();
				Position from = new Position(3, 3);
				board.placePiece(new Piece(type, color), from);
				MoveRuleAssertions.assertMove(board, from, new Position(3, 3),
						false);
			}
		}
	}

	@Test
	public void V08KnightReadsOnlyTheSource() {
		Board board = EasyMock.createStrictMock(Board.class);
		Position from = new Position(3, 3);
		Position to = new Position(4, 5);
		EasyMock.expect(board.getPiece(from))
				.andReturn(new Piece(PieceType.KNIGHT, Color.WHITE));
		EasyMock.replay(board);

		assertTrue(new MoveValidator().isLegalMove(board, from, to));

		EasyMock.verify(board);
	}

	@Test
	public void V09KingReadsOnlyTheSource() {
		Board board = EasyMock.createStrictMock(Board.class);
		Position from = new Position(3, 3);
		Position to = new Position(4, 4);
		EasyMock.expect(board.getPiece(from))
				.andReturn(new Piece(PieceType.KING, Color.BLACK));
		EasyMock.replay(board);

		assertTrue(new MoveValidator().isLegalMove(board, from, to));

		EasyMock.verify(board);
	}

	@Test
	public void V10PawnDoubleAdvanceReadsBothEmptySquares() {
		Board board = EasyMock.createStrictMock(Board.class);
		Position from = new Position(3, 1);
		Position to = new Position(3, 3);
		EasyMock.expect(board.getPiece(from))
				.andReturn(new Piece(PieceType.PAWN, Color.WHITE));
		EasyMock.expect(board.isEmpty(to)).andReturn(true);
		EasyMock.expect(board.isEmpty(new Position(3, 2))).andReturn(true);
		EasyMock.replay(board);

		assertTrue(new MoveValidator().isLegalMove(board, from, to));

		EasyMock.verify(board);
	}

	@Test
	public void V11PawnCaptureReadsTheOpponentAtTheDestination() {
		Board board = EasyMock.createStrictMock(Board.class);
		Position from = new Position(3, 3);
		Position to = new Position(4, 4);
		EasyMock.expect(board.getPiece(from))
				.andReturn(new Piece(PieceType.PAWN, Color.WHITE));
		EasyMock.expect(board.getPiece(to))
				.andReturn(new Piece(PieceType.ROOK, Color.BLACK));
		EasyMock.replay(board);

		assertTrue(new MoveValidator().isLegalMove(board, from, to));

		EasyMock.verify(board);
	}

	@Test
	public void V12SlidingPiecesFollowVerticalRules() {
		for (PieceType type : new PieceType[] {
				PieceType.ROOK, PieceType.BISHOP, PieceType.QUEEN}) {
			Board board = new Board();
			Position from = new Position(3, 3);
			board.placePiece(new Piece(type, Color.WHITE), from);
			boolean legal = type != PieceType.BISHOP;
			MoveRuleAssertions.assertMove(board, from, new Position(3, 4), legal);
		}
	}
}
