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
        return true;
    }

    public void setUpStartingPosition() {
    }
}