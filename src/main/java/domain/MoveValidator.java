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
				return false;
			case KING:
				return false;
			default:
				// Sliding-piece rules are supplied by the separate task.
				return false;
		}
	}

	private boolean isPawnMove(Board board, Piece piece, Position from, Position to) {
		int direction = piece.getColor() == Color.WHITE ? SINGLE_STEP : -SINGLE_STEP;
		int forward = (to.getRank() - from.getRank()) * direction;
		return to.getFile() == from.getFile() && forward == SINGLE_STEP;
	}
}
