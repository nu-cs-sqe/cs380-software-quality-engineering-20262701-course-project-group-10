package domain;

/**
 * Skeleton so the GUI can ask the Board for pieces.
 * The real implementation (bounds checks, equals/hashCode) comes from the Position task.
 */
public final class Position {
    private final int file;
    private final int rank;

    public Position(int file, int rank) {
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
