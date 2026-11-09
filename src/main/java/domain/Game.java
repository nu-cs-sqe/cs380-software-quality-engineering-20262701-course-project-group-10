package domain;

import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/** A chess game: owns the board and tracks whose turn it is. */
public class Game {
	private final Board board;
	private Color currentPlayer;

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
		throw new IllegalArgumentException("Moves are not available yet.");
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
