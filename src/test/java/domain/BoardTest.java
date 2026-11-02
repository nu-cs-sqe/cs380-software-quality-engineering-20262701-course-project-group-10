package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    // ---------- getPiece() tests ----------

    @Test
    void getPiece_returnsNullForEmptySquare() {
        Board board = new Board();

        Piece result = board.getPiece(new Position(0, 0));

        assertNull(result);
    }

    @Test
    void getPiece_returnsPieceAtA1() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.ROOK, Color.WHITE);
        Position position = new Position(0, 0);

        board.placePiece(piece, position);

        assertSame(piece, board.getPiece(position));
    }

    @Test
    void getPiece_returnsPieceAtH1() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.ROOK, Color.WHITE);
        Position position = new Position(7, 0);

        board.placePiece(piece, position);

        assertSame(piece, board.getPiece(position));
    }

    @Test
    void getPiece_returnsPieceAtA8() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.ROOK, Color.BLACK);
        Position position = new Position(0, 7);

        board.placePiece(piece, position);

        assertSame(piece, board.getPiece(position));
    }

    @Test
    void getPiece_returnsPieceAtH8() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.ROOK, Color.BLACK);
        Position position = new Position(7, 7);

        board.placePiece(piece, position);

        assertSame(piece, board.getPiece(position));
    }

    @Test
    void getPiece_returnsPieceAtInteriorPosition() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.KNIGHT, Color.WHITE);
        Position position = new Position(3, 3);

        board.placePiece(piece, position);

        assertSame(piece, board.getPiece(position));
    }


    // ---------- placePiece() tests ----------

    @Test
    void placePiece_placesPieceAtA1() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.ROOK, Color.WHITE);
        Position position = new Position(0, 0);

        board.placePiece(piece, position);

        assertSame(piece, board.getPiece(position));
    }

    @Test
    void placePiece_placesPieceAtH1() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.ROOK, Color.WHITE);
        Position position = new Position(7, 0);

        board.placePiece(piece, position);

        assertSame(piece, board.getPiece(position));
    }

    @Test
    void placePiece_placesPieceAtA8() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.ROOK, Color.BLACK);
        Position position = new Position(0, 7);

        board.placePiece(piece, position);

        assertSame(piece, board.getPiece(position));
    }

    @Test
    void placePiece_placesPieceAtH8() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.ROOK, Color.BLACK);
        Position position = new Position(7, 7);

        board.placePiece(piece, position);

        assertSame(piece, board.getPiece(position));
    }

    @Test
    void placePiece_placesPieceAtInteriorPosition() {
        Board board = new Board();
        Piece piece = new Piece(PieceType.KNIGHT, Color.WHITE);
        Position position = new Position(3, 3);

        board.placePiece(piece, position);

        assertSame(piece, board.getPiece(position));
    }

    // ---------- isEmpty() tests ----------

    @Test
    void isEmpty_returnsTrueForEmptyA1() {
        Board board = new Board();
        Position position = new Position(0, 0);

        assertTrue(board.isEmpty(position));
    }

    @Test
    void isEmpty_returnsTrueForEmptyH1() {
        Board board = new Board();
        Position position = new Position(7, 0);

        assertTrue(board.isEmpty(position));
    }

    @Test
    void isEmpty_returnsTrueForEmptyA8() {
        Board board = new Board();
        Position position = new Position(0, 7);

        assertTrue(board.isEmpty(position));
    }

    @Test
    void isEmpty_returnsTrueForEmptyH8() {
        Board board = new Board();
        Position position = new Position(7, 7);

        assertTrue(board.isEmpty(position));
    }

    @Test
    void isEmpty_returnsFalseForOccupiedA1() {
        Board board = new Board();
        Position position = new Position(0, 0);
        Piece piece = new Piece(PieceType.ROOK, Color.WHITE);

        board.placePiece(piece, position);

        assertFalse(board.isEmpty(position));
    }

    @Test
    void isEmpty_returnsFalseForOccupiedH8() {
        Board board = new Board();
        Position position = new Position(7, 7);
        Piece piece = new Piece(PieceType.ROOK, Color.BLACK);

        board.placePiece(piece, position);

        assertFalse(board.isEmpty(position));
    }

    @Test
    void isEmpty_returnsFalseForOccupiedInteriorPosition() {
        Board board = new Board();
        Position position = new Position(3, 3);
        Piece piece = new Piece(PieceType.KNIGHT, Color.WHITE);

        board.placePiece(piece, position);

        assertFalse(board.isEmpty(position));
    }




    // Count all pieces belonging to a player
    private int countPieces(Board board, Color color) {
        int count = 0;

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece piece = board.getPiece(new Position(file, rank));

                if (piece != null && piece.getColor() == color) {
                    count++;
                }
            }
        }

        return count;
    }

    @Test
    void setUpStartingPosition_createsExactly32Pieces() {
        Board board = new Board();

        board.setUpStartingPosition();

        int total = countPieces(board, Color.WHITE)
                + countPieces(board, Color.BLACK);

        assertEquals(32, total);
    }

    @Test
    void setUpStartingPosition_creates16WhitePieces() {
        Board board = new Board();

        board.setUpStartingPosition();

        assertEquals(16, countPieces(board, Color.WHITE));
    }

    @Test
    void setUpStartingPosition_creates16BlackPieces() {
        Board board = new Board();

        board.setUpStartingPosition();

        assertEquals(16, countPieces(board, Color.BLACK));
    }

    private int countPieces(Board board, Color color, PieceType type) {
        int count = 0;

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece piece = board.getPiece(new Position(file, rank));

                if (piece != null
                        && piece.getColor() == color
                        && piece.getType() == type) {
                    count++;
                }
            }
        }

        return count;
    }

    @Test
    public void testWhitePieceCounts() {
        Board board = new Board();
        board.setUpStartingPosition();

        assertEquals(1, countPieces(board, Color.WHITE, PieceType.KING));
        assertEquals(1, countPieces(board, Color.WHITE, PieceType.QUEEN));
        assertEquals(2, countPieces(board, Color.WHITE, PieceType.ROOK));
        assertEquals(2, countPieces(board, Color.WHITE, PieceType.BISHOP));
        assertEquals(2, countPieces(board, Color.WHITE, PieceType.KNIGHT));
        assertEquals(8, countPieces(board, Color.WHITE, PieceType.PAWN));
    }

    @Test
    public void testBlackPieceCounts() {
        Board board = new Board();
        board.setUpStartingPosition();

        assertEquals(1, countPieces(board, Color.BLACK, PieceType.KING));
        assertEquals(1, countPieces(board, Color.BLACK, PieceType.QUEEN));
        assertEquals(2, countPieces(board, Color.BLACK, PieceType.ROOK));
        assertEquals(2, countPieces(board, Color.BLACK, PieceType.BISHOP));
        assertEquals(2, countPieces(board, Color.BLACK, PieceType.KNIGHT));
        assertEquals(8, countPieces(board, Color.BLACK, PieceType.PAWN));
    }


}