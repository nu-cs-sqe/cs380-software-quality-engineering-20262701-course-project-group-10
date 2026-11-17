package domain;

/** The current result of a chess game. */
public enum GameStatus {
    IN_PROGRESS,
    CHECKMATE,
    STALEMATE,
    RESIGNED,
    DRAW_AGREED
}
