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
}