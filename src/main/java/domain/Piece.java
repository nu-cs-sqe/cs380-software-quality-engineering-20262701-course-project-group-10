package domain;

import java.util.Objects;

public final class Piece {
    private final PieceType type;
    private final Color color;

    public Piece(PieceType type, Color color) {
        this.type = Objects.requireNonNull(type, "type");
        this.color = Objects.requireNonNull(color, "color");
    }

    public PieceType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }
}
