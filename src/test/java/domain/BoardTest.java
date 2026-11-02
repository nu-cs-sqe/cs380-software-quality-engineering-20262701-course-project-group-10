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
}