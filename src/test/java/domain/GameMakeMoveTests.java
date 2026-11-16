package domain;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameMakeMoveTests {
    private static final int FILE_A = 0;
    private static final int FILE_D = 3;
    private static final int FILE_E = 4;
    private static final int FILE_F = 5;
    private static final int FILE_H = 7;
    private static final int RANK_1 = 0;
    private static final int RANK_2 = 1;
    private static final int RANK_3 = 2;
    private static final int RANK_4 = 3;
    private static final int RANK_5 = 4;
    private static final int RANK_8 = 7;
    private static final Position A1 = new Position(FILE_A, RANK_1);
    private static final Position A2 = new Position(FILE_A, RANK_2);
    private static final Position A3 = new Position(FILE_A, RANK_3);
    private static final Position A4 = new Position(FILE_A, RANK_4);
    private static final Position A8 = new Position(FILE_A, RANK_8);
    private static final Position D1 = new Position(FILE_D, RANK_1);
    private static final Position D8 = new Position(FILE_D, RANK_8);
    private static final Position E1 = new Position(FILE_E, RANK_1);
    private static final Position E2 = new Position(FILE_E, RANK_2);
    private static final Position E4 = new Position(FILE_E, RANK_4);
    private static final Position E5 = new Position(FILE_E, RANK_5);
    private static final Position E8 = new Position(FILE_E, RANK_8);
    private static final Position F1 = new Position(FILE_F, RANK_1);
    private static final Position H8 = new Position(FILE_H, RANK_8);

    private Board board;
    private Game game;

    @BeforeEach
    public void createGame() {
        board = new Board();
        game = new Game(board);
    }

    // G08: null from.
    @Test
    public void nullFromIsRejected() {
        game.startNewGame();

        assertThrows(NullPointerException.class,
                () -> game.makeMove(null, new Position(4, 3)));

        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G09: null to.
    @Test
    public void nullToIsRejected() {
        game.startNewGame();

        assertThrows(NullPointerException.class,
                () -> game.makeMove(new Position(4, 1), null));

        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G10: empty source square (e3 is empty at the start).
    @Test
    public void movingFromAnEmptySquareIsRejected() {
        game.startNewGame();

        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Position(4, 2), new Position(4, 3)));

        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G11: White to move, Black pawn selected. The move itself is a legal
    // Black pawn move, so only the turn rule can reject it.
    @Test
    public void whiteCannotMoveABlackPiece() {
        game.startNewGame();
        Piece blackPawn = board.getPiece(new Position(4, 6));

        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Position(4, 6), new Position(4, 5)));

        assertSame(blackPawn, board.getPiece(new Position(4, 6)));
        assertTrue(board.isEmpty(new Position(4, 5)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G12: Black to move, White pawn selected (d2 to d4 is legal for White).
    @Test
    public void blackCannotMoveAWhitePiece() {
        game.startNewGame();
        game.makeMove(new Position(4, 1), new Position(4, 3));
        Piece whitePawn = board.getPiece(new Position(3, 1));

        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Position(3, 1), new Position(3, 3)));

        assertSame(whitePawn, board.getPiece(new Position(3, 1)));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G13: a pawn cannot jump three squares.
    @Test
    public void illegalMoveIsRejectedAndNothingChanges() {
        game.startNewGame();
        Piece pawn = board.getPiece(new Position(4, 1));

        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Position(4, 1), new Position(4, 4)));

        assertSame(pawn, board.getPiece(new Position(4, 1)));
        assertTrue(board.isEmpty(new Position(4, 4)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G14: legal White move.
    @Test
    public void legalWhiteMoveMovesPieceAndPassesTurnToBlack() {
        game.startNewGame();
        Piece pawn = board.getPiece(new Position(4, 1));

        game.makeMove(new Position(4, 1), new Position(4, 3));

        assertSame(pawn, board.getPiece(new Position(4, 3)));
        assertTrue(board.isEmpty(new Position(4, 1)));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G15: turns alternate.
    @Test
    public void legalBlackMovePassesTurnBackToWhite() {
        game.startNewGame();
        Piece blackPawn = board.getPiece(new Position(4, 6));

        game.makeMove(new Position(4, 1), new Position(4, 3));
        game.makeMove(new Position(4, 6), new Position(4, 4));

        assertSame(blackPawn, board.getPiece(new Position(4, 4)));
        assertTrue(board.isEmpty(new Position(4, 6)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G16: a rook may not capture its own pawn.
    @Test
    public void capturingOwnPieceIsRejected() {
        Piece rook = new Piece(PieceType.ROOK, Color.WHITE);
        Piece pawn = new Piece(PieceType.PAWN, Color.WHITE);
        board.placePiece(rook, new Position(3, 3));
        board.placePiece(pawn, new Position(3, 5));

        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(new Position(3, 3), new Position(3, 5)));

        assertSame(rook, board.getPiece(new Position(3, 3)));
        assertSame(pawn, board.getPiece(new Position(3, 5)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G17: a rook captures an opposing pawn.
    @Test
    public void capturingAnOpposingPieceReplacesIt() {
        // Both kings are needed because every accepted move checks king safety.
        place(PieceType.KING, Color.WHITE, A1);
        place(PieceType.KING, Color.BLACK, H8);
        Piece rook = new Piece(PieceType.ROOK, Color.WHITE);
        board.placePiece(rook, new Position(3, 3));
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), new Position(3, 5));

        game.makeMove(new Position(3, 3), new Position(3, 5));

        assertSame(rook, board.getPiece(new Position(3, 5)));
        assertTrue(board.isEmpty(new Position(3, 3)));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G07: startNewGame after a move restores the opening position and White.
    @Test
    public void startNewGameAfterAMoveResetsBoardAndTurn() {
        game.startNewGame();
        game.makeMove(new Position(4, 1), new Position(4, 3));

        game.startNewGame();

        assertSame(PieceType.PAWN, board.getPiece(new Position(4, 1)).getType());
        assertTrue(board.isEmpty(new Position(4, 3)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G18: a king may not move onto a square the opponent attacks.
    @Test
    public void G18KingCannotMoveIntoCheck() {
        place(PieceType.KING, Color.WHITE, E1);
        place(PieceType.KING, Color.BLACK, H8);
        place(PieceType.ROOK, Color.BLACK, D8);
        Board before = board.copy();

        assertThrows(IllegalArgumentException.class, () -> game.makeMove(E1, D1));

        MoveRuleAssertions.assertSameSquares(before, board);
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G19: while in check, moving an unrelated piece is rejected.
    @Test
    public void G19MoveThatLeavesKingInCheckIsRejected() {
        setUpRookCheckOnE1();
        place(PieceType.PAWN, Color.WHITE, A2);
        Board before = board.copy();

        assertThrows(IllegalArgumentException.class, () -> game.makeMove(A2, A3));

        MoveRuleAssertions.assertSameSquares(before, board);
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G20: escaping check by moving the king.
    @Test
    public void G20KingEscapesCheckByMoving() {
        setUpRookCheckOnE1();
        Piece king = board.getPiece(E1);

        game.makeMove(E1, D1);

        assertSame(king, board.getPiece(D1));
        assertTrue(board.isEmpty(E1));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G21: escaping check by blocking the line.
    @Test
    public void G21CheckIsBlockedByAnotherPiece() {
        setUpRookCheckOnE1();
        place(PieceType.ROOK, Color.WHITE, A4);
        Piece blocker = board.getPiece(A4);

        game.makeMove(A4, E4);

        assertSame(blocker, board.getPiece(E4));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G22: escaping check by capturing the attacker.
    @Test
    public void G22CheckIsEndedByCapturingTheAttacker() {
        setUpRookCheckOnE1();
        place(PieceType.ROOK, Color.WHITE, A8);
        Piece capturer = board.getPiece(A8);

        game.makeMove(A8, E8);

        assertSame(capturer, board.getPiece(E8));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G23: a pinned piece may not leave its pin line.
    @Test
    public void G23PinnedPieceCannotLeaveThePinLine() {
        setUpRookPinnedOnE2();
        Board before = board.copy();

        assertThrows(IllegalArgumentException.class, () -> game.makeMove(E2, A2));

        MoveRuleAssertions.assertSameSquares(before, board);
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    // G24: a pinned piece may move along its pin line.
    @Test
    public void G24PinnedPieceMovesAlongThePinLine() {
        setUpRookPinnedOnE2();
        Piece pinned = board.getPiece(E2);

        game.makeMove(E2, E5);

        assertSame(pinned, board.getPiece(E5));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G25: the far end of the pin line: capturing the pinning piece.
    @Test
    public void G25PinnedPieceCapturesThePinner() {
        setUpRookPinnedOnE2();
        Piece pinned = board.getPiece(E2);

        game.makeMove(E2, E8);

        assertSame(pinned, board.getPiece(E8));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G28: after a rejected move, the next legal move works and the turn advances once.
    @Test
    public void G28LegalMoveAfterRejectionAdvancesTurnOnce() {
        place(PieceType.KING, Color.WHITE, E1);
        place(PieceType.KING, Color.BLACK, H8);
        place(PieceType.ROOK, Color.BLACK, D8);
        Piece king = board.getPiece(E1);
        assertThrows(IllegalArgumentException.class, () -> game.makeMove(E1, D1));

        game.makeMove(E1, F1);

        assertSame(king, board.getPiece(F1));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    // G29: an accepted move is copied into the same Board object the GUI draws.
    @Test
    public void G29AcceptedMoveKeepsTheSameBoardObject() {
        setUpRookCheckOnE1();

        game.makeMove(E1, D1);

        assertSame(board, game.getBoard());
    }

    // White king e1 in check from a Black rook on e8; Black king h8.
    private void setUpRookCheckOnE1() {
        place(PieceType.KING, Color.WHITE, E1);
        place(PieceType.ROOK, Color.BLACK, E8);
        place(PieceType.KING, Color.BLACK, H8);
    }

    // White rook e2 pinned to the White king e1 by a Black rook on e8; Black king h8.
    private void setUpRookPinnedOnE2() {
        setUpRookCheckOnE1();
        place(PieceType.ROOK, Color.WHITE, E2);
    }

    private void place(PieceType type, Color color, Position position) {
        board.placePiece(new Piece(type, color), position);
    }
}
