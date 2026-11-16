package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Objects;
import java.util.Optional;

/** Manages turns, legal moves, king safety, and castling rights. */
public class Game {
    private static final int BOARD_SIZE = 8;
    private static final int QUEEN_SIDE_ROOK_FILE = 0;
    private static final int KING_SIDE_ROOK_FILE = 7;
    private static final int KING_SIDE_ROOK_TARGET_FILE = 5;
    private static final int QUEEN_SIDE_ROOK_TARGET_FILE = 3;
    private static final int CASTLING_KING_STEP = 2;
    private static final int KING_START_FILE = 4;
    private static final int WHITE_HOME_RANK = 0;
    private static final int BLACK_HOME_RANK = 7;
    private static final Position WHITE_QUEEN_SIDE_ROOK =
            new Position(QUEEN_SIDE_ROOK_FILE, WHITE_HOME_RANK);
    private static final Position WHITE_KING_SIDE_ROOK =
            new Position(KING_SIDE_ROOK_FILE, WHITE_HOME_RANK);
    private static final Position BLACK_QUEEN_SIDE_ROOK =
            new Position(QUEEN_SIDE_ROOK_FILE, BLACK_HOME_RANK);
    private static final Position BLACK_KING_SIDE_ROOK =
            new Position(KING_SIDE_ROOK_FILE, BLACK_HOME_RANK);

    private final Board board;
    private final MoveValidator validator = new MoveValidator();
    private Color currentPlayer;
    private GameStatus status = GameStatus.IN_PROGRESS;
    private Color winner;
    private boolean whiteKingSide = true;
    private boolean whiteQueenSide = true;
    private boolean blackKingSide = true;
    private boolean blackQueenSide = true;

    public Game(Board board) {
        this.board = Objects.requireNonNull(board, "board");
        this.currentPlayer = Color.WHITE;
    }


    public void startNewGame() {
        board.setUpStartingPosition();
        currentPlayer = Color.WHITE;
        whiteKingSide = true;
        whiteQueenSide = true;
        blackKingSide = true;
        blackQueenSide = true;
    }

    public void makeMove(Position from, Position to) {
        Board trial = trialMove(from, to);
        commit(from, to, trial);
    }

    private Board trialMove(Position from, Position to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");

        requireCurrentPlayersPieceOn(from);
        Piece moving = board.getPiece(from);
        boolean castling = isCastlingAttempt(moving, from, to);
        if (castling) {
            requireCastlingAllowed(from, to);
        } else {
            requireLegalMove(from, to);
            requireNotCapturingOwnPiece(to);
            requireNotCapturingKing(to);
        }

        // Try the move on a copy so a rejected move never touches the real board.
        Board trial = board.copy();
        trial.movePiece(from, to);
        if (castling) {
            moveCastlingRook(trial, from, to);
        }
        requireKingSafe(trial);

        return trial;
    }

    private void commit(Position from, Position to, Board trial) {
        Piece moving = board.getPiece(from);
        board.copyFrom(trial);
        updateCastlingRights(moving, from, to);
        passTurn();
    }

    // A king stepping two files from its original square is castling; the normal king
    // rule would reject it. Any other two-file king move stays illegal.
    private boolean isCastlingAttempt(Piece moving, Position from, Position to) {
        return moving.getType() == PieceType.KING
                && from.equals(kingStartSquare(moving.getColor()))
                && to.getRank() == from.getRank()
                && Math.abs(to.getFile() - from.getFile()) == CASTLING_KING_STEP;
    }

    private Position kingStartSquare(Color color) {
        int rank = color == Color.WHITE ? WHITE_HOME_RANK : BLACK_HOME_RANK;
        return new Position(KING_START_FILE, rank);
    }

    private boolean isKingSideCastle(Position from, Position to) {
        return to.getFile() > from.getFile();
    }

