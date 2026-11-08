package domain;

import java.util.Objects;

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
        throw new UnsupportedOperationException("makeMove is not implemented yet");
    }

    public Board getBoard() {
        return board;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }
}
