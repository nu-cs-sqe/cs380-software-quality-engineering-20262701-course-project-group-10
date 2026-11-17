# Design: Game End Conditions, Start Screen, and Languages

Requirements: [docs/requirements/game-end-conditions.md](../requirements/game-end-conditions.md)
Builds on: [docs/design/multiple-turns.md](multiple-turns.md)

## Goals
- Decide the game result in the domain, using the same move flow that already
  checks every move.
- Keep result detection, resignation, and draws in `Game`, so the GUI only
  reads the status and shows it.
- Keep user-visible text out of the domain, so the GUI can show it in English
  or Spanish.

## Classes

| Class | Change | Responsibility |
| --- | --- | --- |
| `GameStatus` | **New** enum | `IN_PROGRESS`, `CHECKMATE`, `STALEMATE`, `RESIGNED`, `DRAW_AGREED`. |
| `MoveError` | **New** enum | One value per reason a move or action is rejected. The GUI turns each value into translated text. |
| `IllegalMoveException` | **New** | Extends `IllegalArgumentException` and carries a `MoveError`. Existing tests that expect `IllegalArgumentException` keep passing. |
| `Game` | **Change** | Tracks the status and winner. Finds checkmate and stalemate after each move. Handles resignation and agreed draws. Rejects actions after the game ends. |
| `Messages` | **New** (`ui`) | Loads `messages_en.properties` or `messages_es.properties` for the selected `Locale` and returns text by key. Builds the turn, check, error, and result text. Has no Swing code, so it is unit tested. |
| `StartPanel` | **New** (`ui`, view) | Start screen: title, language choice, and **Start Game** button. |
| `ChessFrame` | **Change** (view) | Switches from the start screen to the board. Adds the **Language** menu, **Resign**, and **Offer Draw**. Shows the check message and the result dialog. Refreshes all text when the language changes. |
| `BoardClickHandler` | **Change** | Ignores clicks once the game is over. |
| `Main` | **Change** (view) | Opens the window on the start screen. |

## Shared signatures

Everyone codes against these. Each task stubs or mocks the parts it does not
own in its own branch.

- `enum GameStatus { IN_PROGRESS, CHECKMATE, STALEMATE, RESIGNED, DRAW_AGREED }`
- `Game.getStatus(): GameStatus`
- `Game.getWinner(): Optional<Color>`, empty while in progress and after a draw
- `Game.isGameOver(): boolean`, true when the status is not `IN_PROGRESS`
- `Game.isCurrentPlayerInCheck(): boolean`
- `Game.hasAnyLegalMove(): boolean`, for the current player
- `Game.resign(Color color): void`
- `Game.agreeDraw(): void`
- `IllegalMoveException(MoveError error)` and `IllegalMoveException.getError(): MoveError`
- `Messages(Locale locale)`, `Messages.get(String key): String`

## Game

### Finding a legal move
`makeMove` already checks a move fully on a board copy before it commits. Split
it into two private steps that share all existing checks:

1. `trialMove(from, to)`: runs every check and returns the trial board, or
   throws `IllegalMoveException`. It never changes the real board or any state.
2. `commit(...)`: copies the trial board into the real board, updates castling
   rights and the last move, passes the turn, then updates the status.

`hasAnyLegalMove()` loops over every square holding a current-player piece and
every destination square. It calls `trialMove` for each pair and returns `true`
on the first one that does not throw. Because it reuses `trialMove`, it covers
castling, en passant, and king safety without new rule code. For a pawn
reaching its last rank, it tries the move with a queen: all four choices land
on the same square, so king safety is the same for each.

At most 16 pieces × 64 squares are tried. That is fast enough after each move.

