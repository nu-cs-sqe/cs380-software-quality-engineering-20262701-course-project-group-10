package domain;
import java.util.Objects;

public class Board {
	private static final int BOARD_SIZE = 8;
	private static final int WHITE_BACK_RANK = 0;
	private static final int WHITE_PAWN_RANK = 1;
	private static final int BLACK_PAWN_RANK = 6;
	private static final int BLACK_BACK_RANK = 7;

	private static final PieceType[] BACK_RANK_ORDER = {
			PieceType.ROOK,
			PieceType.KNIGHT,
			PieceType.BISHOP,
			PieceType.QUEEN,
			PieceType.KING,
			PieceType.BISHOP,
			PieceType.KNIGHT,
			PieceType.ROOK
	};

	private Piece[][] squares;

	public Board() {
		squares = new Piece[BOARD_SIZE][BOARD_SIZE];
	}

	public Piece getPiece(Position position) {
		return squares[position.getFile()][position.getRank()];
	}

	public void placePiece(Piece piece, Position position) {
		squares[position.getFile()][position.getRank()] = piece;
	}

	public boolean isEmpty(Position position) {
		return getPiece(position) == null;
	}

	public void setUpStartingPosition() {
		// Clear the board before setting up the pieces
		squares = new Piece[BOARD_SIZE][BOARD_SIZE];

		placeBackRank(Color.WHITE, WHITE_BACK_RANK);
		placePawnRank(Color.WHITE, WHITE_PAWN_RANK);
		placePawnRank(Color.BLACK, BLACK_PAWN_RANK);
		placeBackRank(Color.BLACK, BLACK_BACK_RANK);

		// Place white back rank
		placeBackRank(Color.WHITE, 0);

		// Place white pawns
		for (int file = 0; file < 8; file++) {
			placePiece(
					new Piece(PieceType.PAWN, Color.WHITE),
					new Position(file, 1)
			);
		}

		// Place black pawns
		for (int file = 0; file < 8; file++) {
			placePiece(
					new Piece(PieceType.PAWN, Color.BLACK),
					new Position(file, 6)
			);
		}

		// Place black back rank
		placeBackRank(Color.BLACK, 7);
	}


	private void placeBackRank(Color color, int rank) {
		for (int file = 0; file < BOARD_SIZE; file++) {
			placePiece(new Piece(BACK_RANK_ORDER[file], color), new Position(file, rank));
		}
	}

	private void placePawnRank(Color color, int rank) {
		for (int file = 0; file < BOARD_SIZE; file++) {
			placePiece(new Piece(PieceType.PAWN, color), new Position(file, rank));
		}
	}
	

	public void movePiece(Position from, Position to) {
		Objects.requireNonNull(from, "from");
		Objects.requireNonNull(to, "to");
		requireDifferent(from, to);
		Piece movingPiece = requireOccupied(from);

		placePiece(movingPiece, to);
		placePiece(null, from);
	}

	private void requireDifferent(Position from, Position to) {
		if (from.equals(to)) {
			throw new IllegalArgumentException("A piece cannot move to its own square");
		}
	}

	private Piece requireOccupied(Position position) {
		Piece piece = getPiece(position);
		if (piece == null) {
			throw new IllegalArgumentException("No piece on " + position);
		}
		return piece;
	}
}
