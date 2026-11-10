package domain;

import java.util.Objects;

/** Checks movement patterns without changing the board or enforcing turn order. */
public class MoveValidator {
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
        int fileChange = Math.abs(to.getFile() - from.getFile());
        int rankChange = Math.abs(to.getRank() - from.getRank());
        switch (piece.getType()) {
            case PAWN:
                return isPawnMove(board, piece, from, to);
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
                // Queen is the remaining type in the six-piece enum.
                return (isRookMove(fileChange, rankChange)
                        || isBishopMove(fileChange, rankChange))
                        && isPathClear(board, from, to);
        }
    }

    private boolean isKnightMove(int fileChange, int rankChange) {
        return fileChange == SINGLE_STEP && rankChange == DOUBLE_STEP
                || fileChange == DOUBLE_STEP && rankChange == SINGLE_STEP;
    }

    private boolean isKingMove(int fileChange, int rankChange) {
        return fileChange <= SINGLE_STEP && rankChange <= SINGLE_STEP;
    }

    private boolean isPawnMove(Board board, Piece piece, Position from, Position to) {
        int direction = piece.getColor() == Color.WHITE ? SINGLE_STEP : -SINGLE_STEP;
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
