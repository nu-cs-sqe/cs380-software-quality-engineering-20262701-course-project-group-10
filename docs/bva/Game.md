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
| G17 | White to move; a White rook on d4 (3, 3) and a Black pawn on d6 (3, 5); White king on a1 (0, 0) and Black king on h8 (7, 7), added for the king-safety check; White moves d4 to d6 (real `Board` and `MoveValidator`) | The White rook is on d6 and d4 is empty (the Black pawn is captured); current player is `BLACK` | :white_check_mark: |
### Method under test: `makeMove(from, to)`, king safety

**Contract.** After the checks above, `makeMove` also rejects capturing a king, then
tries the move on `board.copy()`. If `MoveValidator.isInCheck(copy, mover)` is true,
it throws `IllegalArgumentException`. Otherwise it commits with `board.copyFrom(copy)`
and passes the turn. A rejected move changes no square and keeps the current player.
The board object is never replaced (G06), so the GUI keeps drawing the same board.
The new checks run last, so earlier rejections (G08–G16) never reach them.

**Inputs and outputs.** Inputs: where both kings stand, enemy pieces that attack the
mover's king before or after the move, a friendly piece pinned to its king, and a
destination holding a king. Outputs: an exception or none, all 64 squares, the current
player, and the board reference.

**Catalog clues.** **Boolean**: the mover's king is attacked after the move or not.
**Cases**: the moving piece is the king, a blocker, a capturer, or a pinned piece;
the destination holds a king or not. **Intervals**: a pinned piece moving along its pin
line to the far end (capturing the pinner) versus one square off the line. **Counts**:
a rejected move followed by one legal move advances the turn exactly once.
Every case uses a real `Board` and `MoveValidator` with both kings on the board.

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G18 | White to move; White king e1 (4, 0); Black king h8 (7, 7); Black rook d8 (3, 7); White moves e1 to d1 (3, 0) | `IllegalArgumentException` (moving into check); all 64 squares unchanged; current player `WHITE` | :white_check_mark: |
| G19 | White to move; White king e1 (4, 0); Black king h8 (7, 7); Black rook e8 (4, 7); White pawn a2 (0, 1); White moves a2 to a3 (0, 2) | `IllegalArgumentException` (king left in check); all 64 squares unchanged; current player `WHITE` | :white_check_mark: |
| G20 | Same as G19; White moves e1 to d1 (3, 0) | King on d1, e1 empty; current player `BLACK` (escape by moving the king) | :white_check_mark: |
| G21 | White to move; White king e1 (4, 0); Black king h8 (7, 7); Black rook e8 (4, 7); White rook a4 (0, 3); White moves a4 to e4 (4, 3) | Rook on e4; current player `BLACK` (escape by blocking) | :white_check_mark: |
| G22 | White to move; White king e1 (4, 0); Black king h8 (7, 7); Black rook e8 (4, 7); White rook a8 (0, 7); White moves a8 to e8 | White rook on e8, Black rook gone; current player `BLACK` (escape by capturing) | :white_check_mark: |
| G23 | White to move; White king e1 (4, 0); White rook e2 (4, 1); Black rook e8 (4, 7); Black king h8 (7, 7); White moves e2 to a2 (0, 1) | `IllegalArgumentException` (pinned piece leaves the pin line); all 64 squares unchanged; current player `WHITE` | :white_check_mark: |
| G24 | Same as G23; White moves e2 to e5 (4, 4) | Rook on e5; current player `BLACK` (moving along the pin line) | :white_check_mark: |
| G25 | Same as G23; White moves e2 to e8 | White rook on e8, Black rook gone; current player `BLACK` (far end of the pin line: capturing the pinner) | :white_check_mark: |
| G26 | White to move; White king e1 (4, 0); White rook a1 (0, 0); Black king a8 (0, 7); White moves a1 to a8 | `IllegalArgumentException` (a king cannot be captured); all 64 squares unchanged; current player `WHITE` | :white_check_mark: |
| G27 | Black to move with the White king capturable | CAN'T SET: reaching it needs White to have left its king attacked, which G19 forbids, and `Game` has no way to start with Black to move | n/a |
| G28 | Same as G18; after the rejected move, White moves e1 to f1 (5, 0) | King on f1; current player `BLACK` (one turn advance after a rejection) | :white_check_mark: |
| G29 | Same as G20 | `getBoard()` returns the same `Board` object as before the move (commit copies into it) | :white_check_mark: |

