package domain;

public class MoveValidator {
	public boolean isLegalMove(Board board, Position from, Position to) {
		if (board == null || from == null || to == null || from.equals(to)) {
			return false;
		}
		Piece piece = board.getPiece(from);
		if (piece == null) {
			return false;
		}
		switch (piece.getType()) {
			case ROOK:
				return isRookMove(board, from, to);
			default:
				return false;
		}
	}

	private boolean isRookMove(Board board, Position from, Position to) {
		return (from.getFile() == to.getFile() || from.getRank() == to.getRank())
				&& isPathClear(board, from, to);
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
