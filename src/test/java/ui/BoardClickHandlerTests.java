package ui;

import domain.Game;
import domain.Position;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardClickHandlerTests {
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
        assertEquals(new Position(0, 7), handlerWithIdleGame().toPosition(0, 0, 64));
    }

    // C02: last pixel of the first square on both axes.
    @Test
    public void lastPixelOfTopLeftSquareIsStillA8() {
        assertEquals(new Position(0, 7), handlerWithIdleGame().toPosition(63, 63, 64));
    }

    // C03: first pixel of the next square on both axes.
    @Test
    public void firstPixelOfNextSquareIsB7() {
        assertEquals(new Position(1, 6), handlerWithIdleGame().toPosition(64, 64, 64));
    }

    // C04: highest pixel on the board on both axes.
    @Test
    public void bottomRightPixelIsH1() {
        assertEquals(new Position(7, 0), handlerWithIdleGame().toPosition(511, 511, 64));
    }

    // C05: one pixel past the right edge of the board.
    @Test
    public void pixelPastRightEdgeIsOffBoard() {
        assertNull(handlerWithIdleGame().toPosition(512, 0, 64));
    }

    // C06: one pixel past the bottom edge of the board.
    @Test
    public void pixelPastBottomEdgeIsOffBoard() {
        assertNull(handlerWithIdleGame().toPosition(0, 512, 64));
    }

    // A replayed mock with no expectations fails on any call, so these tests
    // also check that mapping a pixel never touches the game.
    private BoardClickHandler handlerWithIdleGame() {
        Game game = EasyMock.createMock(Game.class);
        EasyMock.replay(game);
        return new BoardClickHandler(game);
    }
}
