# Design: Multiple Turns

Requirements: [docs/requirements/multiple-turns.md](../requirements/multiple-turns.md)
Builds on: [docs/design/one-turn.md](one-turn.md)

## Goals
- Support repeated legal turns with check detection, castling, en passant, and promotion.
- Use one move flow to check king safety and commit each move.
- Keep chess rules in the domain and use Swing to collect player input.

## Classes

| Class | Responsibility |
| --- | --- |
| `Game` | Owns the turn, castling rights, and last successful move. Checks and commits each move. |
| `Board` | Stores pieces. Supplies an independent copy for testing a move and copies the accepted position back into the board the GUI already uses. |
| `MoveValidator` | Checks movement and attacks using piece geometry and board occupancy. |
| `BoardClickHandler` | Sends the selected start and destination to `Game`. Requests a promotion choice when needed. |
| `ChessFrame` | Shows the promotion dialog and move errors. |

## Shared methods

- `Board.copy()` returns a `Board` with its own square storage. The two boards can share immutable `Piece` objects.
- `Board.copyFrom(Board other)` copies the squares into the existing board. The source and destination keep separate storage.
- `MoveValidator.isSquareAttacked(Board board, Position square, Color attacker)` returns whether that color attacks the square.
- `MoveValidator.isInCheck(Board board, Color color)` locates that color's king and checks whether the other color attacks it. A board missing that king is an invalid game state.
- `Game.makeMove(Position from, Position to)` remains the entry point for moves without a promotion choice.
- `Game.requiresPromotion(Position from, Position to)` identifies a legal move that needs a promotion choice. Ordinary moves return false. An attempted promotion is checked on a board copy and throws `IllegalArgumentException` if illegal.
- `Game.makeMove(Position from, Position to, PieceType promotion)` checks the move again and requires a valid choice for promotion.

## State in Game

- Keep four castling rights, one for each color and side. A king move clears both rights for that color. A rook move from its original square or capture of that rook on its original square clears that side's right. Returning a piece or promoting a rook leaves lost rights cleared.
- Keep the last successful move as the original `Piece`, its start, and its destination. That is enough to identify a pawn that just moved two squares and confirm it still occupies its destination.
- `startNewGame()` resets the board, gives White the first turn, restores all four rights, and clears the last move.

## Move flow

1. Check the source, current player, and destination. Reject a capture of a friendly piece or either king.
2. Recognize castling and en passant before using the normal movement rule. Check the prerequisites for the selected move. For promotion, also check the chosen piece type before committing.
3. Apply the full move to a copy of the board. This includes removing the captured pawn for en passant, moving the rook for castling, or replacing the pawn for promotion.
4. Check the moving player's king on the resulting board. If it is attacked, throw `IllegalArgumentException` through the existing illegal-move path. The board, turn, castling rights, and last move retain their previous values.
5. Copy the accepted position back into the existing board, update castling rights and the last move, then switch the turn once.

## Check and castling

Use the existing move geometry and path helpers for attacks. Pawns attack the two forward diagonals even when those squares are empty. Kings attack adjacent squares. A pinned piece still counts as attacking its target. This keeps the attack check independent of `Game.makeMove`.

The player selects the king and clicks two files toward the rook on the home rank. Check the stored right, the same-color king and rook on their original squares, and the empty squares between them. Check king safety at each stage:

1. On the starting board, check the king's original square.
2. On the copy, move the king one square toward the rook and check that square.
3. Finish moving the king and rook on the copy. The normal final check verifies the king's destination.

On success, the rook ends on the square the king crossed and the turn switches once.

## En passant and promotion

- For en passant, the destination is empty and one diagonal step forward. The last move must be the opponent's two-square pawn advance, with that pawn still beside the moving pawn. Remove it from its actual square on the copy before checking the king. A successful intervening move replaces the last-move record.
- A pawn reaching its last rank is replaced with a queen, rook, bishop, or knight of the same color as part of the move. The domain checks the choice. To check whether the UI should prompt, `requiresPromotion` can test king safety with the pawn on the arrival square because all four choices occupy that same square.

## Swing flow

- `BoardClickHandler` keeps the current two-click selection. A castling request uses the ordinary two-argument `makeMove` call and `Game` moves both pieces.
- Before sending a move, the handler calls `requiresPromotion`. If true, it gets a choice through a callback supplied by `ChessFrame` via `BoardPanel` and calls the three-argument `makeMove`. The callback can be a `Supplier<PieceType>` so handler tests can provide a choice without Swing.
- Closing the dialog cancels the request before the board or turn changes. Move errors continue to use the existing dialog.

## Task split and tests

The move rules are tracked under Multiple Turns (#33).
Coverage and mutation testing are tracked under Testing Tools (#26) and apply to the project, including the Multiple Turns code.

| Issue | Owner | Work |
| --- | --- | --- |
| #34 | Naman Dua | Board copy and commit flow, attack checks, castling rights, and castling. |
| #35 | Yurou Quan | Last-move record, en passant, promotion, and the choice dialog. Both special moves use the shared move flow. |
| #27 | Mengpang Xing | JaCoCo coverage reports and Checkstyle setup. |
| #28 | Youran Ma | PIT mutation testing. |

For #34 and #35, use the shared signatures and add BVA cases for changed public
methods. Move tests that exercise king safety should place both kings
on the board. Cover an ordinary move that exposes the king, castling through an
attacked square, en passant that opens an attack, promotion cancellation, and
state after a rejected move. Check that the next legal move still works and
advances the turn once.
