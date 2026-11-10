package domain;

public final class Position {
    private static final int MAX_COORDINATE = 7;

    private final int file;
    private final int rank;

    public Position(int file, int rank) {
        if (file < 0 || file > MAX_COORDINATE || rank < 0 || rank > MAX_COORDINATE) {
            throw new IllegalArgumentException("Coordinates must be between 0 and 7");
        }
        this.file = file;
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Position)) {
            return false;
        }
        Position position = (Position) other;
        return file == position.file && rank == position.rank;
    }

    @Override
    public int hashCode() {
        return 31 * file + rank;
    }
}
