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
    private static final int PAWN_DOUBLE_STEP = 2;
    private static final int PAWN_SINGLE_STEP = 1;

    private final Board board;
    private final MoveValidator validator = new MoveValidator();
    private Color currentPlayer;
    private Piece lastMovedPiece;
    private Position lastMoveFrom;
    private Position lastMoveTo;
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

        lastMovedPiece = null;
        lastMoveFrom = null;
        lastMoveTo = null;
    }

    public void makeMove(Position from, Position to) {
        if (requiresPromotion(from, to)) {
            throw new IllegalMoveException(MoveError.PROMOTION_REQUIRED);
        }

        Board trial = trialMove(from, to);
        commit(from, to, trial);
    }

    public void makeMove(Position from, Position to, PieceType promotion) {
        Objects.requireNonNull(promotion, "promotion");

        final Board trial = trialMove(from, to);
        Piece moving = board.getPiece(from);

        if (moving.getType() != PieceType.PAWN) {
            throw new IllegalMoveException(MoveError.NOT_A_PAWN);
        }

        int lastRank = moving.getColor() == Color.WHITE
                ? BLACK_HOME_RANK : WHITE_HOME_RANK;

        if (to.getRank() != lastRank) {
            throw new IllegalMoveException(MoveError.NOT_LAST_RANK);
        }

        if (promotion != PieceType.QUEEN
                && promotion != PieceType.ROOK
                && promotion != PieceType.BISHOP
                && promotion != PieceType.KNIGHT) {
            throw new IllegalMoveException(MoveError.INVALID_PROMOTION);
        }

        Piece promotedPiece = new Piece(promotion, moving.getColor());
        trial.placePiece(promotedPiece, to);

        commit(from, to, trial);
    }

    public boolean requiresPromotion(Position from, Position to) {
        trialMove(from, to);

        Piece moving = board.getPiece(from);

        if (moving.getType() != PieceType.PAWN) {
            return false;
        }

        int lastRank = moving.getColor() == Color.WHITE
                ? BLACK_HOME_RANK : WHITE_HOME_RANK;

        return to.getRank() == lastRank;
    }

    private Board trialMove(Position from, Position to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");

        requireCurrentPlayersPieceOn(from);
        Piece moving = board.getPiece(from);

        boolean castling = isCastlingAttempt(moving, from, to);
        boolean enPassant = isEnPassantAttempt(moving, from, to);

        if (castling) {
            requireCastlingAllowed(from, to);
        } else if (!enPassant) {
            requireLegalMove(from, to);
            requireNotCapturingOwnPiece(to);
            requireNotCapturingKing(to);
        }

        // Try the move on a copy so a rejected move never touches the real board.
        Board trial = board.copy();

        if (enPassant) {
            moveEnPassantPawn(trial, from, to);
        } else {
            trial.movePiece(from, to);

            if (castling) {
                moveCastlingRook(trial, from, to);
            }
        }

        requireKingSafe(trial);

        return trial;
    }

    private void commit(Position from, Position to, Board trial) {
        Piece moving = board.getPiece(from);

        board.copyFrom(trial);
        updateCastlingRights(moving, from, to);

        lastMovedPiece = moving;
        lastMoveFrom = from;
        lastMoveTo = to;

        passTurn();
        updateStatus();
    }

    private boolean isEnPassantAttempt(Piece moving, Position from, Position to) {
        if (moving.getType() != PieceType.PAWN) {
            return false;
        }

        if (!board.isEmpty(to)) {
            return false;
        }

        if (lastMovedPiece == null
                || lastMovedPiece.getType() != PieceType.PAWN) {
            return false;
        }

        if (Math.abs(lastMoveTo.getRank() - lastMoveFrom.getRank())
                != PAWN_DOUBLE_STEP) {
            return false;
        }

        int direction = moving.getColor() == Color.WHITE
                ? PAWN_SINGLE_STEP : -PAWN_SINGLE_STEP;

        boolean diagonalStep =
                Math.abs(to.getFile() - from.getFile()) == PAWN_SINGLE_STEP
                        && to.getRank() - from.getRank() == direction;

        boolean adjacentPawn =
                lastMoveTo.getRank() == from.getRank()
                        && lastMoveTo.getFile() == to.getFile();

        return diagonalStep
                && adjacentPawn
                && board.getPiece(lastMoveTo) == lastMovedPiece;
    }

    private void moveEnPassantPawn(Board trial, Position from, Position to) {
        trial.movePiece(from, to);
        trial.placePiece(null, lastMoveTo);
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
            throw new IllegalMoveException(MoveError.NO_CASTLING_RIGHT);
        }
        // A hand-built board can keep the right with no rook, so check the piece itself.
        Position rookSquare = castlingRookSquare(from, to);
        Piece rook = board.getPiece(rookSquare);
        if (rook == null || rook.getType() != PieceType.ROOK
                || rook.getColor() != currentPlayer) {
            throw new IllegalMoveException(MoveError.NO_CASTLING_ROOK);
        }
        requireEmptyBetween(from, rookSquare);
        if (validator.isInCheck(board, currentPlayer)) {
            throw new IllegalMoveException(MoveError.CASTLING_OUT_OF_CHECK);
        }
        // Use the real board: on the trial board the rook stands here and hides the attack.
        int step = Integer.compare(to.getFile(), from.getFile());
        Position crossing = new Position(from.getFile() + step, from.getRank());
        if (validator.isSquareAttacked(board, crossing, opponent())) {
            throw new IllegalMoveException(MoveError.CASTLING_THROUGH_ATTACK);
        }
    }

    // Checks every square strictly between the king and the rook on their shared rank.
    private void requireEmptyBetween(Position king, Position rook) {
        int step = Integer.compare(rook.getFile(), king.getFile());
        for (int file = king.getFile() + step; file != rook.getFile(); file += step) {
            if (!board.isEmpty(new Position(file, king.getRank()))) {
                throw new IllegalMoveException(MoveError.CASTLING_BLOCKED);
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
            throw new IllegalMoveException(MoveError.KING_IN_CHECK);
        }
    }

    private void requireNotCapturingKing(Position to) {
        Piece target = board.getPiece(to);
        if (target != null && target.getType() == PieceType.KING) {
            throw new IllegalMoveException(MoveError.CAPTURE_KING);
        }
    }

    private void requireNotCapturingOwnPiece(Position to) {
        Piece target = board.getPiece(to);
        if (target != null && target.getColor() == currentPlayer) {
            throw new IllegalMoveException(MoveError.OWN_PIECE);
        }
    }

    private void requireCurrentPlayersPieceOn(Position position) {
        Piece piece = board.getPiece(position);
        if (piece == null) {
            throw new IllegalMoveException(MoveError.NO_PIECE);
        }
        if (piece.getColor() != currentPlayer) {
            throw new IllegalMoveException(MoveError.NOT_YOUR_TURN);
        }
    }

    private void requireLegalMove(Position from, Position to) {
        if (!validator.isLegalMove(board, from, to)) {
            throw new IllegalMoveException(MoveError.ILLEGAL_MOVE);
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
                Position to = new Position(file, rank);
                try {
                    trialMove(from, to);
                    return true;
                } catch (IllegalArgumentException expected) {
                    // A rejected candidate does not end the search for a legal reply.
                }
            }
        }
        return false;
    }

    private void updateStatus() {
        if (hasAnyLegalMove()) {
            status = GameStatus.IN_PROGRESS;
            winner = null;
        } else if (isCurrentPlayerInCheck()) {
            status = GameStatus.CHECKMATE;
            winner = opponent();
        } else {
            status = GameStatus.STALEMATE;
            winner = null;
        }
    }
}
