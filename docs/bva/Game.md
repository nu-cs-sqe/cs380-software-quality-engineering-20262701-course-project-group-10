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
| G01 | `new Game(mockBoard)`; `startNewGame()` not called | `getBoard()` returns `mockBoard` (same reference) on both reads; no calls on the board | yes |
| G02 | `new Game(null)` | `NullPointerException` | yes |

### Method under test: `getCurrentPlayer()`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G03 | `new Game(mockBoard)`; `startNewGame()` called 0 times | `WHITE` on both reads; no calls on the board | yes |

### Method under test: `startNewGame()`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G04 | `new Game(mockBoard)`; `startNewGame()` called 1 time | `setUpStartingPosition()` called exactly once; `getCurrentPlayer()` is `WHITE` | yes |
| G05 | `new Game(mockBoard)`; `startNewGame()` called 2 times | `setUpStartingPosition()` called exactly twice; `getCurrentPlayer()` is `WHITE` | yes |
| G06 | `new Game(mockBoard)`; `startNewGame()` called 1 time | `getBoard()` still returns `mockBoard` (same reference) | yes |
| G07 | current player is `BLACK` when `startNewGame()` is called | current player resets to `WHITE` | CAN'T SET until moves exist |

---


##  `makeMove(Position from, Position to)` in Game

### Contract and scope

`makeMove(from, to)` checks, in this order: both arguments are non-null, a piece exists on `from`, the piece belongs to the current player, and `MoveValidator.isLegalMove(board, from, to)` returns true. If every check passes, it calls `board.movePiece(from, to)` exactly once and passes the turn to the other player.

A null argument throws `NullPointerException`. An empty source, a piece of the wrong color, or an illegal move throws `IllegalArgumentException`. A rejected move never changes the board or the current player.

`Game(Board board, MoveValidator validator)` injects both collaborators. `Game(Board board)` creates a default `MoveValidator`. G01–G06 use a mock Board; makeMove tests (G07–G17) use a real Board and MoveValidator.

Because moves now exist, the current player can become `BLACK`, so G07 is no longer CAN'T SET.

### 1. Identify inputs and outputs

Inputs: `from` and `to` (a real position or null); the piece on `from` (none, the current player's, or the opponent's); the current player (`WHITE` or `BLACK`); the validator's answer (legal or illegal); the number of moves made so far.
Outputs: an exception or none; whether `board.movePiece(from, to)` is called; the current player afterwards.

### 2. Select catalog clues

**Pointers** calls for null and a real `Position`, applied to `from` and `to`. **Cases** apply to the piece on `from` (none / current player's / opponent's) and to the validator's answer (true / false). **Enumeration** applies to the current player (`WHITE` / `BLACK`), and both values must appear in the tests. **Counts** call for one move and more than one move, to check that the turn alternates. The order of checks (null, empty source, turn ownership, legality) is tested by making each rejection happen before the later collaborators are called.

### 3. Choose concrete cases

Each rejection reason gets one case, and each case checks that `movePiece` is not called and the current player is unchanged. Captures are the job of `Board.movePiece` (BOARD-MOVE-10); `Game` only delegates, so captures through `Game` are checked in the integration cases.

### 4. Test cases

### Method under test: `makeMove(from, to)`

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G08 | `from` is null | `NullPointerException`; no calls on the board or validator | :white_check_mark: |
| G09 | `to` is null | `NullPointerException`; no calls on the board or validator | :white_check_mark: |
| G10 | White to move; the board has no piece on `from` | `IllegalArgumentException`; `movePiece` not called; current player is still `WHITE` | :white_check_mark: |
| G11 | White to move; the piece on `from` is Black | `IllegalArgumentException`; validator not called; `movePiece` not called; current player is still `WHITE` | :white_check_mark: |
| G12 | Black to move (after one legal White move); the piece on `from` is White | `IllegalArgumentException`; `movePiece` not called again; current player is still `BLACK` | :white_check_mark: |
| G13 | White to move; the piece on `from` is White; validator returns false | `IllegalArgumentException`; `movePiece` not called; current player is still `WHITE` | :white_check_mark: |
| G14 | White to move; the piece on `from` is White; validator returns true | `movePiece(from, to)` called exactly once; current player is `BLACK` | :white_check_mark: |
| G15 | One legal White move, then one legal Black move | `movePiece` called once per move; current player is `WHITE` again | :white_check_mark: |
| G16 | White to move; the piece on from is White; validator returns true; the piece on to is also White | IllegalArgumentException; movePiece not called; current player is still WHITE | :white_check_mark: |
| G17 | White to move; a White rook on d4 (3, 3) and a Black pawn on d6 (3, 5); White moves d4 to d6 (real `Board` and `MoveValidator`) | The White rook is on d6 and d4 is empty (the Black pawn is captured); current player is `BLACK` | :white_check_mark: |
### Method under test: `startNewGame()` (update)

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G07 | current player is `BLACK` (after one legal move) when `startNewGame()` is called | `setUpStartingPosition()` called once; current player resets to `WHITE` | :white_check_mark: |

