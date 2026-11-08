package ui;

import domain.Board;
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

    public void clearSelection() {
        selected = null;
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

    public void click(Position position) {
        // toPosition returns null for clicks off the board.
        if (position == null) {
            return;
        }
        if (position.equals(selected)) {
            selected = null;
            return;
        }
        if (selected != null) {
            // Clear first so an illegal move, which throws, still leaves nothing selected.
            Position from = selected;
            selected = null;
            game.makeMove(from, position);
            return;
        }
        if (!isCurrentPlayersPiece(position)) {
            return;
        }
        selected = position;
    }

    private boolean isCurrentPlayersPiece(Position position) {
        Board board = game.getBoard();
        return !board.isEmpty(position)
                && board.getPiece(position).getColor() == game.getCurrentPlayer();
    }
}
