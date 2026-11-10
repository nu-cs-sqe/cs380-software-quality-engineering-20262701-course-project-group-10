package domain;

import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/** A chess game: owns the board and tracks whose turn it is. */
public class Game {
	private final Board board;
	private Color currentPlayer;
	private final MoveValidator validator;

	public Game(Board board) {
		this(board, new MoveValidator());
	}

	public Game(Board board, MoveValidator validator) {
		this.board = Objects.requireNonNull(board, "board");
		this.validator = Objects.requireNonNull(validator, "validator");
		this.currentPlayer = Color.WHITE;
	}


	public void startNewGame() {
		board.setUpStartingPosition();
		currentPlayer = Color.WHITE;
	}

	// Stub so the click-to-move GUI compiles. The real version, with turn rules
	// and IllegalArgumentException for illegal moves, comes from the make-move task.
	public void makeMove(Position from, Position to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");

		requireCurrentPlayersPieceOn(from);
		requireLegalMove(from, to);
		requireNotCapturingOwnPiece(to);      

		board.movePiece(from, to);
		passTurn();
	}

	private void requireNotCapturingOwnPiece(Position to) {
		Piece target = board.getPiece(to);
		if (target != null && target.getColor() == currentPlayer) {
			throw new IllegalArgumentException("Cannot capture your own piece");
		}
	}

	private void requireCurrentPlayersPieceOn(Position position) {
		Piece piece = board.getPiece(position);
		if (piece == null) {
			throw new IllegalArgumentException("No piece on " + position);
		}
		if (piece.getColor() != currentPlayer) {
			throw new IllegalArgumentException("It is " + currentPlayer + "'s turn");
		}
	}

	private void requireLegalMove(Position from, Position to) {
		if (!validator.isLegalMove(board, from, to)) {
			throw new IllegalArgumentException("Illegal move");
		}
	}

	private void passTurn() {
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}


	@SuppressFBWarnings(
			value = "EI_EXPOSE_REP",
			justification = "G01/G06 require sharing the board with the GUI.")
	public Board getBoard() {
		return board;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}
}
