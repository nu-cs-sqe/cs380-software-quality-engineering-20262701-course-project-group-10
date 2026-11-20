package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class IllegalMoveExceptionTests {
    @Test
    public void everyErrorRetainsItsCodeAndEnglishDiagnostic() {
        for (MoveError error : MoveError.values()) {
            IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                    () -> {
                        throw new IllegalMoveException(error);
                    });
            assertSame(error, ((IllegalMoveException) failure).getError());
            assertEquals(error.getDefaultMessage(), failure.getMessage());
        }
    }

    @Test
    public void nullErrorIsRejected() {
        assertThrows(NullPointerException.class, () -> {
            throw new IllegalMoveException(null);
        });
    }
}
