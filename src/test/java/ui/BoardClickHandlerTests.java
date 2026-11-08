package ui;

import domain.Game;
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

    private static final int FILE_A = 0;
    private static final int FILE_B = 1;
    private static final int FILE_H = 7;
    private static final int RANK_1 = 0;
    private static final int RANK_7 = 6;
    private static final int RANK_8 = 7;

    private static final Position A8 = new Position(FILE_A, RANK_8);
    private static final Position B7 = new Position(FILE_B, RANK_7);
    private static final Position H1 = new Position(FILE_H, RANK_1);

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

    // A replayed mock with no expectations fails on any call, so these tests
    // also check that mapping a pixel never touches the game.
    private BoardClickHandler handlerWithIdleGame() {
        Game game = EasyMock.createMock(Game.class);
        EasyMock.replay(game);
        return new BoardClickHandler(game);
    }
}
