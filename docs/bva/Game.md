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
| G41 | `startNewGame()`; White pawn e2 to e4, Black pawn e7 to e5, White king e1 to e2; then `startNewGame()` again | All four rights `true` | :white_check_mark: |
| G42 | `canCastleKingSide(null)` and `canCastleQueenSide(null)` | `NullPointerException` for each | :white_check_mark: |
| G43 | A rook promoted from a pawn | CAN'T SET until promotion exists (#35) | n/a |

### Method under test: `makeMove(from, to)`, castling

**Contract.** A king moving exactly two files along its home rank, from its original
square (e1 for White, e8 for Black), is a castling attempt. It is accepted only when
all of these hold:
- the matching right is still `true` (`canCastleKingSide` / `canCastleQueenSide`);
- the mover's own rook stands on that side's original square (a right can still be
  `true` on a hand-built board with no rook there);
- every square between the king and the rook is empty (f and g king side; b, c and d
  queen side);
- the king is not in check now, does not cross an attacked square (f or d), and does
  not land on one (g or c).

When accepted, the king lands two files over, the rook lands on the square the king
crossed (f1/d1 for White, f8/d8 for Black), both of that color's rights are lost, and
the turn passes once. When rejected, `IllegalArgumentException` is thrown and the
board, turn and rights are unchanged. b1/b8 may be attacked, because only the rook
passes over it. An attacked rook does not stop castling. Any other two-square king
move stays illegal. The GUI needs no change: clicking the king, then its two-square
destination, calls `makeMove` like any other move.

**Inputs and outputs.** Inputs: color and side; the right; what stands on the rook
square; each square between king and rook; which squares the opponent attacks; where
the king starts; how far and in which direction it moves. Outputs: an exception or
none, all 64 squares, the current player, and the four rights.

**Catalog clues.** **Boolean**: right held or lost; each square empty or not; each
square attacked or not. **Cases**: both colors and both sides; rook square holding
the own rook, nothing, or an enemy rook. **Intervals**: the king moving 1, 2 or 3
files; the blocking or attacked square adjacent to the king, at the destination, or
(queen side) the b-file square only the rook crosses.

All cases use the "castling setup" from the previous section unless stated. In it
the Black rooks on a8 and h8 attack a1 and h1 down the empty files.

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| G44 | Castling setup; White e1 to g1 (6, 0) | King g1, rook f1 (5, 0), e1 and h1 empty; both White rights `false`, both Black rights `true`; current player `BLACK` (h1 rook is attacked by h8, castling still allowed) | :white_check_mark: |
| G45 | Castling setup; White e1 to c1 (2, 0) | King c1, rook d1 (3, 0), e1 and a1 empty; both White rights `false`; current player `BLACK` | :white_check_mark: |
| G46 | Castling setup; White a1 to a2 (0, 1), then Black e8 to g8 (6, 7) | King g8, rook f8 (5, 7), e8 and h8 empty; both Black rights `false`; current player `WHITE` | :white_check_mark: |
| G47 | Castling setup; White h1 to h2 (7, 1), then Black e8 to c8 (2, 7) | King c8, rook d8 (3, 7), e8 and a8 empty; both Black rights `false`; current player `WHITE` | :white_check_mark: |
| G48 | Castling setup; White h1 to h2, Black a8 to a7 (0, 6), White h2 to h1, Black a7 to a8; then White e1 to g1 | `IllegalArgumentException` (right lost, rook back on h1); board, turn and rights unchanged | :white_check_mark: |
| G49 | Castling setup without the h1 rook (right still `true`); White e1 to g1 | `IllegalArgumentException` (no rook); board, turn and rights unchanged | :white_check_mark: |
| G50 | Castling setup with a White knight on h1 instead of the rook (right still `true`); White e1 to g1 | `IllegalArgumentException` (rook square holds another piece); board, turn and rights unchanged. An enemy rook on h1 is G63 | :white_check_mark: |
| G51 | Castling setup plus a White knight on f1 (5, 0); White e1 to g1 | `IllegalArgumentException` (square next to the king blocked); board, turn and rights unchanged | :white_check_mark: |
| G52 | Castling setup plus a White knight on g1 (6, 0); White e1 to g1 | `IllegalArgumentException` (destination blocked); board, turn and rights unchanged | :white_check_mark: |
| G53 | Castling setup plus a White knight on b1 (1, 0); White e1 to c1 | `IllegalArgumentException` (b1 blocked; only the rook crosses it); board, turn and rights unchanged | :white_check_mark: |
| G54 | Castling setup plus a White bishop on d1 (3, 0); White e1 to c1 | `IllegalArgumentException` (square next to the king blocked); board, turn and rights unchanged | :white_check_mark: |
| G55 | Castling setup plus a Black rook on e4 (4, 3); White e1 to g1 | `IllegalArgumentException` (king in check); board, turn and rights unchanged | :white_check_mark: |
| G56 | Castling setup plus a Black rook on f4 (5, 3); White e1 to g1 | `IllegalArgumentException` (king crosses attacked f1); board, turn and rights unchanged | :white_check_mark: |
| G57 | Castling setup plus a Black rook on g4 (6, 3); White e1 to g1 | `IllegalArgumentException` (king lands on attacked g1); board, turn and rights unchanged | :white_check_mark: |
| G58 | Castling setup plus a Black rook on d4 (3, 3); White e1 to c1 | `IllegalArgumentException` (king crosses attacked d1); board, turn and rights unchanged | :white_check_mark: |
| G59 | Castling setup plus a Black rook on b4 (1, 3); White e1 to c1 | Accepted as G45 (b1 attacked, but the king never crosses it) | :white_check_mark: |
| G60 | White king d1 (3, 0), White rooks a1 and h1, Black king e8; White d1 to f1 (5, 0) | `IllegalArgumentException` (two-file king move not from e1); board unchanged | :white_check_mark: |
| G61 | Castling setup; White e1 to b1 (1, 0), with c1 and d1 empty | `IllegalArgumentException` (three files is never castling); board unchanged | :white_check_mark: |
| G62 | Castling setup; White e1 to e3 (4, 2) | `IllegalArgumentException` (two ranks is never castling); board unchanged | :white_check_mark: |
| G63 | Castling setup with a Black rook on h1 instead of the White one; White e1 to g1 | `IllegalArgumentException` (rook square holds an enemy rook); board, turn and rights unchanged | :white_check_mark: |
| G64 | Castling setup; White e1 to g2 (6, 1) | `IllegalArgumentException` (two files and one rank is never castling); board unchanged | :white_check_mark: |

