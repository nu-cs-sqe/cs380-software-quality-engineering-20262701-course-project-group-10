package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void PB01_rejectsFileBelowZero() {
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 3));
    }

    @Test
    public void PB02_rejectsFileAboveSeven() {
        assertThrows(IllegalArgumentException.class, () -> new Position(8, 3));
    }

    @Test
    public void PB03_rejectsMinimumIntegerFile() {
        assertThrows(IllegalArgumentException.class, () -> new Position(Integer.MIN_VALUE, 3));
    }

    @Test
    public void PB04_rejectsMaximumIntegerFile() {
        assertThrows(IllegalArgumentException.class, () -> new Position(Integer.MAX_VALUE, 3));
    }

    @Test
    public void PB05_rejectsRankBelowZero() {
        assertThrows(IllegalArgumentException.class, () -> new Position(3, -1));
    }

    @Test
    public void PB06_rejectsRankAboveSeven() {
        assertThrows(IllegalArgumentException.class, () -> new Position(3, 8));
    }

    @Test
    public void PB07_rejectsMinimumIntegerRank() {
        assertThrows(IllegalArgumentException.class, () -> new Position(3, Integer.MIN_VALUE));
    }

    @Test
    public void PB08_rejectsMaximumIntegerRank() {
        assertThrows(IllegalArgumentException.class, () -> new Position(3, Integer.MAX_VALUE));
    }

    @Test
    public void PB09_rejectsBothInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 8));
    }
}
