# Feature: Multiple Turns

Multiple Turns is complete when players can take consecutive legal turns,
respond to check, and use castling, en passant, and promotion when allowed.
Builds on the [One Turn requirements](one-turn.md) and the
[chess game rules](game-rules.md).

This file defines the requirements for Multiple Turns (#33).

## User Story
As a chess player, I want to keep playing legal turns and use special moves,
so that I can respond to threats and play according to the rules of chess.

### Acceptance Criteria
1. Players continue to alternate turns. Each accepted move updates the board
   and the turn label once, including a move that uses a special rule.
2. The game detects when a king is attacked. It rejects a move that puts or
   leaves the moving player's king in check or attempts to capture a king.
3. The player castles by selecting their king and clicking two squares toward
   their rook. Both pieces must be on their original squares and eligible to
   castle. Every square between them must be empty. The king's start, crossing,
   and destination squares must be safe. The rook ends on the crossing square.
4. Moving the king or the original rook loses the corresponding castling
   rights. Capturing the original rook also removes that side's right.
   Returning a piece or promoting a rook leaves lost rights cleared.
5. En passant is available on the turn immediately after an opposing pawn
   advances two squares from its starting rank and lands beside the player's
   pawn. The player moves diagonally forward into the empty square the
   opposing pawn passed over, and the opposing pawn is removed.
6. When a pawn legally reaches its last rank, the game asks the player to
   choose a queen, rook, bishop, or knight. A piece of the same color replaces
   the pawn on that square as part of the move.
7. After an illegal move or cancelled promotion, the board, turn, castling
   rights, and last successful move retain their previous values. The game
   shows an error for an illegal move.
8. Starting a new game restores the starting position, White's turn, and the
   castling rights. It clears the previous move.

## Use Case 3: Play Multiple Turns
- **Actor**: Player
- **Preconditions**: A game is in progress and it is the player's turn.
- **Main Flow**:
  1. Player selects one of their pieces and clicks its destination.
  2. System checks the move against the current position and the relevant
     special move rules.
  3. System confirms that the moving player's king is safe after the move.
  4. System updates the board, records the completed move, and passes the turn.
  5. The other player takes the next turn.
- **Alternate Flows**:
  - 2a. Player castles. The system checks the castling rights, empty squares,
    and king safety along the route. It moves the king and rook together.
  - 2b. Player captures en passant. The system checks the previous move and
    includes removal of the captured pawn when evaluating the resulting board.
  - 2c. A pawn is eligible to promote. The system asks for a promotion choice.
    The chosen piece replaces the pawn when the move is accepted.
  - 2d. Player cancels the promotion dialog. The same player can choose a move.
  - 3a. The move is illegal. The system shows an error and lets the same
    player try another move.
- **Postconditions**:
  - The board reflects one complete legal move.
  - The last successful move and castling rights reflect the updated position.
  - The other player is now the current player.

## Out of Scope (later features)
- Checkmate, stalemate, and other game end conditions.