## Checkmate and stalemate (task 1.1)

### Contract and ownership

New queries: `getStatus()`, `getWinner()`, `isGameOver()`,
`isCurrentPlayerInCheck()`, `hasAnyLegalMove()`. Initial status is IN_PROGRESS;
initial winner is Optional.empty. Queries never change the board, turn, castling
rights, result, or piece references. Status updates after a successful move and
after passing the turn. No legal move + check means CHECKMATE with the previous
mover as winner; no legal move without check means STALEMATE with no winner.
A check with an escape remains IN_PROGRESS. Construction does not inspect Board.
Invalid boards (missing king) remain invariant errors rather than draws.

`trialMove` is private, uses the existing full move checks and Board copies, and
never commits. `hasAnyLegalMove` tries current-player pieces and destinations
until a trial succeeds. Rejected candidates are skipped; unexpected invariant
errors propagate. Tests use real positions for chess behavior and strict Board
mocks for collaborator errors and read-only search boundaries.

### Catalog analysis (steps 1-3)

BVA Catalog v4.0: Boolean (check, legal reply, finished), Cases (the result enum,
both colors and supported piece kinds), Counts (zero/one/many own pieces and
legal replies), Intervals ([0,7] file/rank endpoints), Pointers (null Board is
already G02; real board references; empty/filled Optional), Searching (no match,
first match, later match), and object equality (unchanged piece references).
Each public query has no arguments, so null query inputs cannot be supplied.
Coordinates -1 and 8 are CAN'T SET here because Position rejects them first.
Both dimensions of check × legal reply are covered, including both losing colors.
RESIGNED and DRAW_AGREED are declared for the shared API but unreachable through
this task's actions; they belong to task 1.2, not fabricated production states.

### Dependencies on other tasks

