package domain;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class PositionBoardTests {
    @Test
    public void PM01_mockBoardAcceptsEquivalentPosition() {
        Board board = EasyMock.createStrictMock(Board.class);
        EasyMock.expect(board.getPiece(new Position(3, 4))).andReturn(null).once();
        EasyMock.replay(board);

        assertNull(board.getPiece(new Position(3, 4)));

        EasyMock.verify(board);
    }

    @Test
    public void PM02_stubBoardAcceptsRepeatedEquivalentPositions() {
        Board board = EasyMock.createMock(Board.class);
        EasyMock.expect(board.getPiece(new Position(0, 0))).andStubReturn(null);
        EasyMock.expect(board.getPiece(new Position(7, 7))).andStubReturn(null);
        EasyMock.replay(board);

        assertNull(board.getPiece(new Position(0, 0)));
        assertNull(board.getPiece(new Position(7, 7)));
        assertNull(board.getPiece(new Position(0, 0)));
        assertNull(board.getPiece(new Position(7, 7)));

        EasyMock.verify(board);
    }
}