### Method under test: `startNewGame()` (update)

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G07 | current player is `BLACK` (after one legal move) when `startNewGame()` is called | `setUpStartingPosition()` called once; current player resets to `WHITE` | :white_check_mark: |

### Methods under test: `canCastleKingSide(Color)`, `canCastleQueenSide(Color)`, castling rights in `makeMove` and `startNewGame`

**Contract.** `Game` keeps four castling rights: White king side, White queen side,
Black king side, Black queen side. All four are `true` after construction and after
`startNewGame()`. The rights only record whether castling is still allowed; the
castling move itself (next section) also checks the pieces and squares.

A right is lost, and stays lost for the rest of the game, when:
- that color's king moves (both of its rights are lost);
- a piece moves away from that side's original rook square (a1/h1 for White,
  a8/h8 for Black);
- a piece is captured on that original rook square.

A rejected move changes no right. Returning a piece to its original square does not
restore a right. A null color throws `NullPointerException`. Rook promotion belongs to
#35 and is CAN'T SET here.

Two methods are used instead of one method with a `boolean` side flag, because a flag
argument hides which side a call is about.

**Inputs and outputs.** Inputs: the color asked about; which piece moves (king, the
original rook, another rook); where it moves from (original square or not); whether a
piece is captured on an original rook square; whether the move is rejected; how many
moves have been made; `startNewGame()`. Outputs: the four rights.

**Catalog clues.** **Boolean**: each right is `true` or `false`. **Cases**: both colors
and both sides; king move, original-rook move, non-original-rook move, capture on an
original rook square, rejected move. **Counts**: zero moves, one move, and a move away
and back. **Pointers**: null color.

All cases use the "castling setup" unless stated: a real `Board` with only a White king
e1 (4, 0), White rooks a1 (0, 0) and h1 (7, 0), a Black king e8 (4, 7), and Black rooks
a8 (0, 7) and h8 (7, 7). White moves first.

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G30 | New `Game`; no moves | All four rights `true` | :white_check_mark: |
| G31 | `startNewGame()`; no moves | All four rights `true` | :white_check_mark: |
| G32 | Castling setup; White king e1 to f1 (5, 0) | White king side and queen side `false`; both Black rights `true` | :white_check_mark: |
| G33 | Castling setup; White rook h1 to h4 (7, 3) | White king side `false`; White queen side `true`; both Black rights `true` | :white_check_mark: |
| G34 | Castling setup; White rook a1 to a4 (0, 3) | White queen side `false`; White king side `true` | :white_check_mark: |
| G35 | Castling setup; White rook a1 to a2 (0, 1), then Black king e8 to d8 (3, 7) | Black king side and queen side `false` | :white_check_mark: |
| G36 | Castling setup; White rook a1 to a2 (0, 1), then Black rook h8 to h5 (7, 4) | Black king side `false`; Black queen side `true` | :white_check_mark: |
| G37 | Castling setup plus a White rook on b8 (1, 7); White rook b8 captures the Black rook on a8 | Black queen side `false`; Black king side `true`; both White rights `true` | :white_check_mark: |
| G38 | Castling setup plus a White rook on b4 (1, 3); White rook b4 to b5 (1, 4) | All four rights `true` (only the original rook squares count) | :white_check_mark: |
| G39 | Castling setup; White rook h1 to h4 (7, 3), Black rook a8 to a7 (0, 6), White rook h4 back to h1 | White king side still `false` | :white_check_mark: |
| G40 | Castling setup plus a Black rook on d7 (3, 6); White king e1 to d1 (3, 0) is rejected (into check) | All four rights still `true` | :white_check_mark: |
| G41 | `startNewGame()`; White pawn e2 to e4, Black pawn e7 to e5, White king e1 to e2; then `startNewGame()` again | All four rights `true` | :x: |
| G42 | `canCastleKingSide(null)` and `canCastleQueenSide(null)` | `NullPointerException` for each | :x: |
| G43 | A rook promoted from a pawn | CAN'T SET until promotion exists (#35) | n/a |
