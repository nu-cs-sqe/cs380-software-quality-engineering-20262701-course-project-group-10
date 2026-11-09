package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoveValidatorTests {
	@Test
	public void V01NullBoardIsRejected() {
		assertThrows(NullPointerException.class,
				() -> new MoveValidator().isLegalMove(null,
						new Position(3, 3), new Position(3, 4)));
	}

	@Test
	public void V02NullSourceIsRejected() {
		assertThrows(NullPointerException.class,
				() -> new MoveValidator().isLegalMove(new Board(), null, new Position(3, 4)));
	}

	@Test
	public void V03NullDestinationIsRejected() {
		assertThrows(NullPointerException.class,
				() -> new MoveValidator().isLegalMove(new Board(), new Position(3, 3), null));
	}

	@Test
	public void V04AllNullArgumentsAreRejected() {
		assertThrows(NullPointerException.class,
				() -> new MoveValidator().isLegalMove(null, null, null));
	}

	@Test
	public void V05EmptySourceIsIllegal() {
		Board board = new Board();
		assertFalse(new MoveValidator().isLegalMove(board,
				new Position(3, 3), new Position(3, 4)));
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				assertTrue(board.isEmpty(new Position(file, rank)));
			}
		}
	}
}
