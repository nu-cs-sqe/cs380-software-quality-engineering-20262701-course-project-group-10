package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GameTests {
	// G01: non-null pointer; the constructor must not touch the board.
	@Test
	public void newGameKeepsTheSameBoard() {
		Board board = EasyMock.createMock(Board.class);
		EasyMock.replay(board);

		Game game = new Game(board);

		assertSame(board, game.getBoard());
		assertSame(board, game.getBoard());
		EasyMock.verify(board);
	}

	// G02: null pointer boundary.
	@Test
	public void nullBoardIsRejected() {
		assertThrows(NullPointerException.class, () -> new Game(null));
	}

	// G03: zero startNewGame() calls; White is to move from construction.
	@Test
	public void whiteMovesFirstBeforeStartNewGame() {
		Board board = EasyMock.createMock(Board.class);
		EasyMock.replay(board);

		Game game = new Game(board);

		assertSame(Color.WHITE, game.getCurrentPlayer());
		assertSame(Color.WHITE, game.getCurrentPlayer());
		EasyMock.verify(board);
	}

	// G04: one startNewGame() call sets up the board exactly once.
	@Test
	public void startNewGameSetsUpTheBoardOnce() {
		Board board = EasyMock.createMock(Board.class);
		board.setUpStartingPosition();
		EasyMock.expectLastCall().once();
		EasyMock.replay(board);

		Game game = new Game(board);
		game.startNewGame();

		assertSame(Color.WHITE, game.getCurrentPlayer());
		EasyMock.verify(board);
	}

	// G05: more than one startNewGame() call sets up the board each time.
	@Test
	public void startNewGameTwiceSetsUpTheBoardTwice() {
		Board board = EasyMock.createMock(Board.class);
		board.setUpStartingPosition();
		EasyMock.expectLastCall().times(2);
		EasyMock.replay(board);

		Game game = new Game(board);
		game.startNewGame();
		game.startNewGame();

		assertSame(Color.WHITE, game.getCurrentPlayer());
		EasyMock.verify(board);
	}

	// G06: startNewGame() resets the same board instead of replacing it.
	@Test
	public void startNewGameKeepsTheSameBoard() {
		Board board = EasyMock.createMock(Board.class);
		board.setUpStartingPosition();
		EasyMock.expectLastCall().once();
		EasyMock.replay(board);

		Game game = new Game(board);
		game.startNewGame();

		assertSame(board, game.getBoard());
		EasyMock.verify(board);
	}

	@Test
	public void makeMove_movesPieceAndSwitchesPlayer() {
		Board board = new Board();
		Piece knight = new Piece(PieceType.KNIGHT, Color.WHITE);
		Position from = new Position(1, 0);
		Position to = new Position(2, 2);
		board.placePiece(knight, from);

		Game game = new Game(board);

		game.makeMove(from, to);

		assertNull(board.getPiece(from));
		assertSame(knight, board.getPiece(to));
		assertEquals(Color.BLACK, game.getCurrentPlayer());
	}

	@Test
	public void makeMove_rejectsEmptyStartingSquare() {
		Board board = new Board();
		Game game = new Game(board);
		Position from = new Position(0, 0);
		Position to = new Position(0, 3);

		org.junit.jupiter.api.Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> game.makeMove(from, to));

		assertNull(board.getPiece(to));
		assertEquals(Color.WHITE, game.getCurrentPlayer());
	}

	@Test
	public void makeMove_rejectsOpponentPiece() {
		Board board = new Board();
		Piece blackKnight = new Piece(PieceType.KNIGHT, Color.BLACK);
		Position from = new Position(1, 7);
		Position to = new Position(2, 5);
		board.placePiece(blackKnight, from);
		Game game = new Game(board);

		org.junit.jupiter.api.Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> game.makeMove(from, to));

		assertSame(blackKnight, board.getPiece(from));
		assertNull(board.getPiece(to));
		assertEquals(Color.WHITE, game.getCurrentPlayer());
	}

	@Test
	public void makeMove_rejectsDestinationOccupiedByOwnPiece() {
		Board board = new Board();
		Piece knight = new Piece(PieceType.KNIGHT, Color.WHITE);
		Piece ownPiece = new Piece(PieceType.ROOK, Color.WHITE);
		Position from = new Position(1, 0);
		Position to = new Position(2, 2);
		board.placePiece(knight, from);
		board.placePiece(ownPiece, to);
		Game game = new Game(board);

		org.junit.jupiter.api.Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> game.makeMove(from, to));

		assertSame(knight, board.getPiece(from));
		assertSame(ownPiece, board.getPiece(to));
		assertEquals(Color.WHITE, game.getCurrentPlayer());
	}
	@Test
	public void makeMove_capturesOpponentPieceAndSwitchesPlayer() {
		Board board = new Board();
		Piece whiteRook = new Piece(PieceType.ROOK, Color.WHITE);
		Piece blackKnight = new Piece(PieceType.KNIGHT, Color.BLACK);
		Position from = new Position(0, 0);
		Position to = new Position(0, 3);
		board.placePiece(whiteRook, from);
		board.placePiece(blackKnight, to);
		Game game = new Game(board);

		game.makeMove(from, to);

		assertNull(board.getPiece(from));
		assertSame(whiteRook, board.getPiece(to));
		assertEquals(Color.BLACK, game.getCurrentPlayer());
	}

	@Test
	public void makeMove_rejectsIllegalMovementPattern() {
		Board board = new Board();
		Piece rook = new Piece(PieceType.ROOK, Color.WHITE);
		Position from = new Position(0, 0);
		Position to = new Position(1, 2);
		board.placePiece(rook, from);
		Game game = new Game(board);

		org.junit.jupiter.api.Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> game.makeMove(from, to));

		assertSame(rook, board.getPiece(from));
		assertNull(board.getPiece(to));
		assertEquals(Color.WHITE, game.getCurrentPlayer());
	}

	@Test
	public void makeMove_rejectsSameStartingAndDestinationSquare() {
		Board board = new Board();
		Piece knight = new Piece(PieceType.KNIGHT, Color.WHITE);
		Position position = new Position(1, 0);
		board.placePiece(knight, position);
		Game game = new Game(board);

		org.junit.jupiter.api.Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> game.makeMove(position, position));

		assertSame(knight, board.getPiece(position));
		assertEquals(Color.WHITE, game.getCurrentPlayer());
	}
}
