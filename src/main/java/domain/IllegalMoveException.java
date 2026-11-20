package domain;

import java.util.Objects;

/** A rejected move with a language-independent reason for the GUI to translate. */
public class IllegalMoveException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private final MoveError error;

    public IllegalMoveException(MoveError error) {
        super(Objects.requireNonNull(error, "error").getDefaultMessage());
        this.error = error;
    }

    public MoveError getError() {
        return error;
    }
}
