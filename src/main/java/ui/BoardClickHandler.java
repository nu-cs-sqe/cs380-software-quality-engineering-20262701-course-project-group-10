package ui;

import domain.Board;
import domain.Game;
import domain.PieceType;
import domain.Position;
import java.util.Objects;
import java.util.function.Supplier;

/** Translates board clicks into square selection and move requests. */
public class BoardClickHandler {
    private static final int BOARD_SIZE = 8;

    private final Game game;
    private final Supplier<PieceType> promotionChoice;
    private Position selected;

    public BoardClickHandler(Game game) {
        this(game, () -> null);
    }

    public BoardClickHandler(Game game, Supplier<PieceType> promotionChoice) {
        this.game = Objects.requireNonNull(game, "game");
        this.promotionChoice = Objects.requireNonNull(
                promotionChoice, "promotionChoice");
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
            clearSelection();
        } else if (selected != null) {
            moveSelectedTo(position);
        } else {
            selectIfOwnPiece(position);
        }
    }

    private void moveSelectedTo(Position to) {
        // Clear selection even if the move is rejected or cancelled.
        Position from = selected;
        clearSelection();

        if (game.requiresPromotion(from, to)) {
            PieceType choice = promotionChoice.get();

            // Cancelling the dialog must not change the game.
            if (choice == null) {
                return;
            }

            game.makeMove(from, to, choice);
        } else {
            game.makeMove(from, to);
        }
    }

    private void selectIfOwnPiece(Position position) {
        if (isCurrentPlayersPiece(position)) {
            selected = position;
        }
    }

    private boolean isCurrentPlayersPiece(Position position) {
        Board board = game.getBoard();
        return !board.isEmpty(position)
                && board.getPiece(position).getColor() == game.getCurrentPlayer();
    }
}
