package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CastlingTests {
    private static final int FILE_A = 0;
    private static final int FILE_B = 1;
    private static final int FILE_C = 2;
    private static final int FILE_D = 3;
    private static final int FILE_E = 4;
    private static final int FILE_F = 5;
    private static final int FILE_G = 6;
    private static final int FILE_H = 7;
    private static final int RANK_1 = 0;
    private static final int RANK_2 = 1;
    private static final int RANK_3 = 2;
    private static final int RANK_4 = 3;
    private static final int RANK_7 = 6;
    private static final int RANK_8 = 7;
    private static final Position A1 = new Position(FILE_A, RANK_1);
    private static final Position B1 = new Position(FILE_B, RANK_1);
    private static final Position C1 = new Position(FILE_C, RANK_1);
    private static final Position D1 = new Position(FILE_D, RANK_1);
    private static final Position E1 = new Position(FILE_E, RANK_1);
    private static final Position F1 = new Position(FILE_F, RANK_1);
    private static final Position G1 = new Position(FILE_G, RANK_1);
    private static final Position H1 = new Position(FILE_H, RANK_1);
    private static final Position A2 = new Position(FILE_A, RANK_2);
    private static final Position G2 = new Position(FILE_G, RANK_2);
    private static final Position E3 = new Position(FILE_E, RANK_3);
    private static final Position H2 = new Position(FILE_H, RANK_2);
    private static final Position B4 = new Position(FILE_B, RANK_4);
    private static final Position D4 = new Position(FILE_D, RANK_4);
    private static final Position E4 = new Position(FILE_E, RANK_4);
    private static final Position F4 = new Position(FILE_F, RANK_4);
    private static final Position G4 = new Position(FILE_G, RANK_4);
    private static final Position A7 = new Position(FILE_A, RANK_7);
    private static final Position A8 = new Position(FILE_A, RANK_8);
    private static final Position C8 = new Position(FILE_C, RANK_8);
    private static final Position D8 = new Position(FILE_D, RANK_8);
    private static final Position E8 = new Position(FILE_E, RANK_8);
    private static final Position F8 = new Position(FILE_F, RANK_8);
    private static final Position G8 = new Position(FILE_G, RANK_8);
    private static final Position H8 = new Position(FILE_H, RANK_8);

    private Board board;
    private Game game;

    @BeforeEach
    public void createGame() {
        board = new Board();
        game = new Game(board);
    }

    // G44: White castles king side; the attacked h1 rook does not stop it.
    @Test
    public void G44WhiteCastlesKingSide() {
        setUpCastlingPosition();
        Piece king = board.getPiece(E1);
        Piece rook = board.getPiece(H1);

        game.makeMove(E1, G1);

        assertSame(king, board.getPiece(G1));
        assertSame(rook, board.getPiece(F1));
        assertTrue(board.isEmpty(E1));
        assertTrue(board.isEmpty(H1));
        assertFalse(game.canCastleKingSide(Color.WHITE));
        assertFalse(game.canCastleQueenSide(Color.WHITE));
        assertTrue(game.canCastleKingSide(Color.BLACK));
        assertTrue(game.canCastleQueenSide(Color.BLACK));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G45: White castles queen side; the a1 rook lands on d1.
    @Test
    public void G45WhiteCastlesQueenSide() {
        setUpCastlingPosition();
        Piece king = board.getPiece(E1);
        Piece rook = board.getPiece(A1);

        game.makeMove(E1, C1);

        assertSame(king, board.getPiece(C1));
        assertSame(rook, board.getPiece(D1));
        assertTrue(board.isEmpty(E1));
        assertTrue(board.isEmpty(A1));
        assertFalse(game.canCastleKingSide(Color.WHITE));
        assertFalse(game.canCastleQueenSide(Color.WHITE));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G46: Black castles king side on its own home rank.
    @Test
    public void G46BlackCastlesKingSide() {
        setUpCastlingPosition();
        game.makeMove(A1, A2);
        Piece king = board.getPiece(E8);
        Piece rook = board.getPiece(H8);

        game.makeMove(E8, G8);

        assertSame(king, board.getPiece(G8));
        assertSame(rook, board.getPiece(F8));
        assertTrue(board.isEmpty(E8));
        assertTrue(board.isEmpty(H8));
        assertFalse(game.canCastleKingSide(Color.BLACK));
        assertFalse(game.canCastleQueenSide(Color.BLACK));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G47: Black castles queen side on its own home rank.
    @Test
    public void G47BlackCastlesQueenSide() {
        setUpCastlingPosition();
        game.makeMove(H1, H2);
        Piece king = board.getPiece(E8);
        Piece rook = board.getPiece(A8);

        game.makeMove(E8, C8);

        assertSame(king, board.getPiece(C8));
        assertSame(rook, board.getPiece(D8));
        assertTrue(board.isEmpty(E8));
        assertTrue(board.isEmpty(A8));
        assertFalse(game.canCastleKingSide(Color.BLACK));
        assertFalse(game.canCastleQueenSide(Color.BLACK));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G48: a rook that moved away and back has lost the right, so castling is rejected.
    @Test
    public void G48CastlingWithoutTheRightIsRejected() {
        setUpCastlingPosition();
        game.makeMove(H1, H2);
        game.makeMove(A8, A7);
        game.makeMove(H2, H1);
        game.makeMove(A7, A8);

        assertCastlingRejected(E1, G1);
        assertFalse(game.canCastleKingSide(Color.WHITE));
        assertTrue(game.canCastleQueenSide(Color.WHITE));
    }

    // G49: the right is still true on a hand-built board, but no rook stands on h1.
    @Test
    public void G49CastlingWithoutTheRookIsRejected() {
        setUpCastlingPositionWithoutKingSideRook();

        assertCastlingRejected(E1, G1);
    }

    // G50: the h1 square must hold the mover's rook, not just any own piece.
    @Test
    public void G50CastlingWithAnotherPieceOnTheRookSquareIsRejected() {
        setUpCastlingPositionWithoutKingSideRook();
        place(PieceType.KNIGHT, Color.WHITE, H1);

        assertCastlingRejected(E1, G1);
    }

    // G51: the square next to the king on the king side must be empty.
    @Test
    public void G51BlockedSquareNextToKingStopsKingSideCastling() {
        setUpCastlingPosition();
        place(PieceType.KNIGHT, Color.WHITE, F1);

        assertCastlingRejected(E1, G1);
    }

    // G52: the king's destination must be empty.
    @Test
    public void G52BlockedDestinationStopsCastling() {
        setUpCastlingPosition();
        place(PieceType.KNIGHT, Color.WHITE, G1);

        assertCastlingRejected(E1, G1);
    }

    // G53: b1 must be empty even though only the rook crosses it.
    @Test
    public void G53BlockedSquareOnlyTheRookCrossesStopsQueenSideCastling() {
        setUpCastlingPosition();
        place(PieceType.KNIGHT, Color.WHITE, B1);

        assertCastlingRejected(E1, C1);
    }

    // G54: the square next to the king on the queen side must be empty.
    @Test
    public void G54BlockedSquareNextToKingStopsQueenSideCastling() {
        setUpCastlingPosition();
        place(PieceType.BISHOP, Color.WHITE, D1);

        assertCastlingRejected(E1, C1);
    }

    // G55: a king in check may not castle out of it.
    @Test
    public void G55KingInCheckCannotCastle() {
        setUpCastlingPosition();
        place(PieceType.ROOK, Color.BLACK, E4);

        assertCastlingRejected(E1, G1);
    }

    // G56: the king may not cross an attacked square, even though the rook lands on it.
    @Test
    public void G56KingCannotCrossAnAttackedSquare() {
        setUpCastlingPosition();
        place(PieceType.ROOK, Color.BLACK, F4);

        assertCastlingRejected(E1, G1);
    }

    // G57: the king may not land on an attacked square.
    @Test
    public void G57KingCannotCastleOntoAnAttackedSquare() {
        setUpCastlingPosition();
        place(PieceType.ROOK, Color.BLACK, G4);

        assertCastlingRejected(E1, G1);
    }

    // G58: on the queen side, the king may not cross attacked d1.
    @Test
    public void G58KingCannotCrossAttackedSquareOnQueenSide() {
        setUpCastlingPosition();
        place(PieceType.ROOK, Color.BLACK, D4);

        assertCastlingRejected(E1, C1);
    }

    // G59: an attacked b1 does not stop castling; only the rook passes over it.
    @Test
    public void G59AttackedSquareOnlyTheRookCrossesDoesNotStopCastling() {
        setUpCastlingPosition();
        place(PieceType.ROOK, Color.BLACK, B4);
        Piece king = board.getPiece(E1);
        Piece rook = board.getPiece(A1);

        game.makeMove(E1, C1);

        assertSame(king, board.getPiece(C1));
        assertSame(rook, board.getPiece(D1));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G60: a two-file king move that does not start on e1 is not castling.
    @Test
    public void G60TwoFileKingMoveNotFromHomeSquareIsRejected() {
        place(PieceType.KING, Color.WHITE, D1);
        place(PieceType.ROOK, Color.WHITE, A1);
        place(PieceType.ROOK, Color.WHITE, H1);
        place(PieceType.KING, Color.BLACK, E8);

        assertCastlingRejected(D1, F1);
    }

    // G61: three files is never castling, even with an empty path.
    @Test
    public void G61ThreeFileKingMoveIsNotCastling() {
        setUpCastlingPosition();

        assertCastlingRejected(E1, B1);
    }

    // G62: two ranks is never castling.
    @Test
    public void G62TwoRankKingMoveIsNotCastling() {
        setUpCastlingPosition();

        assertCastlingRejected(E1, E3);
    }

    // G63: the rook on h1 must be the mover's own color.
    @Test
    public void G63CastlingWithAnEnemyRookIsRejected() {
        setUpCastlingPositionWithoutKingSideRook();
        place(PieceType.ROOK, Color.BLACK, H1);

        assertCastlingRejected(E1, G1);
    }

    // G64: two files plus one rank is not castling, even from e1.
    @Test
    public void G64TwoFilesAndOneRankIsNotCastling() {
        setUpCastlingPosition();

        assertCastlingRejected(E1, G2);
    }

    // A rejected castle changes no square and keeps the turn.
    private void assertCastlingRejected(Position from, Position to) {
        Board before = board.copy();

        assertThrows(IllegalArgumentException.class, () -> game.makeMove(from, to));

        MoveRuleAssertions.assertSameSquares(before, board);
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // Only the two kings and four rooks, all on their original squares.
    private void setUpCastlingPosition() {
        setUpCastlingPositionWithoutKingSideRook();
        place(PieceType.ROOK, Color.WHITE, H1);
    }

    // The castling setup with h1 left empty.
    private void setUpCastlingPositionWithoutKingSideRook() {
        place(PieceType.KING, Color.WHITE, E1);
        place(PieceType.ROOK, Color.WHITE, A1);
        place(PieceType.KING, Color.BLACK, E8);
        place(PieceType.ROOK, Color.BLACK, A8);
        place(PieceType.ROOK, Color.BLACK, H8);
    }

    private void place(PieceType type, Color color, Position position) {
        board.placePiece(new Piece(type, color), position);
    }
}
