# BoardClickHandler

## Contract and scope

`BoardClickHandler` holds the click-to-move logic for `BoardPanel` so it can be
unit tested without Swing. `BoardPanel` only forwards mouse clicks and paints.

`BoardClickHandler(Game game)` rejects a null game with `NullPointerException`.
Nothing is selected after construction. The default constructor provides
a promotion callback that returns null, so promotion is cancelled when no
choice is available.

`BoardClickHandler(Game game, Supplier<PieceType> promotionChoice)` accepts
a promotion choice callback. A null callback is rejected with
`NullPointerException`.

When a pawn reaches the last rank, the handler requests a promotion choice.
A valid choice is passed to `game.makeMove(from, to, promotion)`.
If the callback returns null, the move is cancelled without changing
the board or current player.

`toPosition(int x, int y, int squareSize)` turns a pixel inside the panel into a
board square. Pixel (0, 0) is the top-left corner, and rank 8 is drawn on the top
row. It returns `null` when the pixel is outside the 8x8 board or when
`squareSize` is 0 (the panel is too small to draw a board).

`click(Position position)` follows Use Case 2:
- Nothing selected: a square holding a current-player piece becomes selected.
  An empty square or an opponent's piece selects nothing (flow 1a).
- A square selected, same square clicked: the selection clears (flow 3a).
- A square selected, another square clicked: the handler first calls
  `game.requiresPromotion(from, to)`.
  - For a normal move, it calls `game.makeMove(from, to)`.
  - For promotion, it requests a choice and calls
    `game.makeMove(from, to, promotion)`.
  - If promotion is cancelled, no move is made.
    The selection is cleared in all cases.
- If move validation or execution throws `IllegalArgumentException`,
  the exception is passed on unchanged so `ChessFrame` can display
  the error message (flow 4a).
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
| C06 | `x = 0, y = 512, squareSize = 64` | `null` | yes |
| C07 | `x = -1, y = 0, squareSize = 64` | `null` | yes |
| C08 | `x = 0, y = -1, squareSize = 64` | `null` | yes |
| C09 | `x = 0, y = 0, squareSize = 0` | `null` | yes |
| C10 | `x = 7, y = 7, squareSize = 1` | h1 (file 7, rank 0) | yes |

### Method under test: `click(Position)`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| K01 | White to move; nothing selected; click e2 (White pawn) | `getSelected()` is e2; no `makeMove` | yes |
| K02 | White to move; nothing selected; click e4 (empty) | `getSelected()` is `null`; no `makeMove` | yes |
| K03 | White to move; nothing selected; click e7 (Black pawn) | `getSelected()` is `null`; no `makeMove` | yes |
| K04 | Black to move; nothing selected; click e7 (Black pawn) | `getSelected()` is e7; no `makeMove` | yes |
| K05 | White to move; e2 selected; click e2 | `getSelected()` is `null`; no `makeMove` | yes |
| K06 | White to move; e2 selected; click e4; `requiresPromotion()` returns false | `makeMove(e2, e4)` called once; `getSelected()` is `null` | yes |
| K07 | White to move; e2 selected; click e5; `requiresPromotion()` throws `IllegalArgumentException` | The same exception is passed on; selection is cleared; `makeMove()` is not called | yes |
| K08 | White to move; nothing selected; click `null` | `getSelected()` is `null`; no `makeMove` | yes |
| K09 | White to move; e2 selected; click `null` | `getSelected()` is still e2; no `makeMove` | yes |

### Method under test: `clearSelection()`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| S01 | e2 selected; `clearSelection()` | `getSelected()` is `null` | yes |
| S02 | nothing selected; `clearSelection()` | `getSelected()` is `null` | yes |

---

## 5. Promotion Choice Handling (Issue #35)

### Contract and scope

Promotion choices are provided through `Supplier<PieceType>`, allowing
the click handler to be tested without displaying Swing dialogs.

The handler requests a promotion choice only when
`Game.requiresPromotion(from, to)` returns true.

