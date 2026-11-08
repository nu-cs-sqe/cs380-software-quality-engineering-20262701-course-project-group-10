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
        Game game = EasyMock.createMock(Game.class);
        EasyMock.replay(game);
        BoardClickHandler handler = new BoardClickHandler(game);

        assertEquals(new Position(0, 7), handler.toPosition(0, 0, 64));
        EasyMock.verify(game);
    }
}
