# Position

## Contract and scope

`Position(int file, int rank)` stores one square on the chessboard. Both inputs
must be between 0 and 7, inclusive. An invalid input causes
`IllegalArgumentException`. `getFile()` and `getRank()` return the original
coordinates. The object is immutable.

`equals(Object)` compares both coordinates. It is reflexive, symmetric,
transitive, and consistent. It returns false for null and other types.
Equal positions have equal `hashCode()` results, and repeated reads are stable.
Different positions are not required to have different hash codes.

This follows the Position contract in [the setup design](../design/game-setup.md).
Chess notation parsing, movement rules, and ordering are outside this issue.

## 1. Identify inputs and outputs

Inputs are the two integer coordinates and the object passed to `equals`.
Outputs are the stored coordinates, a constructor exception, a boolean equality
result, and an integer hash code.

## 2. Select boundary clues

- Numeric intervals: test -1, 0, 1, 6, 7, and 8 for each coordinate while holding
  the other coordinate valid. Include all four corners and an interior square.
- Integer extremes: test `Integer.MIN_VALUE` and `Integer.MAX_VALUE` for each
  coordinate so validation cannot accidentally wrap or clamp invalid inputs.
- Equality cases: the same object, equal separate objects, a different file,
  a different rank, both different, null, and another type.
- Repeated observations: getters, equality, and hashes must not change state.

## 3. Test doubles and TDD plan

Position has no collaborators, so its unit tests must exercise a real Position.
Mocking Position would replace the very behavior that these tests need to check.
No real Board implementation is used by this work's tests.

Two additional contract tests use EasyMock to replace Board. A strict mock checks
one `getPiece` call with an equivalent Position. A stub returns an empty-square
result for repeated equivalent-coordinate queries. These tests exercise Position
equality in a caller without depending on the unfinished Board implementation.

The inherited Position skeleton already stores coordinates and implements the
getters. Storage tests therefore establish an existing passing baseline; no
artificial failing implementation will be introduced. For missing behavior,
commit and run failing tests before implementing bounds, equality, and hashing
in separate steps. Update the implemented status after each step.

## 4. Test cases

Each test is in `src/test/java/domain/PositionTests.java` unless stated otherwise.
The ID appears at the start of the test method name.

### Constructor and getters

For PS01-PS09, construct the listed Position and read both getters twice. The
expected output is the listed file and rank on every read.

| ID | State / constructor inputs | Expected output | Implemented? |
| --- | --- | --- | --- |
| PS01 | `(0, 0)` | file 0, rank 0 | no |
| PS02 | `(7, 0)` | file 7, rank 0 | no |
| PS03 | `(0, 7)` | file 0, rank 7 | no |
| PS04 | `(7, 7)` | file 7, rank 7 | no |
| PS05 | `(1, 3)` | file 1, rank 3 | no |
| PS06 | `(6, 3)` | file 6, rank 3 | no |
| PS07 | `(3, 1)` | file 3, rank 1 | no |
| PS08 | `(3, 6)` | file 3, rank 6 | no |
| PS09 | `(3, 4)` | file 3, rank 4 | no |

For PB01-PB09, there is no Position before construction. Each listed constructor
call must throw `IllegalArgumentException`.

| ID | State / constructor inputs | Expected output | Implemented? |
| --- | --- | --- | --- |
| PB01 | `(-1, 3)` | `IllegalArgumentException` | no |
| PB02 | `(8, 3)` | `IllegalArgumentException` | no |
| PB03 | `(Integer.MIN_VALUE, 3)` | `IllegalArgumentException` | no |
| PB04 | `(Integer.MAX_VALUE, 3)` | `IllegalArgumentException` | no |
| PB05 | `(3, -1)` | `IllegalArgumentException` | no |
| PB06 | `(3, 8)` | `IllegalArgumentException` | no |
| PB07 | `(3, Integer.MIN_VALUE)` | `IllegalArgumentException` | no |
| PB08 | `(3, Integer.MAX_VALUE)` | `IllegalArgumentException` | no |
| PB09 | `(-1, 8)` | `IllegalArgumentException` | no |

### `equals(Object)`

| ID | State / comparison | Expected output | Implemented? |
| --- | --- | --- | --- |
| PE01 | Compare `(3, 4)` to itself | true | no |
| PE02 | Compare two separate `(3, 4)` objects in both directions | true in both directions | no |
| PE03 | Compare `(0, 3)` and `(7, 3)` in both directions | false in both directions | no |
| PE04 | Compare `(3, 0)` and `(3, 7)` in both directions | false in both directions | no |
| PE05 | Compare `(0, 0)` and `(7, 7)` | false | no |
| PE06 | Compare `(3, 4)` to null | false | no |
| PE07 | Compare `(3, 4)` to the String `"3,4"` | false | no |
| PE08 | Compare three separate `(3, 4)` objects pairwise | all comparisons true | no |
| PE09 | Compare two separate `(3, 4)` objects twice and read their getters | both comparisons true; all coordinates unchanged | no |

### `hashCode()`

| ID | State / operation | Expected output | Implemented? |
| --- | --- | --- | --- |
| PH01 | Read the hash of `(3, 4)` twice, then read its getters | same hash; file 3, rank 4 | no |
| PH02 | Hash two separate `(0, 0)` objects | equal hashes | no |
| PH03 | Hash two separate `(7, 7)` objects | equal hashes | no |
| PH04 | Hash two separate `(3, 4)` objects | equal hashes | no |
| PH05 | Add two separate `(3, 4)` objects to a `HashSet`, then add `(4, 3)` | duplicate rejected; different coordinate added; size 2 | no |

### EasyMock caller contracts

These tests are in `src/test/java/domain/PositionBoardTests.java`.

| ID | Methods / state | Expected output | Implemented? |
| --- | --- | --- | --- |
| PM01 | `equals`; strict mock Board expects one `getPiece(new Position(3, 4))`; call it with a separate `(3, 4)` | expectation matches, returns null, verification succeeds | no |
| PM02 | `equals`; Board stub returns null for `(0, 0)` and `(7, 7)`; query each twice using new Position objects | all four equivalent-coordinate queries match and return null | no |