A valid choice completes the promotion. A null choice cancels the move.
Ordinary moves do not request a promotion choice.

### Boundary Analysis

The main cases are:

- **Promotion required:** true or false.
- **Promotion choice:** valid piece type or null.
- **Constructor:** default callback or supplied callback.
- **Result:** successful promotion, cancelled promotion, or ordinary move.
- **State:** board, current player, and selection before and after the click.

### Test Cases

#### Constructor and Default Callback

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| H03 | `BoardClickHandler(Game)` | Default constructor; a legal promotion move is selected. | Promotion is cancelled because no choice is provided; board and turn remain unchanged. | :white_check_mark: |
| H04 | `BoardClickHandler(Game, Supplier<PieceType>)` | A null promotion callback is supplied. | Throws `NullPointerException`. | :white_check_mark: |

#### Promotion Click Handling

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| K10 | `click(Position)` | White Pawn at e7; player selects e7 and e8; callback returns QUEEN. | Pawn is replaced by a White Queen at e8; turn switches to Black; selection is cleared. | :white_check_mark: |
| K11 | `click(Position)` | White Pawn at e7; player selects e7 and e8; callback returns null. | Promotion is cancelled; board and turn remain unchanged; selection is cleared. | :white_check_mark: |
| K12 | `click(Position)` | White Pawn at e2; player selects e2 and e3; promotion callback would throw if called. | Pawn moves normally; callback is never called; turn switches to Black. | :white_check_mark: |

### Notes

1. `BoardClickHandler` does not determine promotion legality.
   This responsibility belongs to `Game`.
2. Tests use a real `Game` and `Board` with stubbed promotion callbacks.
3. The Swing promotion dialog belongs to `ChessFrame` and is verified manually.
4. A cancelled promotion does not change the board or current player.

---

## 6. Ignoring clicks after the game ends (Issue #59)

### Contract and scope

Once `game.isGameOver()` is true, `click(Position)` ignores the board:
- it never selects a piece, never calls `requiresPromotion` or `makeMove`, and
  never asks the promotion callback;
- it clears any selection left over from before the game ended (for example, a
  piece was selected when the player resigned), so no highlight stays on screen;
- an off-board click (`null`) is still ignored before the game is asked anything.

`click` asks `isGameOver()` on every on-board click, so while the game is in
progress the cases above (K01 to K12) behave exactly as before. Which ending it
was (checkmate, stalemate, resignation, agreed draw) belongs to `Game`; the
handler only sees the boolean. "New Game" makes `isGameOver()` false again, and
clicks work again with no change in this class.

### Inputs and outputs

Inputs: `isGameOver()` (false or true), the selection before the click (none or a
square), and the clicked square (own piece, another square, the selected square,
or `null`). Outputs: the selection after the click, and which `Game` methods are
called.

### Catalog clues

**Boolean**: `isGameOver()` false or true; the flip from false to true between
two clicks. **Cases**: nothing selected or a square selected; clicking an own
piece, a destination, the selected square, or off the board. **Pointers**: a
`null` position.

All cases use an EasyMock `Game` with a White pawn on e2 and a Black pawn on e7,
White to move. A replayed strict mock fails on any call it was not told about,
so "no `makeMove`" is checked by the mock itself.

### Test cases

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| K13 | Game over; nothing selected; click e2 (White pawn) | `getSelected()` is `null`; no `requiresPromotion` or `makeMove` | :white_check_mark: |
| K14 | e2 selected while the game was in progress; game is now over; click e4 | `getSelected()` is `null`; no `requiresPromotion` or `makeMove` | :white_check_mark: |
| K15 | e2 selected while the game was in progress; game is now over; click e2 | `getSelected()` is `null`; no `makeMove` | :x: |
| K16 | Game over; e2 selected; click `null` | `getSelected()` is still e2; `isGameOver()` is not called (off-board check comes first) | :x: |
| K17 | Game in progress | K01 to K12 unchanged; `isGameOver()` returns false | :x: |
