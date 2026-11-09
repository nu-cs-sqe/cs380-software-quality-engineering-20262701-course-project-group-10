package domain;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

	@Test
	public void PE01_equalsItself() {
		Position position = new Position(3, 4);

		assertTrue(position.equals(position));
	}

	@Test
	public void PE02_equalsMatchingCoordinatesInBothDirections() {
		Position first = new Position(3, 4);
		Position second = new Position(3, 4);

		assertTrue(first.equals(second));
		assertTrue(second.equals(first));
	}

	@Test
	public void PE03_rejectsDifferentFilesInBothDirections() {
		Position first = new Position(0, 3);
		Position second = new Position(7, 3);

		assertFalse(first.equals(second));
		assertFalse(second.equals(first));
	}

	@Test
	public void PE04_rejectsDifferentRanksInBothDirections() {
		Position first = new Position(3, 0);
		Position second = new Position(3, 7);

		assertFalse(first.equals(second));
		assertFalse(second.equals(first));
	}

	@Test
	public void PE05_rejectsBothDifferentCoordinates() {
		Position first = new Position(0, 0);
		Position second = new Position(7, 7);

		assertFalse(first.equals(second));
	}

	@Test
	public void PE06_doesNotEqualNull() {
		Position position = new Position(3, 4);

		assertFalse(position.equals(null));
	}

	@Test
	public void PE07_doesNotEqualAnotherType() {
		Position position = new Position(3, 4);

		assertFalse(position.equals("3,4"));
	}

	@Test
	public void PE08_equalityIsTransitive() {
		Position first = new Position(3, 4);
		Position second = new Position(3, 4);
		Position third = new Position(3, 4);

		assertTrue(first.equals(second));
		assertTrue(second.equals(third));
		assertTrue(first.equals(third));
	}

	@Test
	public void PE09_repeatedEqualityPreservesCoordinates() {
		Position first = new Position(3, 4);
		Position second = new Position(3, 4);

		assertTrue(first.equals(second));
		assertTrue(first.equals(second));
		assertEquals(3, first.getFile());
		assertEquals(4, first.getRank());
		assertEquals(3, second.getFile());
		assertEquals(4, second.getRank());
	}

	@Test
	public void PH01_repeatedHashPreservesCoordinates() {
		Position position = new Position(3, 4);
		int firstHash = position.hashCode();

		assertEquals(firstHash, position.hashCode());
		assertEquals(3, position.getFile());
		assertEquals(4, position.getRank());
	}

	@Test
	public void PH02_equalLowerCornersHaveEqualHashes() {
		Position first = new Position(0, 0);
		Position second = new Position(0, 0);

		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	public void PH03_equalUpperCornersHaveEqualHashes() {
		Position first = new Position(7, 7);
		Position second = new Position(7, 7);

		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	public void PH04_equalInteriorPositionsHaveEqualHashes() {
		Position first = new Position(3, 4);
		Position second = new Position(3, 4);

		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	public void PH05_hashSetRejectsDuplicateCoordinates() {
		HashSet<Position> positions = new HashSet<Position>();

		assertTrue(positions.add(new Position(3, 4)));
		assertFalse(positions.add(new Position(3, 4)));
		assertTrue(positions.add(new Position(4, 3)));
		assertEquals(2, positions.size());
	}
}
