package domain;

public class Board {

    private static final int BOARD_SIZE = 8;
    private static final int WHITE_BACK_RANK = 0;
    private static final int WHITE_PAWN_RANK = 1;
    private static final int BLACK_PAWN_RANK = 6;
    private static final int BLACK_BACK_RANK = 7;

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

        // Place white back rank
        placeBackRank(Color.WHITE, WHITE_BACK_RANK);

        // Place white pawns
        for (int file = 0; file < BOARD_SIZE; file++) {
            placePiece(
                    new Piece(PieceType.PAWN, Color.WHITE),
                    new Position(file, WHITE_PAWN_RANK)
            );
        }

        // Place black pawns
        for (int file = 0; file < BOARD_SIZE; file++) {
            placePiece(
                    new Piece(PieceType.PAWN, Color.BLACK),
                    new Position(file, BLACK_PAWN_RANK)
            );
        }

        // Place black back rank
        placeBackRank(Color.BLACK, BLACK_BACK_RANK);
    }

    private void placeBackRank(Color color, int rank) {

        PieceType[] backRank = {
                PieceType.ROOK,
                PieceType.KNIGHT,
                PieceType.BISHOP,
                PieceType.QUEEN,
                PieceType.KING,
                PieceType.BISHOP,
                PieceType.KNIGHT,
                PieceType.ROOK
        };

        for (int file = 0; file < BOARD_SIZE; file++) {
            Piece piece = new Piece(backRank[file], color);

            placePiece(piece, new Position(file, rank));
        }
    }


}
