package ui;

import domain.Board;
import domain.Color;
import domain.Game;
import domain.Piece;
import domain.PieceType;
import domain.Position;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardClickHandlerTests {
    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 64;
    private static final int LAST_PIXEL_OF_FIRST_SQUARE = SQUARE_SIZE - 1;
    private static final int FIRST_PIXEL_OF_SECOND_SQUARE = SQUARE_SIZE;
    private static final int BOARD_PIXELS = BOARD_SIZE * SQUARE_SIZE;
    private static final int LAST_BOARD_PIXEL = BOARD_PIXELS - 1;
    private static final int PIXEL_BEFORE_BOARD = -1;
    private static final int NO_BOARD_SQUARE_SIZE = 0;
    private static final int SMALLEST_SQUARE_SIZE = 1;
    private static final int LAST_PIXEL_OF_SMALLEST_BOARD = BOARD_SIZE * SMALLEST_SQUARE_SIZE - 1;

    private static final int FILE_A = 0;
    private static final int FILE_B = 1;
    private static final int FILE_E = 4;
    private static final int FILE_H = 7;
    private static final int RANK_1 = 0;
    private static final int RANK_2 = 1;
    private static final int RANK_7 = 6;
    private static final int RANK_8 = 7;

    private static final Position A8 = new Position(FILE_A, RANK_8);
    private static final Position B7 = new Position(FILE_B, RANK_7);
    private static final Position H1 = new Position(FILE_H, RANK_1);
    private static final Position E2 = new Position(FILE_E, RANK_2);
    private static final Position E7 = new Position(FILE_E, RANK_7);

    // H01: nothing is selected after construction.
    @Test
    public void newHandlerHasNothingSelected() {
        Game game = EasyMock.createMock(Game.class);
        EasyMock.replay(game);

        BoardClickHandler handler = new BoardClickHandler(game);

        assertNull(handler.getSelected());
        EasyMock.verify(game);
    }

    // H02: null pointer boundary.
    @Test
    public void nullGameIsRejected() {
        assertThrows(NullPointerException.class, () -> new BoardClickHandler(null));
    }

    // C01: lowest pixel on both axes; the top row is rank 8.
    @Test
    public void topLeftPixelIsA8() {
        assertEquals(A8, handlerWithIdleGame().toPosition(0, 0, SQUARE_SIZE));
    }

    // C02: last pixel of the first square on both axes.
    @Test
    public void lastPixelOfTopLeftSquareIsStillA8() {
        assertEquals(A8, handlerWithIdleGame().toPosition(
                LAST_PIXEL_OF_FIRST_SQUARE, LAST_PIXEL_OF_FIRST_SQUARE, SQUARE_SIZE));
    }

    // C03: first pixel of the next square on both axes.
    @Test
    public void firstPixelOfNextSquareIsB7() {
        assertEquals(B7, handlerWithIdleGame().toPosition(
                FIRST_PIXEL_OF_SECOND_SQUARE, FIRST_PIXEL_OF_SECOND_SQUARE, SQUARE_SIZE));
    }

    // C04: highest pixel on the board on both axes.
    @Test
    public void bottomRightPixelIsH1() {
        assertEquals(H1, handlerWithIdleGame().toPosition(
                LAST_BOARD_PIXEL, LAST_BOARD_PIXEL, SQUARE_SIZE));
    }

    // C05: one pixel past the right edge of the board.
    @Test
    public void pixelPastRightEdgeIsOffBoard() {
        assertNull(handlerWithIdleGame().toPosition(BOARD_PIXELS, 0, SQUARE_SIZE));
    }

    // C06: one pixel past the bottom edge of the board.
    @Test
    public void pixelPastBottomEdgeIsOffBoard() {
        assertNull(handlerWithIdleGame().toPosition(0, BOARD_PIXELS, SQUARE_SIZE));
    }

    // C07: one pixel before the left edge of the board.
    @Test
    public void pixelBeforeLeftEdgeIsOffBoard() {
        assertNull(handlerWithIdleGame().toPosition(PIXEL_BEFORE_BOARD, 0, SQUARE_SIZE));
    }

    // C08: one pixel above the top edge of the board.
    @Test
    public void pixelAboveTopEdgeIsOffBoard() {
        assertNull(handlerWithIdleGame().toPosition(0, PIXEL_BEFORE_BOARD, SQUARE_SIZE));
    }

    // C09: zero square size; the panel is too small to draw a board.
    @Test
    public void zeroSquareSizeHasNoBoard() {
        assertNull(handlerWithIdleGame().toPosition(0, 0, NO_BOARD_SQUARE_SIZE));
    }

    // C10: smallest drawable board; its last pixel is still h1.
    @Test
    public void lastPixelOfSmallestBoardIsH1() {
        assertEquals(H1, handlerWithIdleGame().toPosition(
                LAST_PIXEL_OF_SMALLEST_BOARD, LAST_PIXEL_OF_SMALLEST_BOARD, SMALLEST_SQUARE_SIZE));
    }

    // K01: nothing selected; clicking a current-player piece selects it.
    @Test
    public void clickingOwnPieceSelectsIt() {
        BoardClickHandler handler = new BoardClickHandler(gameWithPawns(Color.WHITE));

        handler.click(E2);

        assertEquals(E2, handler.getSelected());
    }

    // A replayed mock with no expectations fails on any call, so these tests
    // also check that mapping a pixel never touches the game.
    private BoardClickHandler handlerWithIdleGame() {
        Game game = EasyMock.createMock(Game.class);
        EasyMock.replay(game);
        return new BoardClickHandler(game);
    }

    // A White pawn on e2 and a Black pawn on e7. Only reads are expected, so any
    // makeMove call fails the test unless a case records it.
    private Game gameWithPawns(Color currentPlayer) {
        Board board = new Board();
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE), E2);
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK), E7);

        Game game = EasyMock.createMock(Game.class);
        EasyMock.expect(game.getBoard()).andReturn(board).anyTimes();
        EasyMock.expect(game.getCurrentPlayer()).andReturn(currentPlayer).anyTimes();
        EasyMock.replay(game);
        return game;
    }
}
