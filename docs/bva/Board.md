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

The board has four corner positions::
- a1: (0, 0)
- h1: (7, 0)
- a8: (0, 7)
- h8: (7, 7)

These positions cover the minimum and maximum values of both file and rank. An interior position is also tested to ensure normal array access works.

Both empty and occupied squares are considered.

### Test Cases

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| BOARD-GET-01 | `getPiece(position)` | The board is empty. Query position a1 (0, 0). | Returns `null`. | :white_check_mark: |
| BOARD-GET-02 | `getPiece(position)` | A piece is placed at a1 (0, 0). | Returns the exact piece stored at a1. | :white_check_mark: |
| BOARD-GET-03 | `getPiece(position)` | A piece is placed at h1 (7, 0). | Returns the exact piece stored at h1. | :white_check_mark: |
| BOARD-GET-04 | `getPiece(position)` | A piece is placed at a8 (0, 7). | Returns the exact piece stored at a8. | :white_check_mark: |
| BOARD-GET-05 | `getPiece(position)` | A piece is placed at h8 (7, 7). | Returns the exact piece stored at h8. | :white_check_mark: |
| BOARD-GET-06 | `getPiece(position)` | A piece is placed at an interior position, such as d4 (3, 3). | Returns the exact piece stored at d4. | :white_check_mark: |

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
| BOARD-PLACE-01 | `placePiece(piece, position)` | The board is empty. Place a piece at a1 (0, 0). | The piece can be retrieved from a1 using `getPiece()`. | :white_check_mark: |
| BOARD-PLACE-02 | `placePiece(piece, position)` | The board is empty. Place a piece at h1 (7, 0). | The piece can be retrieved from h1 using `getPiece()`. | :white_check_mark: |
| BOARD-PLACE-03 | `placePiece(piece, position)` | The board is empty. Place a piece at a8 (0, 7). | The piece can be retrieved from a8 using `getPiece()`. | :white_check_mark: |
| BOARD-PLACE-04 | `placePiece(piece, position)` | The board is empty. Place a piece at h8 (7, 7). | The piece can be retrieved from h8 using `getPiece()`. | :white_check_mark: |
| BOARD-PLACE-05 | `placePiece(piece, position)` | The board is empty. Place a piece at an interior position, such as d4 (3, 3). | The piece can be retrieved from d4 using `getPiece()`. | :white_check_mark: |

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
| BOARD-EMPTY-01 | `isEmpty(position)` | The board is empty. Check a1 (0, 0). | Returns `true`. | :white_check_mark: |
| BOARD-EMPTY-02 | `isEmpty(position)` | The board is empty. Check h1 (7, 0). | Returns `true`. | :white_check_mark: |
| BOARD-EMPTY-03 | `isEmpty(position)` | The board is empty. Check a8 (0, 7). | Returns `true`. | :white_check_mark: |
| BOARD-EMPTY-04 | `isEmpty(position)` | The board is empty. Check h8 (7, 7). | Returns `true`. | :white_check_mark: |
| BOARD-EMPTY-05 | `isEmpty(position)` | A piece is placed at a1 (0, 0). Check a1. | Returns `false`. | :white_check_mark: |
| BOARD-EMPTY-06 | `isEmpty(position)` | A piece is placed at h8 (7, 7). Check h8. | Returns `false`. | :white_check_mark: |
| BOARD-EMPTY-07 | `isEmpty(position)` | A piece is placed at an interior position, such as d4 (3, 3). Check d4. | Returns `false`. | :white_check_mark: |

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
| BOARD-SETUP-01 | `setUpStartingPosition()` | A newly created empty board. | The board contains exactly 32 pieces after setup. | :white_check_mark: |
| BOARD-SETUP-02 | `setUpStartingPosition()` | A newly created empty board. | White has exactly 16 pieces. | :white_check_mark: |
| BOARD-SETUP-03 | `setUpStartingPosition()` | A newly created empty board. | Black has exactly 16 pieces. | :white_check_mark: |
| BOARD-SETUP-04 | `setUpStartingPosition()` | A newly created empty board. | White has one King, one Queen, two Rooks, two Bishops, two Knights, and eight Pawns. | :white_check_mark: |
| BOARD-SETUP-05 | `setUpStartingPosition()` | A newly created empty board. | Black has one King, one Queen, two Rooks, two Bishops, two Knights, and eight Pawns. | :white_check_mark: |
| BOARD-SETUP-06 | `setUpStartingPosition()` | A newly created empty board. | White's back rank (rank 1) has the order Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook. | :white_check_mark: |
| BOARD-SETUP-07 | `setUpStartingPosition()` | A newly created empty board. | Black's back rank (rank 8) has the order Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook. | :white_check_mark: |
| BOARD-SETUP-08 | `setUpStartingPosition()` | A newly created empty board. | All White pawns are on rank 2. | :white_check_mark: |
| BOARD-SETUP-09 | `setUpStartingPosition()` | A newly created empty board. | All Black pawns are on rank 7. | :white_check_mark: |
| BOARD-SETUP-10 | `setUpStartingPosition()` | A newly created empty board. | Ranks 3, 4, 5, and 6 are empty. | :white_check_mark: |
| BOARD-SETUP-11 | `setUpStartingPosition()` | The board contains pieces at positions outside the standard starting arrangement. | The previous pieces are cleared, and only the 32 standard starting pieces remain. | :white_check_mark: |
| BOARD-SETUP-12 | `setUpStartingPosition()` | The board has already been initialized once. Call setup again. | The board still contains exactly the same 32 starting pieces, without duplicates. | :white_check_mark: |

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

