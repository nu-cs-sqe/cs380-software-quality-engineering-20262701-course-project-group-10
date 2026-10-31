# Game

## Contract and scope

`Game(Board board)` creates a game that owns the supplied board. A null board is
rejected with `NullPointerException`. The constructor does not touch the board;
the current player starts as `WHITE`.

`startNewGame()` calls `board.setUpStartingPosition()` exactly once per call and
sets the current player to `WHITE`. It never replaces the board.

`getBoard()` returns the same board reference given to the constructor.
`getCurrentPlayer()` returns the side to move. Neither getter calls the board.

Moves and turn switching are outside this feature, so the current player can
only be `WHITE` for now. `Board` is replaced by an EasyMock mock in these tests,
so each case can check exactly which board methods were called.

## 1. Identify inputs and outputs

Inputs: the constructor's board (a real object or null) and how many times
`startNewGame()` has been called (0, 1, or more).
Outputs: a constructed game or a constructor exception, the getter results, and
the calls made on the board.

## 2. Select catalog clues

**Pointers** calls for null and a reference to a real object, applied to the
constructor's board. **Counts** calls for zero, one, and more than one, applied
to the number of `startNewGame()` calls and therefore to the number of
`setUpStartingPosition()` calls expected on the board. **Cases** for the current
player are `WHITE` and `BLACK`, but `BLACK` cannot be reached until moves exist,
so it is CAN'T SET for this feature.

## 3. Choose concrete cases

Construct with a mock board and with null. Check the getters with zero, one, and
two `startNewGame()` calls. Read each getter twice to check that observing the
game does not change it. Each mock is created with no extra expectations, so any
unexpected board call fails the test.

## 4. Test cases

### Method under test: `Game(Board)`, `getBoard()`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G01 | `new Game(mockBoard)`; `startNewGame()` not called | `getBoard()` returns `mockBoard` (same reference) on both reads; no calls on the board | no |
| G02 | `new Game(null)` | `NullPointerException` | no |

### Method under test: `getCurrentPlayer()`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G03 | `new Game(mockBoard)`; `startNewGame()` called 0 times | `WHITE` on both reads; no calls on the board | no |

### Method under test: `startNewGame()`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G04 | `new Game(mockBoard)`; `startNewGame()` called 1 time | `setUpStartingPosition()` called exactly once; `getCurrentPlayer()` is `WHITE` | no |
| G05 | `new Game(mockBoard)`; `startNewGame()` called 2 times | `setUpStartingPosition()` called exactly twice; `getCurrentPlayer()` is `WHITE` | no |
| G06 | `new Game(mockBoard)`; `startNewGame()` called 1 time | `getBoard()` still returns `mockBoard` (same reference) | no |
| G07 | current player is `BLACK` when `startNewGame()` is called | current player resets to `WHITE` | CAN'T SET until moves exist |
