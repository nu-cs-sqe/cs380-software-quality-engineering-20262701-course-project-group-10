# Feature: One Turn of the Game

One Turn is complete when the current player can move one of their pieces according to that piece's
movement rule, the move (or capture) is shown on the board, and the turn passes to the other player.

This file is the source text for the "One Turn" item on the GitHub Project board.

## User Story
As a chess player, I want to move one of my pieces on my turn, so that the game can progress and my
opponent can take their turn.

### Acceptance Criteria
1. A player can only move a piece of their own color.
2. A move is rejected if the start square is empty.
3. A move is rejected if the destination holds a piece of the player's own color.
4. A move is rejected if it does not follow that piece's movement rule ([game-rules.md §4](game-rules.md#4-piece-movement)).
5. Rook, Bishop, and Queen cannot jump over pieces. Knights can.
6. Moving onto an opponent's piece captures it (it is removed from the board).
7. After a legal move, the current player switches (White → Black → White).
8. After an illegal move, the board and the current player are unchanged, and the player sees an error message.
9. The GUI shows whose turn it is.

## Use Case 2: Make a Move
- **Actor**: Player
- **Preconditions**: A game has been set up (Use Case 1) and it is the player's turn.
- **Main Flow**:
  1. Player clicks a square holding one of their own pieces.
  2. System highlights the selected square.
  3. Player clicks a destination square.
  4. System checks that the move follows the piece's movement rule.
  5. System moves the piece. If an opponent's piece was on the destination, it is captured.
  6. System switches the current player and updates the board and the turn label.
- **Alternate Flows**:
  - 1a. Player clicks an empty square or an opponent's piece.
    1. System does not select anything. Resume at step 1.
  - 3a. Player clicks the selected square again.
    1. System clears the selection. Resume at step 1.
  - 4a. The move is illegal (wrong pattern, blocked path, or own piece on the destination).
    1. System shows an error message and clears the selection.
    2. The board and the current player are unchanged. Resume at step 1.
- **Postconditions**:
  - Exactly one piece has moved, and at most one opponent piece has been removed.
  - The other player is now the current player.

## Out of Scope (later features)
- Check and checkmate. For now a player is allowed to move into check.
- Castling, en passant, and pawn promotion.
- Game end conditions.
