package domain;

import java.util.Objects;

/** A chess game: owns the board. */
public final class Game {
    private final Board board;

    public Game(Board board) {
        this.board = Objects.requireNonNull(board, "board");
    }

    public Board getBoard() {
        return board;
    }
}
