package domain;

import java.util.Objects;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/** A chess game: owns the board and tracks whose turn it is. */
public class Game {
	private final Board board;
	private Color currentPlayer;
	private final MoveValidator moveValidator = new MoveValidator();

	public Game(Board board) {
		this.board = Objects.requireNonNull(board, "board");
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

		Piece piece = board.getPiece(from);

		if (piece == null) {
			throw new IllegalArgumentException("Starting square is empty.");
		}

		if (piece.getColor() != currentPlayer) {
			throw new IllegalArgumentException("Cannot move opponent's piece.");
		}

		Piece destinationPiece = board.getPiece(to);
		if (destinationPiece != null && destinationPiece.getColor() == currentPlayer) {
			throw new IllegalArgumentException("Cannot capture own piece.");
		}

		if (!moveValidator.isLegalMove(board, from, to)) {
			throw new IllegalArgumentException("Illegal move.");
		}

		board.movePiece(from, to);
		currentPlayer = currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
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
