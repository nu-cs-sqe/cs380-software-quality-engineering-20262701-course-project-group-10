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
}
