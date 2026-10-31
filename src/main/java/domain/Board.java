package domain;

/**
 * Skeleton so Game and the GUI can be built before the real Board exists.
 * The real implementation comes from the Board task.
 */
public class Board {
    public void setUpStartingPosition() {
    }

    /** Always empty until the real Board lands, so the GUI draws a blank board. */
    public Piece getPiece(Position position) {
        return null;
    }
}
