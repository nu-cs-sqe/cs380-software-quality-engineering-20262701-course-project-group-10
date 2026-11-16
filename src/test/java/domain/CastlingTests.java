package domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CastlingTests {
    private static final int FILE_A = 0;
    private static final int FILE_E = 4;
    private static final int FILE_F = 5;
    private static final int FILE_G = 6;
    private static final int FILE_H = 7;
    private static final int RANK_1 = 0;
    private static final int RANK_8 = 7;
    private static final Position A1 = new Position(FILE_A, RANK_1);
    private static final Position E1 = new Position(FILE_E, RANK_1);
    private static final Position F1 = new Position(FILE_F, RANK_1);
    private static final Position G1 = new Position(FILE_G, RANK_1);
    private static final Position H1 = new Position(FILE_H, RANK_1);
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
}
