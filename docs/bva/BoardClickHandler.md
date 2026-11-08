# BoardClickHandler

## Contract and scope

`BoardClickHandler` holds the click-to-move logic for `BoardPanel` so it can be
unit tested without Swing. `BoardPanel` only forwards mouse clicks and paints.

`BoardClickHandler(Game game)` rejects a null game with `NullPointerException`.
Nothing is selected after construction.

`toPosition(int x, int y, int squareSize)` turns a pixel inside the panel into a
board square. Pixel (0, 0) is the top-left corner, and rank 8 is drawn on the top
row. It returns `null` when the pixel is outside the 8x8 board or when
`squareSize` is 0 (the panel is too small to draw a board).

`click(Position position)` follows Use Case 2:
- Nothing selected: a square holding a current-player piece becomes selected.
  An empty square or an opponent's piece selects nothing (flow 1a).
- A square selected, same square clicked: the selection clears (flow 3a).
- A square selected, another square clicked: `game.makeMove(selected, position)`
  is called once and the selection clears. If `makeMove` throws
  `IllegalArgumentException`, the selection still clears and the exception is
  passed on unchanged so `ChessFrame` can show its message (flow 4a).
- A null position (an off-board click) is ignored.

`getSelected()` returns the selected square, or `null`. `clearSelection()`
removes any selection (used by "New Game").

Move rules and turn switching belong to `Game` and `MoveValidator`, not here.
In these tests `Game` is an EasyMock mock and `Board` is the real class, so each
case can place exactly the pieces it needs.

## 1. Identify inputs and outputs

Inputs: pixel `x` and `y`, `squareSize`, the clicked `Position` (or null), the
current selection (none or a square), the piece on the clicked square (none,
current player's, opponent's), and the current player (`WHITE`, `BLACK`).
Outputs: a `Position` or null, the selection after a click, the calls made on
`Game`, and any exception passed on from `makeMove`.

## 2. Select catalog clues

**Intervals**: each pixel coordinate is valid on `[0, 8 * squareSize - 1]`, and
each square covers `[k * squareSize, (k + 1) * squareSize - 1]`. Test -1, 0, the
last pixel of a square, the first pixel of the next square, the last pixel of the
board, and one past it. **Counts**: `squareSize` 0 (no board) and 1 (smallest
board). **Cases**: the clicked square is empty, the current player's, or the
opponent's; the selection is none, the same square, or a different square; the
current player is `WHITE` or `BLACK`. **Pointers**: null game and null position.

## 3. Choose concrete cases

Use `squareSize` 64 (a 512 x 512 board) for the interval cases. Check corners of
the board and of the first square so both axes and the rank flip are covered.
For clicks, put a White pawn on e2 and a Black pawn on e7 on an otherwise empty
board. Each `Game` mock returns that board and the listed current player, and
expects no `makeMove` call unless the case says so.

## 4. Test cases

### Method under test: `BoardClickHandler(Game)`, `getSelected()`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| H01 | `new BoardClickHandler(mockGame)` | `getSelected()` is `null` | yes |
| H02 | `new BoardClickHandler(null)` | `NullPointerException` | yes |

### Method under test: `toPosition(int, int, int)`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| C01 | `x = 0, y = 0, squareSize = 64` | a8 (file 0, rank 7) | yes |
| C02 | `x = 63, y = 63, squareSize = 64` | a8 (file 0, rank 7) | yes |
| C03 | `x = 64, y = 64, squareSize = 64` | b7 (file 1, rank 6) | yes |
| C04 | `x = 511, y = 511, squareSize = 64` | h1 (file 7, rank 0) | yes |
| C05 | `x = 512, y = 0, squareSize = 64` | `null` | yes |
| C06 | `x = 0, y = 512, squareSize = 64` | `null` | no |
| C07 | `x = -1, y = 0, squareSize = 64` | `null` | no |
| C08 | `x = 0, y = -1, squareSize = 64` | `null` | no |
| C09 | `x = 0, y = 0, squareSize = 0` | `null` | no |
| C10 | `x = 7, y = 7, squareSize = 1` | h1 (file 7, rank 0) | no |

### Method under test: `click(Position)`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| K01 | White to move; nothing selected; click e2 (White pawn) | `getSelected()` is e2; no `makeMove` | no |
| K02 | White to move; nothing selected; click e4 (empty) | `getSelected()` is `null`; no `makeMove` | no |
| K03 | White to move; nothing selected; click e7 (Black pawn) | `getSelected()` is `null`; no `makeMove` | no |
| K04 | Black to move; nothing selected; click e7 (Black pawn) | `getSelected()` is e7; no `makeMove` | no |
| K05 | White to move; e2 selected; click e2 | `getSelected()` is `null`; no `makeMove` | no |
| K06 | White to move; e2 selected; click e4 | `makeMove(e2, e4)` called once; `getSelected()` is `null` | no |
| K07 | White to move; e2 selected; click e5; `makeMove` throws `IllegalArgumentException("That piece cannot move there.")` | the same exception and message are passed on; `getSelected()` is `null` | no |
| K08 | White to move; nothing selected; click `null` | `getSelected()` is `null`; no `makeMove` | no |
| K09 | White to move; e2 selected; click `null` | `getSelected()` is still e2; no `makeMove` | no |

### Method under test: `clearSelection()`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| S01 | e2 selected; `clearSelection()` | `getSelected()` is `null` | no |
| S02 | nothing selected; `clearSelection()` | `getSelected()` is `null` | no |