    private void requireCastlingAllowed(Position from, Position to) {
        boolean hasRight = isKingSideCastle(from, to)
                ? canCastleKingSide(currentPlayer)
                : canCastleQueenSide(currentPlayer);
        if (!hasRight) {
            throw new IllegalArgumentException("You can no longer castle on that side");
        }
        // A hand-built board can keep the right with no rook, so check the piece itself.
        Position rookSquare = castlingRookSquare(from, to);
        Piece rook = board.getPiece(rookSquare);
        if (rook == null || rook.getType() != PieceType.ROOK
                || rook.getColor() != currentPlayer) {
            throw new IllegalArgumentException("No rook to castle with");
        }
        requireEmptyBetween(from, rookSquare);
        if (validator.isInCheck(board, currentPlayer)) {
            throw new IllegalArgumentException("You cannot castle out of check");
        }
        // Use the real board: on the trial board the rook stands here and hides the attack.
        int step = Integer.compare(to.getFile(), from.getFile());
        Position crossing = new Position(from.getFile() + step, from.getRank());
        if (validator.isSquareAttacked(board, crossing, opponent())) {
            throw new IllegalArgumentException(
                    "Your king cannot castle through an attacked square");
        }
    }

    // Checks every square strictly between the king and the rook on their shared rank.
    private void requireEmptyBetween(Position king, Position rook) {
        int step = Integer.compare(rook.getFile(), king.getFile());
        for (int file = king.getFile() + step; file != rook.getFile(); file += step) {
            if (!board.isEmpty(new Position(file, king.getRank()))) {
                throw new IllegalArgumentException("Pieces are in the way of castling");
            }
        }
    }

    private Position castlingRookSquare(Position from, Position to) {
        int file = isKingSideCastle(from, to) ? KING_SIDE_ROOK_FILE : QUEEN_SIDE_ROOK_FILE;
        return new Position(file, from.getRank());
    }

    // The rook jumps to the square the king crossed, on the king's own home rank.
    private void moveCastlingRook(Board trial, Position from, Position to) {
        int file = isKingSideCastle(from, to)
                ? KING_SIDE_ROOK_TARGET_FILE : QUEEN_SIDE_ROOK_TARGET_FILE;
        trial.movePiece(castlingRookSquare(from, to), new Position(file, from.getRank()));
    }

    private void updateCastlingRights(Piece moving, Position from, Position to) {
        if (moving.getType() == PieceType.KING) {
            if (currentPlayer == Color.WHITE) {
                whiteKingSide = false;
                whiteQueenSide = false;
            } else {
                blackKingSide = false;
                blackQueenSide = false;
            }
        }
        loseRightAt(from);
        loseRightAt(to);
    }

    // A piece leaving or landing on an original rook square means that rook has moved
    // or is being captured.
    private void loseRightAt(Position square) {
        if (square.equals(WHITE_KING_SIDE_ROOK)) {
            whiteKingSide = false;
        } else if (square.equals(WHITE_QUEEN_SIDE_ROOK)) {
            whiteQueenSide = false;
        } else if (square.equals(BLACK_KING_SIDE_ROOK)) {
            blackKingSide = false;
        } else if (square.equals(BLACK_QUEEN_SIDE_ROOK)) {
            blackQueenSide = false;
        }
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
        currentPlayer = opponent();
    }

    private Color opponent() {
        return currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
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
        Objects.requireNonNull(color, "color");
        return color == Color.WHITE ? whiteKingSide : blackKingSide;
    }

    public boolean canCastleQueenSide(Color color) {
        Objects.requireNonNull(color, "color");
        return color == Color.WHITE ? whiteQueenSide : blackQueenSide;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Optional<Color> getWinner() {
        return Optional.ofNullable(winner);
    }

    public boolean isGameOver() {
        return status != GameStatus.IN_PROGRESS;
    }

    public boolean isCurrentPlayerInCheck() {
        return validator.isInCheck(board, currentPlayer);
    }

    public boolean hasAnyLegalMove() {
        for (int file = 0; file < BOARD_SIZE; file++) {
            for (int rank = 0; rank < BOARD_SIZE; rank++) {
                Position from = new Position(file, rank);
                Piece piece = board.getPiece(from);
                if (piece != null && piece.getColor() == currentPlayer && hasLegalMoveFrom(from)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasLegalMoveFrom(Position from) {
        for (int file = 0; file < BOARD_SIZE; file++) {
            for (int rank = 0; rank < BOARD_SIZE; rank++) {
                try {
                    trialMove(from, new Position(file, rank));
                    return true;
                } catch (IllegalArgumentException expected) {
                    // A rejected candidate does not end the search for a legal reply.
                }
            }
        }
        return false;
    }
}
