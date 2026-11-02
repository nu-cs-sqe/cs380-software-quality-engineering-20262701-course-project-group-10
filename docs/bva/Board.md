# Board — Boundary Value Analysis

## 1. Class Overview

The `Board` class represents an 8 × 8 chessboard containing 64 squares.

Each square can either contain one chess piece or be empty.

The `Board` class is responsible for:
- Retrieving a piece from a specified position.
- Placing a piece at a specified position.
- Checking whether a specified position is empty.
- Setting up the standard starting position for a new chess game.

The board uses `Position` objects to identify squares.

**Scope note:** The `Position` class is responsible for validating file and rank values. Both values must be between 0 and 7, inclusive. Therefore, invalid coordinate tests (such as -1 and 8) belong to the `Position` class rather than the `Board` class.

---

## 2. BVA for `getPiece(Position position)`

### Method Description

Returns the chess piece located at the specified position.

If the specified square is empty, the method returns `null`.

### Boundary Analysis

The board has 8 files and 8 ranks:
- Minimum valid file: 0 (a-file)
- Maximum valid file: 7 (h-file)
- Minimum valid rank: 0 (rank 1)
- Maximum valid rank: 7 (rank 8)

The important boundary positions are:
- a1: (0, 0)
- h1: (7, 0)
- a8: (0, 7)
- h8: (7, 7)

Interior positions are also tested to make sure normal array access works.

### Test Cases

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| BOARD-GET-01 | `getPiece(position)` | The board is empty. Query position a1 (0, 0). | Returns `null`. | No |
| BOARD-GET-02 | `getPiece(position)` | A piece is placed at a1 (0, 0). | Returns the exact piece stored at a1. | No |
| BOARD-GET-03 | `getPiece(position)` | A piece is placed at h1 (7, 0). | Returns the exact piece stored at h1. | No |
| BOARD-GET-04 | `getPiece(position)` | A piece is placed at a8 (0, 7). | Returns the exact piece stored at a8. | No |
| BOARD-GET-05 | `getPiece(position)` | A piece is placed at h8 (7, 7). | Returns the exact piece stored at h8. | No |
| BOARD-GET-06 | `getPiece(position)` | A piece is placed at an interior position, such as d4 (3, 3). | Returns the exact piece stored at d4. | No |

---

## 3. BVA for `placePiece(Piece piece, Position position)`

### Method Description

Places the specified chess piece at the specified position on the board.

### Boundary Analysis

The method should correctly handle positions at the edges and corners of the board, as well as an interior position.

The four corner positions are tested because they represent the extreme combinations of the minimum and maximum file and rank values.

### Test Cases

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| BOARD-PLACE-01 | `placePiece(piece, position)` | The board is empty. Place a piece at a1 (0, 0). | The piece can be retrieved from a1 using `getPiece()`. | No |
| BOARD-PLACE-02 | `placePiece(piece, position)` | The board is empty. Place a piece at h1 (7, 0). | The piece can be retrieved from h1 using `getPiece()`. | No |
| BOARD-PLACE-03 | `placePiece(piece, position)` | The board is empty. Place a piece at a8 (0, 7). | The piece can be retrieved from a8 using `getPiece()`. | No |
| BOARD-PLACE-04 | `placePiece(piece, position)` | The board is empty. Place a piece at h8 (7, 7). | The piece can be retrieved from h8 using `getPiece()`. | No |
| BOARD-PLACE-05 | `placePiece(piece, position)` | The board is empty. Place a piece at an interior position, such as d4 (3, 3). | The piece can be retrieved from d4 using `getPiece()`. | No |

---

## 4. BVA for `isEmpty(Position position)`

### Method Description

Checks whether the specified position on the board is empty.

Returns `true` if no piece is present, and `false` if a piece occupies the position.

### Boundary Analysis

The method is tested at the corner positions and an interior position.

Both empty and occupied states must be checked to verify the two possible return values.

