package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Objects;

/** A chess game: owns the board and tracks whose turn it is. */
public class Game {
    private final Board board;
    private final MoveValidator validator = new MoveValidator();
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
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");

        requireCurrentPlayersPieceOn(from);
        requireLegalMove(from, to);
        requireNotCapturingOwnPiece(to);
        requireNotCapturingKing(to);

        // Try the move on a copy so a rejected move never touches the real board.
        Board trial = board.copy();
        trial.movePiece(from, to);
        requireKingSafe(trial);

        board.copyFrom(trial);
        passTurn();
    }

    private void requireKingSafe(Board trial) {
        if (validator.isInCheck(trial, currentPlayer)) {
            throw new IllegalArgumentException("That move would leave your king in check");
        }
    }

    private void requireNotCapturingKing(Position to) {
        Piece target = board.getPiece(to);
        if (target != null && target.getType() == PieceType.KING) {
            throw new IllegalArgumentException("You cannot capture a king");
        }
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

    public boolean canCastleKingSide(Color color) {
        return true;
    }

    public boolean canCastleQueenSide(Color color) {
        return true;
    }
}
