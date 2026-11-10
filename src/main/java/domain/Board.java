package domain;

public class Board {

    private Piece[][] squares;

    public Board() {
        squares = new Piece[8][8];
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
        squares = new Piece[8][8];

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

        for (int file = 0; file < 8; file++) {
            Piece piece = new Piece(backRank[file], color);

            placePiece(piece, new Position(file, rank));
        }
    }


}