**Task 1.1 integration dependency:** the current branch supports ordinary moves
and castling. En passant and promotion remain with task #35; the search must use
their implementation in `trialMove` once it merges, including last-move state and
a queen promotion choice for trial searches. Until then, detection is limited to
the implemented rules. The deferred special-move cases are listed in
[Game BVA](../bva/Game.md#dependencies-on-other-tasks).

### Updating the status
At the end of `commit`, after the turn passes:

| Current player in check? | Has a legal move? | Status | Winner |
| --- | --- | --- | --- |
| yes | no | `CHECKMATE` | opponent of the current player |
| no | no | `STALEMATE` | none |
| yes / no | yes | `IN_PROGRESS` | none |

### Resignation and draws
- `resign(color)`: only the current player can resign, since the game is
  played on one screen. Sets `RESIGNED` and makes the other color the winner.
- `agreeDraw()`: sets `DRAW_AGREED`. The GUI asks the opponent before calling
  it. Because both players share one screen, `Game` does not store a pending
  offer.

### After the game ends
`makeMove`, `resign`, and `agreeDraw` throw
`IllegalMoveException(MoveError.GAME_OVER)` once `isGameOver()` is true. Nothing
changes. `startNewGame()` sets the status back to `IN_PROGRESS` and clears the
winner.

### Error reasons
Replace each English message in `Game` with a `MoveError`:

| `MoveError` | Today's message |
| --- | --- |
| `NO_PIECE` | "No piece on ..." |
| `NOT_YOUR_TURN` | "It is ...'s turn" |
| `ILLEGAL_MOVE` | "Illegal move" |
| `OWN_PIECE` | "Cannot capture your own piece" |
| `CAPTURE_KING` | "You cannot capture a king" |
| `KING_IN_CHECK` | "That move would leave your king in check" |
| `NO_CASTLING_RIGHT` | "You can no longer castle on that side" |
| `NO_CASTLING_ROOK` | "No rook to castle with" |
| `CASTLING_BLOCKED` | "Pieces are in the way of castling" |
| `CASTLING_OUT_OF_CHECK` | "You cannot castle out of check" |
| `CASTLING_THROUGH_ATTACK` | "Your king cannot castle through an attacked square" |
| `GAME_OVER` | new |

Add values for any en passant and promotion errors when #35 merges. The
exception keeps the English text as its message, so logs and test failures stay
readable.

## Start screen and languages

### Resource bundles
- `src/main/resources/messages_en.properties` and
  `src/main/resources/messages_es.properties`, with the same keys.
- Key names: `app.title`, `menu.game`, `menu.newGame`, `menu.resign`,
  `menu.offerDraw`, `menu.language`, `start.button`, `turn.white`,
  `turn.black`, `turn.check`, `error.<MOVE_ERROR>`, `result.checkmate`,
  `result.stalemate`, `result.resigned`, `result.drawAgreed`, plus the
  promotion dialog and confirm dialog texts.
- Java 9 and later read `.properties` bundles as UTF-8, so Spanish accents can
  be written directly.

### Messages
- `Messages` wraps `ResourceBundle.getBundle("messages", locale)`.
- `turnText(Color player, boolean inCheck)`, `errorText(MoveError error)`, and
  `resultText(GameStatus status, Optional<Color> winner)` build the full text,
  so this logic is unit tested and the Swing classes only display it.
- A missing key throws `MissingResourceException`. A test checks that both
  bundles have the same keys.

### Swing flow
- `ChessFrame` uses a `CardLayout` with two cards: `StartPanel` and the board.
  **Start Game** calls `game.startNewGame()` and shows the board card.
- The **Language** menu and the start screen choice both create a new
  `Messages` and call one `refreshTexts()` method that resets every label,
  menu item, and title.
- After each move, `ChessFrame` refreshes the turn label using
  `isCurrentPlayerInCheck()`. If `isGameOver()`, it shows the result dialog.
- **Resign** asks for confirmation, then calls `game.resign(currentPlayer)`.
  **Offer Draw** asks the opponent with a Yes/No dialog, then calls
  `game.agreeDraw()` if they accept.
- `BoardClickHandler.click` returns right away when `game.isGameOver()`.

## Design decisions
- **Status lives in `Game`, not a separate referee class.** `Game` already owns
  the turn and the move flow that `hasAnyLegalMove` reuses. A new class would
  need access to `Game`'s private checks.
- **`hasAnyLegalMove` tries moves instead of generating them.** Reusing
  `trialMove` means one place decides legality. A separate move generator could
  disagree with `makeMove`.
- **Errors are enum values, not strings.** The domain stays free of language,
  and the GUI picks the text. `IllegalMoveException` extends
  `IllegalArgumentException`, so callers and existing tests do not change.
- **No pending draw offer in `Game`.** Both players share one screen, so the
  GUI can ask and answer in one step.
- **Threefold repetition, the fifty-move rule, and insufficient material stay
  out of scope.** They need move history that `Game` does not keep yet.

## Integration tests
Integration tests use real `Game`, `Board`, and `MoveValidator` objects with no
mocks. They live in `src/test/java/integration/` and drive the game only
through public `Game` methods, as the GUI does.

| Test class | Scenarios |
| --- | --- |
| `MultipleTurnsIntegrationTests` | Several normal moves and a capture; king-side and queen-side castling; en passant; promotion; a move rejected because it leaves the king in check, followed by a legal move that still passes the turn once. |
| `GameEndIntegrationTests` | Fool's Mate (checkmate in two moves); a stalemate position reached by real moves; resignation; agreed draw; no move accepted after the game ends; New Game after a finished game. |

## BVA files to write or update
- `docs/bva/Game.md`: `getStatus`, `getWinner`, `isGameOver`,
  `isCurrentPlayerInCheck`, `hasAnyLegalMove`, `resign`, `agreeDraw`, and the
  game-over rejection in `makeMove`.
- `docs/bva/Messages.md` (new): `get`, `turnText`, `errorText`, `resultText`
  for both languages.
- `docs/bva/BoardClickHandler.md`: clicks after the game ends.
- Enums, `IllegalMoveException`, and view classes need no BVA file.

## Task split

| Main task | Sub-task | Classes |
| --- | --- | --- |
| Game End Conditions | 1.1 Checkmate and stalemate | `GameStatus`, `Game` (`trialMove`, `hasAnyLegalMove`, status update, `getStatus`, `getWinner`, `isGameOver`, `isCurrentPlayerInCheck`) |
| Game End Conditions | 1.2 Resignation, agreed draw, game over | `Game` (`resign`, `agreeDraw`, `GAME_OVER` rejection, `startNewGame` reset) |
| GUI: Start Screen, i18n, End of Game | 2 | `MoveError`, `IllegalMoveException`, `Messages`, bundles, `StartPanel`, `ChessFrame`, `BoardClickHandler`, `Main` |
| Integration Tests | 3.1 Multiple Turns | `MultipleTurnsIntegrationTests` |
| Integration Tests | 3.2 Game End Conditions | `GameEndIntegrationTests` |

Tasks 1.1, 1.2, and 2 all change `Game`. To keep merges small:
- Task 2 replaces the English messages with `MoveError` first and opens that
  part as an early PR.
- Task 1.1 owns the `makeMove` split into `trialMove` and `commit`.
- Task 1.2 only adds new methods and the game-over guard at the top of
  `makeMove`.

Pull from `main` often.
