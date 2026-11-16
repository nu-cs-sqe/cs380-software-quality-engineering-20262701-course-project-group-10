package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CastlingRightsTests {
    private static final int FILE_A = 0;
    private static final int FILE_E = 4;
    private static final int FILE_F = 5;
    private static final int FILE_H = 7;
    private static final int RANK_1 = 0;
    private static final int RANK_4 = 3;
    private static final int RANK_8 = 7;
    private static final Position A1 = new Position(FILE_A, RANK_1);
    private static final Position E1 = new Position(FILE_E, RANK_1);
    private static final Position F1 = new Position(FILE_F, RANK_1);
    private static final Position H1 = new Position(FILE_H, RANK_1);
    private static final Position H4 = new Position(FILE_H, RANK_4);
    private static final Position A8 = new Position(FILE_A, RANK_8);
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
