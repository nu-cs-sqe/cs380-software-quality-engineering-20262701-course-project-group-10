package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTests {
    private void assertCoordinates(int file, int rank) {
        Position position = new Position(file, rank);

        assertEquals(file, position.getFile());
        assertEquals(rank, position.getRank());
        assertEquals(file, position.getFile());
        assertEquals(rank, position.getRank());
    }

    @Test
    public void PS01_storesLowerCorner() {
        assertCoordinates(0, 0);
    }

    @Test
    public void PS02_storesUpperFileLowerRank() {
        assertCoordinates(7, 0);
    }

    @Test
    public void PS03_storesLowerFileUpperRank() {
        assertCoordinates(0, 7);
    }

    @Test
    public void PS04_storesUpperCorner() {
        assertCoordinates(7, 7);
    }

    @Test
    public void PS05_storesFileAboveLowerBoundary() {
        assertCoordinates(1, 3);
    }

    @Test
    public void PS06_storesFileBelowUpperBoundary() {
        assertCoordinates(6, 3);
    }

    @Test
    public void PS07_storesRankAboveLowerBoundary() {
        assertCoordinates(3, 1);
    }

    @Test
    public void PS08_storesRankBelowUpperBoundary() {
        assertCoordinates(3, 6);
    }

    @Test
    public void PS09_storesDifferentInteriorCoordinates() {
        assertCoordinates(3, 4);
    }
}
