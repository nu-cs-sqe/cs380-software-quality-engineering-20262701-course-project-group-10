package domain;

import java.util.Objects;

public class MoveValidator {
    private static final int BOARD_SIZE = 8;
    private static final int SINGLE_STEP = 1;
    private static final int DOUBLE_STEP = 2;
    private static final int WHITE_PAWN_START = 1;
    private static final int BLACK_PAWN_START = 6;

    public boolean isLegalMove(Board board, Position from, Position to) {
        Objects.requireNonNull(board, "board");
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (from.equals(to)) {
            return false;
        }
        Piece piece = board.getPiece(from);
        if (piece == null) {
            return false;
        }
        if (piece.getType() == PieceType.PAWN) {
            return isPawnMove(board, piece, from, to);
        }
        return matchesPieceShape(board, piece.getType(), from, to);
    }

    public boolean isSquareAttacked(Board board, Position square, Color attacker) {
        Objects.requireNonNull(board, "board");
        Objects.requireNonNull(square, "square");
        Objects.requireNonNull(attacker, "attacker");
        for (int file = 0; file < BOARD_SIZE; file++) {
            for (int rank = 0; rank < BOARD_SIZE; rank++) {
                Position from = new Position(file, rank);
                Piece piece = board.getPiece(from);
                // A piece never attacks its own square, whatever its step rule allows.
                if (!from.equals(square) && piece != null && piece.getColor() == attacker
                        && attacks(board, piece, from, square)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheck(Board board, Color color) {
        Objects.requireNonNull(board, "board");
        Objects.requireNonNull(color, "color");
        Color opponent = color == Color.WHITE ? Color.BLACK : Color.WHITE;
        return isSquareAttacked(board, findKing(board, color), opponent);
    }

    private Position findKing(Board board, Color color) {
        for (int file = 0; file < BOARD_SIZE; file++) {
            for (int rank = 0; rank < BOARD_SIZE; rank++) {
                Position square = new Position(file, rank);
                Piece piece = board.getPiece(square);
                if (piece != null && piece.getType() == PieceType.KING
                        && piece.getColor() == color) {
                    return square;
                }
            }
        }
        // Game always keeps both kings on the board, so a missing one is a bug, not a bad move.
        throw new IllegalStateException("No " + color + " king on the board");
    }

    private boolean attacks(Board board, Piece piece, Position from, Position square) {
        if (piece.getType() == PieceType.PAWN) {
            return isPawnAttack(piece, from, square);
        }
        return matchesPieceShape(board, piece.getType(), from, square);
    }

    // Shared by moves and attacks: every piece except the pawn moves the way it attacks.
    // Callers handle pawns first, so they never reach this switch.
    private boolean matchesPieceShape(Board board, PieceType type, Position from, Position to) {
        int fileChange = Math.abs(to.getFile() - from.getFile());
        int rankChange = Math.abs(to.getRank() - from.getRank());
        switch (type) {
            case KNIGHT:
                return isKnightMove(fileChange, rankChange);
            case KING:
                return isKingMove(fileChange, rankChange);
            case ROOK:
                return isRookMove(fileChange, rankChange)
                        && isPathClear(board, from, to);
            case BISHOP:
                return isBishopMove(fileChange, rankChange)
                        && isPathClear(board, from, to);
            default:
                // Queen is the remaining non-pawn type in the six-piece enum.
                return (isRookMove(fileChange, rankChange)
                        || isBishopMove(fileChange, rankChange))
                        && isPathClear(board, from, to);
        }
    }

    private int pawnDirection(Color color) {
        return color == Color.WHITE ? SINGLE_STEP : -SINGLE_STEP;
    }

    // Unlike a pawn move, an attack covers both forward diagonals even when they are empty.
    private boolean isPawnAttack(Piece pawn, Position from, Position square) {
        int direction = pawnDirection(pawn.getColor());
        int fileChange = Math.abs(square.getFile() - from.getFile());
        int forward = (square.getRank() - from.getRank()) * direction;
        return fileChange == SINGLE_STEP && forward == SINGLE_STEP;
    }

    private boolean isKnightMove(int fileChange, int rankChange) {
        return fileChange == SINGLE_STEP && rankChange == DOUBLE_STEP
                || fileChange == DOUBLE_STEP && rankChange == SINGLE_STEP;
    }

    private boolean isKingMove(int fileChange, int rankChange) {
        return fileChange <= SINGLE_STEP && rankChange <= SINGLE_STEP;
    }

    private boolean isPawnMove(Board board, Piece piece, Position from, Position to) {
        int direction = pawnDirection(piece.getColor());
        int forward = (to.getRank() - from.getRank()) * direction;
        int fileChange = Math.abs(to.getFile() - from.getFile());
        if (fileChange == SINGLE_STEP && forward == SINGLE_STEP) {
            Piece target = board.getPiece(to);
            return target != null && target.getColor() != piece.getColor();
        }
        if (fileChange != 0 || !board.isEmpty(to)) {
            return false;
        }
        if (forward == SINGLE_STEP) {
            return true;
        }
        int startRank = piece.getColor() == Color.WHITE
                ? WHITE_PAWN_START : BLACK_PAWN_START;
        return forward == DOUBLE_STEP && from.getRank() == startRank
                && board.isEmpty(new Position(from.getFile(),
                        from.getRank() + direction));
    }

    private boolean isRookMove(int fileChange, int rankChange) {
        return fileChange == 0 || rankChange == 0;
    }

    private boolean isBishopMove(int fileChange, int rankChange) {
        return fileChange == rankChange;
    }

    private boolean isPathClear(Board board, Position from, Position to) {
        int endFile = to.getFile();
        int endRank = to.getRank();
        int fileStep = Integer.compare(endFile, from.getFile());
        int rankStep = Integer.compare(endRank, from.getRank());
        int file = from.getFile() + fileStep;
        int rank = from.getRank() + rankStep;

        while (file != endFile || rank != endRank) {
            if (!board.isEmpty(new Position(file, rank))) {
                return false;
            }
            file += fileStep;
            rank += rankStep;
        }
        return true;
    }
}
