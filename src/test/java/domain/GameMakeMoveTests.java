package domain;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameMakeMoveTests {
	private static final Position FROM = new Position(0, 1);
	private static final Position TO = new Position(0, 3);
	private static final Piece WHITE_PAWN = new Piece(PieceType.PAWN, Color.WHITE);
	private static final Piece BLACK_PAWN = new Piece(PieceType.PAWN, Color.BLACK);

	private Board board;
	private MoveValidator validator;
	private Game game;

	@BeforeEach
	public void createGameWithMocks() {
		board = EasyMock.createMock(Board.class);
		validator = EasyMock.createMock(MoveValidator.class);
		game = new Game(board, validator);
	}

	private void expectPieceOn(Position position, Piece piece) {
		EasyMock.expect(board.getPiece(position)).andReturn(piece);
	}

	private void expectLegalMove(Piece piece, Position from, Position to) {
		expectPieceOn(from, piece);
		EasyMock.expect(validator.isLegalMove(board, from, to)).andReturn(true);
		board.movePiece(from, to);
		EasyMock.expectLastCall().once();
	}

	private void replayAll() {
		EasyMock.replay(board, validator);
	}

	private void verifyAll() {
		EasyMock.verify(board, validator);
	}

	// G14
	@Test
	public void legalWhiteMoveMovesPieceAndPassesTurnToBlack() {
		expectLegalMove(WHITE_PAWN, FROM, TO);
		replayAll();

		game.makeMove(FROM, TO);

		assertSame(Color.BLACK, game.getCurrentPlayer());
		verifyAll();
	}

	// G15
	@Test
	public void legalBlackMovePassesTurnBackToWhite() {
		Position blackFrom = new Position(0, 6);
		Position blackTo = new Position(0, 4);
		expectLegalMove(WHITE_PAWN, FROM, TO);
		expectLegalMove(BLACK_PAWN, blackFrom, blackTo);
		replayAll();

		game.makeMove(FROM, TO);
		game.makeMove(blackFrom, blackTo);

		assertSame(Color.WHITE, game.getCurrentPlayer());
		verifyAll();
	}

	// G10
	@Test
	public void movingFromAnEmptySquareIsRejected() {
		expectPieceOn(FROM, null);
		replayAll();

		assertThrows(IllegalArgumentException.class, () -> game.makeMove(FROM, TO));

		assertSame(Color.WHITE, game.getCurrentPlayer());
		verifyAll();
	}

	// G11
	@Test
	public void whiteCannotMoveABlackPiece() {
		expectPieceOn(FROM, BLACK_PAWN);
		replayAll();

		assertThrows(IllegalArgumentException.class, () -> game.makeMove(FROM, TO));

		assertSame(Color.WHITE, game.getCurrentPlayer());
		verifyAll();
	}

	// G12
	@Test
	public void blackCannotMoveAWhitePiece() {
		expectLegalMove(WHITE_PAWN, FROM, TO);
		expectPieceOn(FROM, WHITE_PAWN);
		replayAll();
		game.makeMove(FROM, TO);

		assertThrows(IllegalArgumentException.class, () -> game.makeMove(FROM, TO));

		assertSame(Color.BLACK, game.getCurrentPlayer());
		verifyAll();
	}

	// G13
	@Test
	public void illegalMoveIsRejectedAndTurnStays() {
		expectPieceOn(FROM, WHITE_PAWN);
		EasyMock.expect(validator.isLegalMove(board, FROM, TO)).andReturn(false);
		replayAll();

		assertThrows(IllegalArgumentException.class, () -> game.makeMove(FROM, TO));

		assertSame(Color.WHITE, game.getCurrentPlayer());
		verifyAll();
	}

	// G08
	@Test
	public void nullFromIsRejected() {
		replayAll();

		assertThrows(NullPointerException.class, () -> game.makeMove(null, TO));

		verifyAll();
	}

	// G09
	@Test
	public void nullToIsRejected() {
		replayAll();

		assertThrows(NullPointerException.class, () -> game.makeMove(FROM, null));

		verifyAll();
	}

	// G07
	@Test
	public void startNewGameAfterAMoveResetsTurnToWhite() {
		expectLegalMove(WHITE_PAWN, FROM, TO);
		board.setUpStartingPosition();
		EasyMock.expectLastCall().once();
		replayAll();
		game.makeMove(FROM, TO);

		game.startNewGame();

		assertSame(Color.WHITE, game.getCurrentPlayer());
		verifyAll();
	}

	// G16
	@Test
	public void nullValidatorIsRejected() {
		Board board = EasyMock.createMock(Board.class);
		assertThrows(NullPointerException.class, () -> new Game(board, null));
	}

	// G17
	@Test
	public void capturingOwnPieceIsRejected() {
		expectPieceOn(FROM, WHITE_PAWN);
		EasyMock.expect(validator.isLegalMove(board, FROM, TO)).andReturn(true);
		expectPieceOn(TO, WHITE_PAWN);
		replayAll();

		assertThrows(IllegalArgumentException.class, () -> game.makeMove(FROM, TO));

		assertSame(Color.WHITE, game.getCurrentPlayer());
		verifyAll();
	}
}