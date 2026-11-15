package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BoardTest {
    private static final int BOARD_SIZE = 8;
    private static final int FILE_A = 0;
    private static final int FILE_D = 3;
    private static final int FILE_E = 4;
    private static final int FILE_H = 7;
    private static final int RANK_1 = 0;
    private static final int RANK_2 = 1;
    private static final int RANK_4 = 3;
    private static final int RANK_8 = 7;
    private static final Position A1 = new Position(FILE_A, RANK_1);
    private static final Position H1 = new Position(FILE_H, RANK_1);
    private static final Position A2 = new Position(FILE_A, RANK_2);
    private static final Position A8 = new Position(FILE_A, RANK_8);
    private static final Position H8 = new Position(FILE_H, RANK_8);
    private static final Position D4 = new Position(FILE_D, RANK_4);
    private static final Position E4 = new Position(FILE_E, RANK_4);

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

    private int countPieces(Board board) {
        int count = 0;

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                if (!board.isEmpty(new Position(file, rank))) {
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


    private void assertBackRank(Board board, Color color, int rank) {
        PieceType[] expected = {
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
            Piece piece = board.getPiece(new Position(file, rank));

            assertNotNull(piece);
            assertEquals(color, piece.getColor());
            assertEquals(expected[file], piece.getType());
        }
    }


    @Test
    public void testWhiteBackRankOrder() {
        Board board = new Board();
        board.setUpStartingPosition();

        assertBackRank(board, Color.WHITE, 0);
    }

    @Test
    public void testBlackBackRankOrder() {
        Board board = new Board();
        board.setUpStartingPosition();

        assertBackRank(board, Color.BLACK, 7);
    }

    private void assertPawnRank(Board board, Color color, int rank) {
        for (int file = 0; file < 8; file++) {
            Piece piece = board.getPiece(new Position(file, rank));

            assertNotNull(piece);
            assertEquals(color, piece.getColor());
            assertEquals(PieceType.PAWN, piece.getType());
        }
    }

    @Test
    public void testWhitePawnsOnSecondRank() {
        Board board = new Board();
        board.setUpStartingPosition();

        assertPawnRank(board, Color.WHITE, 1);
    }

    @Test
    public void testBlackPawnsOnSeventhRank() {
        Board board = new Board();
        board.setUpStartingPosition();

        assertPawnRank(board, Color.BLACK, 6);
    }

    @Test
    public void testMiddleFourRanksAreEmpty() {
        Board board = new Board();
        board.setUpStartingPosition();

        for (int file = 0; file < 8; file++) {
            for (int rank = 2; rank <= 5; rank++) {
                assertTrue(
                        board.isEmpty(new Position(file, rank)),
                        "Expected position (" + file + ", "
                                + rank + ") to be empty"
                );
            }
        }
    }

    @Test
    public void testSetUpStartingPositionClearsExistingPieces() {
        Board board = new Board();

        board.placePiece(
                new Piece(PieceType.QUEEN, Color.WHITE),
                new Position(3, 3)
        );

        board.setUpStartingPosition();

        assertTrue(board.isEmpty(new Position(3, 3)));

        assertEquals(32, countPieces(board));
    }

    @Test
    public void testSetUpStartingPositionCanBeCalledRepeatedly() {
        Board board = new Board();

        board.setUpStartingPosition();
        board.setUpStartingPosition();

        assertEquals(32, countPieces(board));

        assertBackRank(board, Color.WHITE, 0);
        assertBackRank(board, Color.BLACK, 7);
        assertPawnRank(board, Color.WHITE, 1);
        assertPawnRank(board, Color.BLACK, 6);
    }
  
    @Test
    void movePiece_fromA1ToH8() {
        assertMoveToEmptySquare(new Position(0, 0), new Position(7, 7));
    }

    private void assertMoveToEmptySquare(Position from, Position to) {
        Board board = new Board();
        Piece rook = new Piece(PieceType.ROOK, Color.WHITE);
        board.placePiece(rook, from);

        board.movePiece(from, to);

        assertSame(rook, board.getPiece(to));
        assertTrue(board.isEmpty(from));
    }

    @Test
    void movePiece_fromH8ToA1() {
        assertMoveToEmptySquare(new Position(7, 7), new Position(0, 0));
    }

    @Test
    void movePiece_fromH1ToA8() {
        assertMoveToEmptySquare(new Position(7, 0), new Position(0, 7));
    }

    @Test
    void movePiece_fromA8ToH1() {
        assertMoveToEmptySquare(new Position(0, 7), new Position(7, 0));
    }

    @Test
    void movePiece_betweenInteriorSquares() {
        assertMoveToEmptySquare(new Position(3, 3), new Position(3, 4));
    }

    @Test
    void movePiece_captureReplacesTargetPiece() {
        Board board = new Board();
        Piece attacker = new Piece(PieceType.ROOK, Color.WHITE);
        Position from = new Position(3, 3);
        Position to = new Position(3, 5);
        board.placePiece(attacker, from);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), to);

        board.movePiece(from, to);

        assertSame(attacker, board.getPiece(to));
        assertTrue(board.isEmpty(from));
    }

    @Test
    void movePiece_doesNotTouchOtherSquares() {
        Board board = new Board();
        Piece bystander = new Piece(PieceType.KING, Color.BLACK);
        Position bystanderSquare = new Position(5, 5);
        board.placePiece(bystander, bystanderSquare);
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), new Position(0, 0));

        board.movePiece(new Position(0, 0), new Position(0, 7));

        assertSame(bystander, board.getPiece(bystanderSquare));
    }

    @Test
    void movePiece_emptySourceIsRejected() {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class,
                () -> board.movePiece(new Position(0, 0), new Position(0, 1)));
    }

    @Test
    void movePiece_sameSquareIsRejectedAndPieceStays() {
        Board board = new Board();
        Piece rook = new Piece(PieceType.ROOK, Color.WHITE);
        Position square = new Position(3, 3);
        board.placePiece(rook, square);

        assertThrows(IllegalArgumentException.class, () -> board.movePiece(square, square));

        assertSame(rook, board.getPiece(square));
    }

    @Test
    void movePiece_nullFromIsRejected() {
        Board board = new Board();
        assertThrows(NullPointerException.class,
                () -> board.movePiece(null, new Position(3, 3)));
    }

    @Test
    void movePiece_nullToIsRejected() {
        Board board = new Board();
        Position from = new Position(3, 3);
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), from);

        assertThrows(NullPointerException.class, () -> board.movePiece(from, null));
    }

    // ---------- copy() tests ----------

    // BOARD-COPY-01: an empty board copies to a separate, empty board.
    @Test
    void copy_emptyBoardGivesSeparateEmptyBoard() {
        Board board = new Board();

        Board copy = board.copy();

        assertNotSame(board, copy);
        assertAllSquaresEmpty(copy);
    }

    // BOARD-COPY-02: corner and interior pieces copy as the same objects.
    @Test
    void copy_keepsCornerAndInteriorPieces() {
        Board board = boardWithCornerAndInteriorPieces();

        Board copy = board.copy();

        assertSameContents(board, copy);
    }

    // BOARD-COPY-03: the full starting position copies square for square.
    @Test
    void copy_keepsStartingPosition() {
        Board board = new Board();
        board.setUpStartingPosition();

        Board copy = board.copy();

        assertSameContents(board, copy);
    }

    // BOARD-COPY-04: a move on the copy does not reach the original.
    @Test
    void copy_changingCopyLeavesOriginalAlone() {
        Board board = new Board();
        Piece rook = new Piece(PieceType.ROOK, Color.WHITE);
        board.placePiece(rook, A1);
        Board copy = board.copy();

        copy.movePiece(A1, A2);

        assertSame(rook, board.getPiece(A1));
        assertTrue(board.isEmpty(A2));
    }

    // BOARD-COPY-05: a change to the original does not reach the copy.
    @Test
    void copy_changingOriginalLeavesCopyAlone() {
        Board board = new Board();
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), A1);
        Board copy = board.copy();

        board.placePiece(new Piece(PieceType.KING, Color.BLACK), H8);

        assertTrue(copy.isEmpty(H8));
    }

    // ---------- copyFrom() tests ----------

    // BOARD-COPYFROM-01: null pointer boundary; the board is left unchanged.
    @Test
    void copyFrom_nullIsRejectedAndBoardStays() {
        Board board = new Board();
        Piece queen = new Piece(PieceType.QUEEN, Color.WHITE);
        board.placePiece(queen, D4);

        assertThrows(NullPointerException.class, () -> board.copyFrom(null));

        assertSame(queen, board.getPiece(D4));
    }

    // BOARD-COPYFROM-02: an empty board takes the other board's pieces.
    @Test
    void copyFrom_emptyBoardTakesOtherBoardsPieces() {
        Board board = new Board();
        Board other = boardWithCornerAndInteriorPieces();

        board.copyFrom(other);

        assertSameContents(other, board);
    }

    // BOARD-COPYFROM-03: pieces the other board does not have are removed.
    @Test
    void copyFrom_emptyOtherClearsOldPieces() {
        Board board = new Board();
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), A1);
        board.placePiece(new Piece(PieceType.ROOK, Color.BLACK), H8);

        board.copyFrom(new Board());

        assertAllSquaresEmpty(board);
    }

    // BOARD-COPYFROM-04: a piece on the same square is overwritten.
    @Test
    void copyFrom_overwritesPieceOnSameSquare() {
        Board board = new Board();
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), E4);
        Board other = new Board();
        Piece knight = new Piece(PieceType.KNIGHT, Color.BLACK);
        other.placePiece(knight, E4);

        board.copyFrom(other);

        assertSame(knight, board.getPiece(E4));
    }

    // BOARD-COPYFROM-05: a later change to the other board does not reach this one.
    @Test
    void copyFrom_changingOtherLeavesBoardAlone() {
        Board board = new Board();
        Board other = new Board();
        other.placePiece(new Piece(PieceType.ROOK, Color.WHITE), A1);
        board.copyFrom(other);

        other.placePiece(new Piece(PieceType.KING, Color.BLACK), H8);

        assertTrue(board.isEmpty(H8));
    }

    // BOARD-COPYFROM-06: a later change to this board does not reach the other one.
    @Test
    void copyFrom_changingBoardLeavesOtherAlone() {
        Board board = new Board();
        Board other = new Board();
        Piece rook = new Piece(PieceType.ROOK, Color.WHITE);
        other.placePiece(rook, A1);
        board.copyFrom(other);

        board.movePiece(A1, A2);

        assertSame(rook, other.getPiece(A1));
        assertTrue(other.isEmpty(A2));
    }

    // BOARD-COPYFROM-07: copying a board onto itself changes nothing.
    @Test
    void copyFrom_sameBoardKeepsItsPieces() {
        Piece rook = new Piece(PieceType.ROOK, Color.WHITE);
        Piece queen = new Piece(PieceType.QUEEN, Color.WHITE);
        Board board = new Board();
        board.placePiece(rook, A1);
        board.placePiece(queen, D4);
        Board expected = new Board();
        expected.placePiece(rook, A1);
        expected.placePiece(queen, D4);

        board.copyFrom(board);

        assertSameContents(expected, board);
    }

    private Board boardWithCornerAndInteriorPieces() {
        Board board = new Board();
        board.placePiece(new Piece(PieceType.ROOK, Color.WHITE), A1);
        board.placePiece(new Piece(PieceType.KNIGHT, Color.WHITE), H1);
        board.placePiece(new Piece(PieceType.ROOK, Color.BLACK), A8);
        board.placePiece(new Piece(PieceType.KNIGHT, Color.BLACK), H8);
        board.placePiece(new Piece(PieceType.QUEEN, Color.WHITE), D4);
        return board;
    }

    // Compares all 64 squares, so a missing piece or a stray one both fail.
    private void assertSameContents(Board expected, Board actual) {
        for (int file = 0; file < BOARD_SIZE; file++) {
            for (int rank = 0; rank < BOARD_SIZE; rank++) {
                Position square = new Position(file, rank);
                assertSame(expected.getPiece(square), actual.getPiece(square));
            }
        }
    }

    private void assertAllSquaresEmpty(Board board) {
        for (int file = 0; file < BOARD_SIZE; file++) {
            for (int rank = 0; rank < BOARD_SIZE; rank++) {
                assertTrue(board.isEmpty(new Position(file, rank)));
            }
        }
    }
}
