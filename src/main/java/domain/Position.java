package domain;

public final class Position {
    private final int file;
    private final int rank;

    public Position(int file, int rank) {
        if (file < 0 || file > 7 || rank < 0 || rank > 7) {
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
}