---

## 7. BVA for `movePiece(Position from, Position to)`

### Method Description

Moves the piece on `from` to `to`. If `to` holds a piece, that piece is captured, meaning it is replaced by the moving piece. `from` becomes empty.

The method only moves pieces. It does not check chess rules, because turn order and legality belong to `Game` and `MoveValidator`.

### Boundary Analysis

**1. Inputs and outputs**

- Inputs: `from` and `to` (a real position or null); the content of the `from` square (empty or occupied); the content of the `to` square (empty or occupied); whether `from` equals `to`.
- Outputs: the board state after the call (`from`, `to`, and all other squares), or an exception.

**2. Catalog clues**

- **Pointers**: null and a real `Position`, applied to `from` and `to`.
- **Cases**: `from` is empty / occupied; `to` is empty / occupied by an opposing piece; `from` equals `to` / differs from `to`.
- **Intervals** (file and rank, 0 to 7): the four corners and one interior square. Out-of-range coordinates belong to `Position`, per the scope note in section 1.
- **Output state**: the moved piece is the same object, `from` is empty, `to` holds the piece, and the other squares are unchanged.

**3. Concrete cases**

The 64 squares are not combined exhaustively (each-choice strategy). Each corner is used once as a source and once as a destination, which covers every boundary value of file and rank.

### Test Cases

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| BOARD-MOVE-01 | `movePiece(from, to)` | `from` is null; `to` is d4 (3, 3). | Throws `NullPointerException`. | :white_check_mark: |
| BOARD-MOVE-02 | `movePiece(from, to)` | `from` is d4 (3, 3); `to` is null. | Throws `NullPointerException`. | :white_check_mark: |
| BOARD-MOVE-03 | `movePiece(from, to)` | The board is empty. `from` is a1 (0, 0), `to` is a2 (0, 1). | Throws `IllegalArgumentException`. The board is still empty. | :white_check_mark: |
| BOARD-MOVE-04 | `movePiece(from, to)` | A piece is at d4 (3, 3). `from` and `to` are both d4. | Throws `IllegalArgumentException`. The piece is still at d4. | :white_check_mark: |
| BOARD-MOVE-05 | `movePiece(from, to)` | A piece is at a1 (0, 0). `to` is h8 (7, 7), empty. | The same piece object is at h8. a1 is empty. | :white_check_mark: |
| BOARD-MOVE-06 | `movePiece(from, to)` | A piece is at h8 (7, 7). `to` is a1 (0, 0), empty. | The same piece object is at a1. h8 is empty. | :white_check_mark: |
| BOARD-MOVE-07 | `movePiece(from, to)` | A piece is at h1 (7, 0). `to` is a8 (0, 7), empty. | The same piece object is at a8. h1 is empty. | :white_check_mark: |
| BOARD-MOVE-08 | `movePiece(from, to)` | A piece is at a8 (0, 7). `to` is h1 (7, 0), empty. | The same piece object is at h1. a8 is empty. | :white_check_mark: |
| BOARD-MOVE-09 | `movePiece(from, to)` | A piece is at d4 (3, 3). `to` is d5 (3, 4), empty. | The same piece object is at d5. d4 is empty. | :white_check_mark: |
| BOARD-MOVE-10 | `movePiece(from, to)` | A White piece is at d4 (3, 3) and a Black piece is at d6 (3, 5). Move d4 to d6. | d6 holds the White piece (the Black piece is gone). d4 is empty. | :white_check_mark: |
| BOARD-MOVE-11 | `movePiece(from, to)` | A piece is at a1 (0, 0) and an unrelated piece is at f6 (5, 5). Move a1 to a8 (0, 7). | The unrelated piece is still at f6. | :white_check_mark: |

### Assumptions

1. `NullPointerException` for null arguments and `IllegalArgumentException` for an empty source or `from == to` follow the style already used in `Game`. The requirements do not specify them.
2. Moving onto a square held by a piece of the same color is not tested here. `Board.movePiece` does not check colors; `Game` rejects such a move through `MoveValidator`.
3. `Board` tests use real `Piece` and `Position` objects. No mocking is needed because `Board` has no external collaborators.