package ui;

import domain.Game;
import domain.Position;

import java.util.Objects;

public class BoardClickHandler {
    private static final int BOARD_SIZE = 8;

    private final Game game;
    private Position selected;

    public BoardClickHandler(Game game) {
        this.game = Objects.requireNonNull(game, "game");
    }

    public Position getSelected() {
        return selected;
    }

    public Position toPosition(int x, int y, int squareSize) {
        int boardPixels = BOARD_SIZE * squareSize;
        if (x < 0 || y < 0 || x >= boardPixels || y >= boardPixels) {
            return null;
        }
        int file = x / squareSize;
        // Rank 8 is drawn on the top row, so screen rows run opposite to ranks.
        int rank = BOARD_SIZE - 1 - y / squareSize;
        return new Position(file, rank);
    }
}
