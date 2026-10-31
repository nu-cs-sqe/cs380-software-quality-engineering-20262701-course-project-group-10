package domain;

import java.util.Objects;

/** A chess game: owns the board and tracks whose turn it is. */
public final class Game {
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

    public Board getBoard() {
        return board;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }
}