### Test Cases

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| BOARD-EMPTY-01 | `isEmpty(position)` | The board is empty. Check a1 (0, 0). | Returns `true`. | No |
| BOARD-EMPTY-02 | `isEmpty(position)` | The board is empty. Check h1 (7, 0). | Returns `true`. | No |
| BOARD-EMPTY-03 | `isEmpty(position)` | The board is empty. Check a8 (0, 7). | Returns `true`. | No |
| BOARD-EMPTY-04 | `isEmpty(position)` | The board is empty. Check h8 (7, 7). | Returns `true`. | No |
| BOARD-EMPTY-05 | `isEmpty(position)` | A piece is placed at a1 (0, 0). Check a1. | Returns `false`. | No |
| BOARD-EMPTY-06 | `isEmpty(position)` | A piece is placed at h8 (7, 7). Check h8. | Returns `false`. | No |
| BOARD-EMPTY-07 | `isEmpty(position)` | A piece is placed at an interior position, such as d4 (3, 3). Check d4. | Returns `false`. | No |

---

## 5. BVA for `setUpStartingPosition()`

### Method Description

Clears the board and places all 32 chess pieces in their standard starting positions.

### Boundary Analysis

The setup operation must create the standard chess starting configuration.

The important conditions are:
- The board contains exactly 64 squares.
- Each player has exactly 16 pieces.
- Each player has one King, one Queen, two Rooks, two Bishops, two Knights, and eight Pawns.
- The back-rank pieces are in the correct order.
- Pawns occupy the correct ranks.
- Ranks 3 through 6 are empty.
- Reinitializing the board does not leave old pieces or create duplicates.

### Test Cases

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| BOARD-SETUP-01 | `setUpStartingPosition()` | A newly created empty board. | The board contains exactly 32 pieces after setup. | No |
| BOARD-SETUP-02 | `setUpStartingPosition()` | A newly created empty board. | White has exactly 16 pieces. | No |
| BOARD-SETUP-03 | `setUpStartingPosition()` | A newly created empty board. | Black has exactly 16 pieces. | No |
| BOARD-SETUP-04 | `setUpStartingPosition()` | A newly created empty board. | White has one King, one Queen, two Rooks, two Bishops, two Knights, and eight Pawns. | No |
| BOARD-SETUP-05 | `setUpStartingPosition()` | A newly created empty board. | Black has one King, one Queen, two Rooks, two Bishops, two Knights, and eight Pawns. | No |
| BOARD-SETUP-06 | `setUpStartingPosition()` | A newly created empty board. | White's back rank (rank 1) has the order Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook. | No |
| BOARD-SETUP-07 | `setUpStartingPosition()` | A newly created empty board. | Black's back rank (rank 8) has the order Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook. | No |
| BOARD-SETUP-08 | `setUpStartingPosition()` | A newly created empty board. | All White pawns are on rank 2. | No |
| BOARD-SETUP-09 | `setUpStartingPosition()` | A newly created empty board. | All Black pawns are on rank 7. | No |
| BOARD-SETUP-10 | `setUpStartingPosition()` | A newly created empty board. | Ranks 3, 4, 5, and 6 are empty. | No |
| BOARD-SETUP-11 | `setUpStartingPosition()` | The board contains pieces at positions outside the standard starting arrangement. | The previous pieces are cleared, and only the 32 standard starting pieces remain. | No |
| BOARD-SETUP-12 | `setUpStartingPosition()` | The board has already been initialized once. Call setup again. | The board still contains exactly the same 32 starting pieces, without duplicates. | No |

---

## 6. Additional Notes and Assumptions

1. The `Position` class validates that file and rank are within the range 0–7. Invalid coordinate tests belong to `Position` BVA.
2. `getPiece()` returns `null` when the specified square is empty.
3. `placePiece()` is verified by placing a piece and retrieving it with `getPiece()`.
4. `isEmpty()` is verified for both empty and occupied squares.
5. `setUpStartingPosition()` must clear the board before placing the standard starting pieces.
6. The initial board arrangement follows the Game Setup requirements.
7. The requirements do not specify what should happen when placing a piece on an occupied square or passing `null` arguments. These behaviors are not assumed in this analysis.
8. The `Implemented` column will be updated as the corresponding tests and implementation are completed.