- En passant and promotion (#35) are not implemented on this branch. Search
  delegates to `trialMove`, so that task must extend the shared path, including
  any last-move state. For promotion search, try a queen without changing the
  real board; all promotion choices have the same occupancy for king safety.
- Required follow-up cases after #35: en passant as the only legal reply; en
  passant rejected because it exposes the king; expired en passant; promotion
  and promotion-capture as legal replies; search preserves promotion and
  last-move state. These are **deferred dependency tests**, not passing tests
  or impossible chess positions. Do not claim full-rule end detection yet.
- Task 1.2 owns resignation, agreed draw, rejection after game over, and resetting
  status/winner in `startNewGame`. This task adds none of those actions.
- Task 2 owns MoveError/IllegalMoveException. This branch retains the existing
  IllegalArgumentException errors; trial searches catch that base class so the
  translated exception subclass can replace it without duplicated legality code.

### Step 4: cases

Coordinates below use chess notation (a1=(0,0), h8=(7,7)). Unlisted squares are
empty except for the initial-position cases. White moves first. Every row is
implemented by the method with its GE ID in GameEndTests unless noted.

| ID | Methods / state | Catalog boundary | Expected output | Implemented? |
| --- | --- | --- | --- | --- |
| GE01 | GameStatus.values() in GameStatusTests: exact five design values | enum declaration | IN_PROGRESS, CHECKMATE, STALEMATE, RESIGNED, DRAW_AGREED | yes |
| GE02 | getStatus(): new Game(mockBoard); no Board calls | cases: initial status | IN_PROGRESS on repeated reads | yes |
| GE03 | getWinner(): new Game(mockBoard); no Board calls | pointers: no result | Optional.empty on repeated reads | yes |
| GE04 | isGameOver(): new Game(mockBoard); no Board calls | boolean false | false on repeated reads | yes |
| GE05 | isCurrentPlayerInCheck(): White Ke1; Black Ka8, Re8 | check true | true; all state unchanged | yes |
| GE06 | isCurrentPlayerInCheck(): initial position, then White e2-e4 | both colors, check false | false before and after; query changes nothing | yes |
| GE07 | hasAnyLegalMove(): empty board | zero own pieces | false; unchanged | yes |
| GE08 | hasAnyLegalMove(): White Ka1, Black Kh8 | one own piece, multiple destinations | true; unchanged | yes |
| GE09 | hasAnyLegalMove(): only Black Kh8, White to move | enemy sources skipped | false; unchanged | yes |
| GE10 | hasAnyLegalMove(): White Ka1, Black Kc3, Qb3 | zero legal destinations, not in check | false; unchanged | yes |
| GE11 | hasAnyLegalMove(): White Ka1, Black Kc3, Qb2 | zero legal destinations, in check | false; unchanged | yes |
| GE12 | hasAnyLegalMove(): White Ka1, Black Kc3, Ra3 | exactly one escape b1 | true; unchanged | yes |
| GE13 | hasAnyLegalMove(): White Ka1, Bb3; Black Kc2, Ra8 | king trapped; another piece can block at a2 | true; unchanged | yes |
| GE14 | hasAnyLegalMove(): White Ka1, Rh8; Black Kc2, Ra8 | king trapped; another piece can capture a8 | true; unchanged | yes |
| GE15 | hasAnyLegalMove(): White Ka1, Rb1; Black Kc3, Qb2, Rh1 | apparent rook capture exposes king to Rh1 | false; unchanged | yes |
| GE16 | hasAnyLegalMove(): initial position queried twice | many pieces, repeated search | true twice; board, turn, rights, status, winner unchanged | yes |
| GE17 | hasAnyLegalMove(): White Kh8, Black Ka1 | last source file/rank and inward destinations | true; unchanged | yes |
| GE18 | hasAnyLegalMove(): White Kh1, Black Ka8 | source rank 0 / file 7 boundary | true; unchanged | yes |
| GE19 | hasAnyLegalMove(): White Kb2, Black Kh8 Ra8; mock Board delegates reads and trial copies to a real position | rejected candidates before a later legal destination | true; strict mock rejects any commit or state writes | yes |
| GE20 | hasAnyLegalMove(): White Ke1 Rh1, Black Ka8 | king-side castling available | true; unchanged; later real castle still works | yes |
| GE21 | hasAnyLegalMove(): query after king/rook rights lost | rights false preserved during trial searches | same four rights before and after | yes |
| GE22 | makeMove(): White Kc6 Qb6, Black Ka8; Qb6-b7 | Black checkmated | CHECKMATE; winner WHITE; current BLACK; isGameOver true; no legal move | yes |
| GE23 | makeMove(): White Ka2, Black Kc3 Qb3; Ka2-a1 then Qb3-b2 | White checkmated | CHECKMATE; winner BLACK; current WHITE; isGameOver true | yes |
| GE24 | makeMove(): White Kc6 Qb5, Black Ka8; Qb5-b6 | Black stalemated | STALEMATE; winner empty; current BLACK; isGameOver true; not in check | yes |
| GE25 | makeMove(): White Ka2, Black Kc3 Qb4; Ka2-a1 then Qb4-b3 | White stalemated | STALEMATE; winner empty; current WHITE; isGameOver true | yes |
| GE26 | makeMove(): initial position; f2-f3, e7-e5, g2-g4, Qd8-h4 | real move sequence and diagonal mate | CHECKMATE; winner BLACK | yes |
| GE27 | makeMove(): White Ke1 Ra1, Black Ke8; Ra1-a8 | check with a king escape | IN_PROGRESS; winner empty; current BLACK; in check and has legal move | yes |
| GE28 | makeMove(): initial position; e2-e4, e7-e5 | not check with legal replies | IN_PROGRESS after both moves; winner empty; isGameOver false | yes |
| GE29 | makeMove(): initial position; e2-e5 rejected | rejected move before status update | IllegalArgumentException; board, turn, rights, status, winner unchanged | yes |
| GE30 | hasAnyLegalMove(): strict mock Board throws IllegalStateException on source read | unexpected collaborator error | same error propagates; no writes | yes |
| GE31 | isCurrentPlayerInCheck(): empty board | missing king invariant | IllegalStateException; board unchanged | yes |
| GE32 | hasAnyLegalMove(): snapshot then actual move on original board | trial does not commit or consume turn | same source references before move; real move accepted once | yes |
| GE33 | hasAnyLegalMove(): mock Board has a legal source before unread later squares | continued searching / early match | true; later source squares never read | yes |
| GE34 | hasAnyLegalMove(): mock Board throws IllegalStateException while copying a legal trial | unexpected failure inside trial | same exception propagates; no commit | yes |
| GE35 | hasAnyLegalMove(): White Ka1 Ph2; Black Kc3 Qb3 | trapped king with a quiet pawn reply | true; unchanged | yes |
| GE36 | hasAnyLegalMove(): White Ka1; Black Kc4 Qb2 | exactly one escape, capturing the unprotected checker on b2 | true; unchanged; real Kxb2 still succeeds | yes |
| GE37 | makeMove(): White Ka2 Ph2; Black Kc3 Qb4; Ka2-a1 then Qb4-b3 | king trapped, but pawn can advance | IN_PROGRESS, no winner; not in check; legal reply exists | yes |

### Mocked future-reply contract

These tests live in `GameEndReplyContractTests`. An EasyMock partial mock replaces
only `hasAnyLegalMove()`; `makeMove`, the board-copy and commit paths, check
detection, and status/winner queries run normally. A reported reply stands in for
an eventual special-move reply, without pretending to execute that rule.
The fixtures deliberately have no ordinary reply. Reporting true is a synthetic
search contract, not a claim that an en passant or promotion move exists there.
The real-board special-move integration cases above remain deferred.

| ID | Methods / state | Catalog boundary | Expected output | Implemented? |
| --- | --- | --- | --- | --- |
| GE38 | makeMove: White Kc6 Qb6, Black Ka8; Qb6-b7; mocked search reports true once | checked + simulated future reply available | IN_PROGRESS, no winner, not over; real Black king in check; only intended queen move committed | yes |
| GE39 | makeMove: White Kc6 Qb5, Black Ka8; Qb5-b6; mocked search reports true once | not checked + simulated future reply available | IN_PROGRESS, no winner, not over; real Black king not in check; only intended queen move committed | yes |
| GE40 | GE38 board and move; mocked search reports false once | checked + no reported reply | CHECKMATE, winner WHITE, over; only intended queen move committed | yes |
| GE41 | GE39 board and move; mocked search reports false once | not checked + no reported reply | STALEMATE, no winner, over; only intended queen move committed | yes |

---

## En Passant and Pawn Promotion (Issue #35)

### Contract and Scope

`Game` records the last successful move to support en passant.

En passant is allowed only immediately after an opponent's pawn moves
two squares and lands next to the current player's pawn. The capturing
pawn moves diagonally forward to an empty square, and the opponent's
pawn is removed from its original square.

Pawn promotion occurs when a pawn reaches the last rank. The player
must choose QUEEN, ROOK, BISHOP, or KNIGHT. The promoted piece keeps
the original pawn's color.

Both special moves must preserve king safety. Rejected moves must not
change the board, current player, castling rights, game status, or
last successful move.

### Boundary Analysis

The main boundaries are:

- **Last move:** no previous move, valid previous move, expired previous move.
- **Pawn movement:** one-square advance versus two-square advance.
- **Color:** WHITE and BLACK.
- **Position:** promotion at rank 7 for WHITE and rank 0 for BLACK.
- **Promotion type:** QUEEN, ROOK, BISHOP, KNIGHT, KING, PAWN, and null.
- **Move result:** legal, illegal, or cancelled.
- **King safety:** safe after the move or left in check.

Invalid file/rank values are handled by `Position` and are not
retested here.

### Test Cases

#### 1. Last Successful Move

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| EP-01 | `makeMove()` | A new game; no previous move. | No en passant opportunity exists. | Covered by EP-05 and EP-06 |
| EP-02 | `makeMove()` | A legal move is completed. | The move is recorded as the last successful move. | Covered by EP-06 and EP-07 |
| EP-03 | `makeMove()` | Two legal moves are completed. | Only the most recent move determines en passant eligibility. | Covered by EP-10 |
| EP-04 | `makeMove()` | An illegal move is attempted after a legal move. | The last successful move remains unchanged. | :white_check_mark: |
| EP-05 | `startNewGame()` | A previous move exists. | The previous move is cleared. | :white_check_mark: |

#### 2. En Passant

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| EP-06 | `makeMove()` | White pawn at e5; Black pawn just moved d7 to d5. White moves e5 to d6. | White pawn moves to d6; Black pawn at d5 is removed; turn switches. | :white_check_mark: |
| EP-07 | `makeMove()` | Black pawn at e4; White pawn just moved d2 to d4. Black moves e4 to d3. | Black pawn moves to d3; White pawn at d4 is removed; turn switches. | :white_check_mark: |
| EP-08 | `makeMove()` | Opponent's adjacent pawn moved only one square. | En passant is rejected; game state unchanged. | :white_check_mark: |
| EP-09 | `makeMove()` | Opponent's pawn moved two squares but is not adjacent. | En passant is rejected; game state unchanged. | :white_check_mark: |
| EP-10 | `makeMove()` | Opponent's pawn moved two squares, but another successful move occurred afterward. | En passant is rejected because the opportunity expired. | :white_check_mark: |
| EP-11 | `makeMove()` | The previous move was made by a non-pawn piece. | En passant is rejected. | Covered by EP-10 if the intervening move is a non-pawn move |
| EP-12 | `makeMove()` | The diagonal destination is occupied. | The move follows normal capture rules; it is not treated as en passant. | :white_check_mark: |
| EP-13 | `makeMove()` | En passant would expose the moving player's King to attack. | Throws `IllegalArgumentException`; all game state remains unchanged. | :white_check_mark: |
| EP-14 | `makeMove()` | A legal en passant move is completed. | The move is recorded and the turn switches exactly once. | Implemented in EP-06 and EP-07 |
| EP-15 | `makeMove()` | The pawn from the last two-square advance has been replaced by another Pawn object. | En passant is rejected; board and turn remain unchanged. | :white_check_mark: |

#### 3. Pawn Promotion

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| PR-01 | `requiresPromotion()` | White pawn can legally move from e7 to e8. | Returns `true`. | :white_check_mark: |
| PR-02 | `requiresPromotion()` | Black pawn can legally move from e2 to e1. | Returns `true`. | :white_check_mark: |
| PR-03 | `requiresPromotion()` | A legal pawn move does not reach the last rank. | Returns `false`. | :white_check_mark: |
| PR-04 | `requiresPromotion()` | An attempted promotion move is illegal. | Throws `IllegalArgumentException`; state unchanged. | :white_check_mark: |
| PR-05 | `makeMove(from, to, promotion)` | White pawn reaches rank 7; promotion is QUEEN. | White QUEEN replaces the pawn. | :white_check_mark: |
| PR-06 | `makeMove(from, to, promotion)` | Black pawn reaches rank 0; promotion is QUEEN. | Black QUEEN replaces the pawn. | :white_check_mark: |
| PR-07 | `makeMove(from, to, promotion)` | Pawn reaches the last rank; promotion is ROOK. | ROOK of the same color replaces the pawn. | :white_check_mark: |
| PR-08 | `makeMove(from, to, promotion)` | Pawn reaches the last rank; promotion is BISHOP. | BISHOP of the same color replaces the pawn. | :white_check_mark: |
| PR-09 | `makeMove(from, to, promotion)` | Pawn reaches the last rank; promotion is KNIGHT. | KNIGHT of the same color replaces the pawn. | :white_check_mark: |
| PR-10 | `makeMove(from, to, promotion)` | Pawn reaches the last rank; promotion is KING. | Throws `IllegalArgumentException`; state unchanged. | :white_check_mark: |
| PR-11 | `makeMove(from, to, promotion)` | Pawn reaches the last rank; promotion is PAWN. | Throws `IllegalArgumentException`; state unchanged. | :white_check_mark: |
| PR-12 | `makeMove(from, to, promotion)` | Pawn reaches the last rank; promotion is null. | Throws `NullPointerException`; state unchanged. | :white_check_mark: |
| PR-13 | `makeMove(from, to, promotion)` | White pawn captures an opponent's piece on the last rank. | The opponent's piece is removed and the pawn is promoted. | :white_check_mark: |
| PR-14 | `makeMove(from, to, promotion)` | Promotion would leave the moving player's King in check. | Throws `IllegalArgumentException`; state unchanged. | :white_check_mark: |
| PR-15 | `makeMove(from, to)` | Pawn reaches the last rank without a promotion choice. | Move is rejected; the pawn is not silently promoted. | :white_check_mark: |
| PR-19 | `makeMove(from, to, promotion)` | A non-pawn piece attempts promotion. | Throws `IllegalArgumentException`; board and turn remain unchanged. | :white_check_mark: |
| PR-20 | `makeMove(from, to, promotion)` | A pawn makes a legal move without reaching the last rank but specifies a promotion type. | Throws `IllegalArgumentException`; board and turn remain unchanged. | :white_check_mark: |

#### 4. GUI Promotion

| ID | Method Under Test | State of System Under Test | Expected Output | Implemented |
|---|---|---|---|---|
| PR-16 | `BoardClickHandler.click()` | A legal pawn promotion move is selected through the board UI. | Requests a promotion choice and passes it to `Game`. | :white_check_mark: |
| PR-17 | `BoardClickHandler.click()` | The promotion-choice callback returns null to simulate cancellation. | No move occurs; board and turn remain unchanged. | :white_check_mark: |
| PR-18 | `BoardClickHandler.click()` | An ordinary legal move is selected. | No promotion dialog is requested. | :white_check_mark: |

### Notes

1. `Game` owns the special-move rules and last-move information.
2. `Board` continues to store and move pieces without validating chess rules.
3. `Piece` remains immutable; promotion replaces the pawn with a new `Piece`.
4. En passant and promotion use the existing board-copy and king-safety checks.
5. GUI tests may use a mocked or stubbed promotion-choice callback.
6. The Implemented column records completed tests and cases covered by other tests.
## Internationalization error contract

Issue #58 replaces each English rule rejection with `IllegalMoveException` and
a stable `MoveError`. Existing IllegalArgumentException callers remain compatible.
Null/invariant failures retain their existing exception types. The strengthened
GameMakeMoveTests, CastlingTests, and GamePromotionTests assert the precise error
codes and continue checking that rejected moves leave state unchanged. Translation
is tested separately in MessagesTests; Game never selects a display language.

## Resignation and agreed draw stubs (Issue #59, until #54 merges)

**Contract.** Temporary versions of `resign(Color color)` and `agreeDraw()` from
`docs/design/game-end-conditions.md`, so the Resign and Offer Draw menu items can
be built and tested by hand before #54 lands. `resign(color)` sets the status to
`RESIGNED` and makes the other color the winner. `agreeDraw()` sets the status to
`DRAW_AGREED` with no winner. Both only cover the happy path. Rejecting a call after
the game is over, "only the current player may resign", and null checks belong to
#54, whose version and BVA replace this section. The `GR` prefix keeps these IDs
apart from #54's `GE` rows.

**Inputs and outputs.** Inputs: the resigning color. Outputs: `getStatus()`,
`getWinner()`, and `isGameOver()`.

**Catalog clues.** **Cases**: White or Black resigns; a draw is agreed.

| ID | State of the system | Expected output | Implemented? |
| --- | --- | --- | --- |
| GR01 | New game, White to move; `resign(WHITE)` | Status `RESIGNED`; winner `BLACK`; `isGameOver()` true | :x: |
| GR02 | New game, White plays e2 to e4, Black to move; `resign(BLACK)` | Status `RESIGNED`; winner `WHITE` | :x: |
| GR03 | New game, White to move; `agreeDraw()` | Status `DRAW_AGREED`; no winner; `isGameOver()` true | :x: |
