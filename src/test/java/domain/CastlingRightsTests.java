package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CastlingRightsTests {
    private static final int FILE_A = 0;
    private static final int FILE_B = 1;
    private static final int FILE_D = 3;
    private static final int FILE_E = 4;
    private static final int FILE_F = 5;
    private static final int FILE_H = 7;
    private static final int RANK_1 = 0;
    private static final int RANK_2 = 1;
    private static final int RANK_4 = 3;
    private static final int RANK_5 = 4;
    private static final int RANK_7 = 6;
    private static final int RANK_8 = 7;
    private static final Position A1 = new Position(FILE_A, RANK_1);
    private static final Position A2 = new Position(FILE_A, RANK_2);
    private static final Position A4 = new Position(FILE_A, RANK_4);
    private static final Position A7 = new Position(FILE_A, RANK_7);
    private static final Position B4 = new Position(FILE_B, RANK_4);
    private static final Position B5 = new Position(FILE_B, RANK_5);
    private static final Position D1 = new Position(FILE_D, RANK_1);
    private static final Position D7 = new Position(FILE_D, RANK_7);
    private static final Position E2 = new Position(FILE_E, RANK_2);
    private static final Position E4 = new Position(FILE_E, RANK_4);
    private static final Position E5 = new Position(FILE_E, RANK_5);
    private static final Position E7 = new Position(FILE_E, RANK_7);
    private static final Position E1 = new Position(FILE_E, RANK_1);
    private static final Position F1 = new Position(FILE_F, RANK_1);
    private static final Position H1 = new Position(FILE_H, RANK_1);
    private static final Position H4 = new Position(FILE_H, RANK_4);
    private static final Position H5 = new Position(FILE_H, RANK_5);
    private static final Position A8 = new Position(FILE_A, RANK_8);
    private static final Position B8 = new Position(FILE_B, RANK_8);
    private static final Position D8 = new Position(FILE_D, RANK_8);
    private static final Position E8 = new Position(FILE_E, RANK_8);
    private static final Position H8 = new Position(FILE_H, RANK_8);

    private Board board;
    private Game game;

    @BeforeEach
    public void createGame() {
        board = new Board();
        game = new Game(board);
    }

    // G30: a new game starts with every castling right.
    @Test
    public void G30NewGameHasAllCastlingRights() {
        assertRights(true, true, true, true);
    }

    // G31: starting a new game gives every castling right.
    @Test
    public void G31StartNewGameHasAllCastlingRights() {
        game.startNewGame();

        assertRights(true, true, true, true);
    }

    // G32: a king move loses both of that color's rights.
    @Test
    public void G32KingMoveLosesBothRightsForThatColor() {
        setUpCastlingPosition();

        game.makeMove(E1, F1);

        assertRights(false, false, true, true);
    }

    // G33: moving the h1 rook loses only White's king-side right.
    @Test
    public void G33KingSideRookMoveLosesKingSideRight() {
        setUpCastlingPosition();

        game.makeMove(H1, H4);

        assertRights(false, true, true, true);
    }

    // G34: moving the a1 rook loses only White's queen-side right.
    @Test
    public void G34QueenSideRookMoveLosesQueenSideRight() {
        setUpCastlingPosition();

        game.makeMove(A1, A4);

        assertRights(true, false, true, true);
    }

    // G35: a Black king move loses both Black rights.
    @Test
    public void G35BlackKingMoveLosesBothBlackRights() {
        setUpCastlingPosition();
        game.makeMove(A1, A2);

        game.makeMove(E8, D8);

        assertRights(true, false, false, false);
    }

    // G36: moving the h8 rook loses only Black's king-side right.
    @Test
    public void G36BlackKingSideRookMoveLosesThatRight() {
        setUpCastlingPosition();
        game.makeMove(A1, A2);

        game.makeMove(H8, H5);

        assertRights(true, false, false, true);
    }

    // G37: capturing a rook on its original square loses that side's right.
    @Test
    public void G37CapturingRookOnItsSquareLosesThatRight() {
        setUpCastlingPosition();
        place(PieceType.ROOK, Color.WHITE, B8);

        game.makeMove(B8, A8);

        assertRights(true, true, true, false);
    }

    // G38: a rook that did not start on an original square changes no right.
    @Test
    public void G38OtherRookMoveKeepsAllRights() {
        setUpCastlingPosition();
        place(PieceType.ROOK, Color.WHITE, B4);

        game.makeMove(B4, B5);

        assertRights(true, true, true, true);
    }

    // G39: moving the rook back to its original square does not restore the right.
    @Test
    public void G39ReturningRookDoesNotRestoreRight() {
        setUpCastlingPosition();
        game.makeMove(H1, H4);
        game.makeMove(A8, A7);

        game.makeMove(H4, H1);

        assertRights(false, true, true, false);
    }

    // G40: a rejected king move keeps every right.
    @Test
    public void G40RejectedKingMoveKeepsAllRights() {
        setUpCastlingPosition();
        place(PieceType.ROOK, Color.BLACK, D7);

        assertThrows(IllegalArgumentException.class, () -> game.makeMove(E1, D1));

        assertRights(true, true, true, true);
    }

    // G41: starting a new game restores rights that were lost.
    @Test
    public void G41StartNewGameRestoresLostRights() {
        game.startNewGame();
        game.makeMove(E2, E4);
        game.makeMove(E7, E5);
        game.makeMove(E1, E2);

        game.startNewGame();

        assertRights(true, true, true, true);
    }

    // Only the two kings and four rooks, all on their original squares.
    private void setUpCastlingPosition() {
        place(PieceType.KING, Color.WHITE, E1);
        place(PieceType.ROOK, Color.WHITE, A1);
        place(PieceType.ROOK, Color.WHITE, H1);
        place(PieceType.KING, Color.BLACK, E8);
        place(PieceType.ROOK, Color.BLACK, A8);
        place(PieceType.ROOK, Color.BLACK, H8);
    }

    private void place(PieceType type, Color color, Position position) {
        board.placePiece(new Piece(type, color), position);
    }

    private void assertRights(boolean whiteKingSide, boolean whiteQueenSide,
            boolean blackKingSide, boolean blackQueenSide) {
        assertEquals(whiteKingSide, game.canCastleKingSide(Color.WHITE), "White king side");
        assertEquals(whiteQueenSide, game.canCastleQueenSide(Color.WHITE), "White queen side");
        assertEquals(blackKingSide, game.canCastleKingSide(Color.BLACK), "Black king side");
        assertEquals(blackQueenSide, game.canCastleQueenSide(Color.BLACK), "Black queen side");
    }
}
