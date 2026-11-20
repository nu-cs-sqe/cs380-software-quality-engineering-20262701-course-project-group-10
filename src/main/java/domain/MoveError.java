package domain;

/** Stable reasons a move or game action is rejected. */
public enum MoveError {
    NO_PIECE("There is no piece on that square."),
    NOT_YOUR_TURN("That is not your piece."),
    ILLEGAL_MOVE("That piece cannot move there."),
    OWN_PIECE("You cannot capture your own piece."),
    CAPTURE_KING("You cannot capture a king."),
    KING_IN_CHECK("That move would leave your king in check."),
    NO_CASTLING_RIGHT("You can no longer castle on that side."),
    NO_CASTLING_ROOK("There is no rook to castle with."),
    CASTLING_BLOCKED("Pieces are in the way of castling."),
    CASTLING_OUT_OF_CHECK("You cannot castle out of check."),
    CASTLING_THROUGH_ATTACK("Your king cannot castle through an attacked square."),
    PROMOTION_REQUIRED("A promotion choice is required."),
    NOT_A_PAWN("Only pawns can promote."),
    NOT_LAST_RANK("The pawn has not reached the last rank."),
    INVALID_PROMOTION("Invalid promotion type."),
    GAME_OVER("The game is over.");

    private final String defaultMessage;

    MoveError(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
