# Feature: Game Setup Phase

Game Setup is complete when the board is initialized, every piece is on its starting square, the
board is shown in the GUI, and the system is ready for White's first move.

This file is the source text for the "Game Setup" item on the GitHub Project board.

## User Story
As a chess player, I want a new game to be set up automatically when I start the application, so that
I can begin playing right away without placing the pieces myself.

### Acceptance Criteria
1. The board has exactly 8 ranks and 8 files (64 squares).
2. Square `a1` is dark and square `h1` is light.
3. Each player has exactly 16 pieces: 1 King, 1 Queen, 2 Rooks, 2 Bishops, 2 Knights, 8 Pawns.
4. White's back-rank pieces are on rank 1 in the order Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook (`a1`–`h1`).
5. White's pawns are on rank 2. Black's pawns are on rank 7.
6. Black's back-rank pieces are on rank 8 in the same order (`a8`–`h8`).
7. Ranks 3–6 are empty.
8. The current player is White when setup is complete.
9. The GUI shows the 8×8 board with every piece on its starting square.
10. Choosing "New Game" during a game resets the board to the starting position, with White to move.

## Use Case 1: Start New Game
- **Actor**: Player
- **Preconditions**: The application is installed and can be launched.
- **Main Flow**:
  1. Player launches the application.
  2. System creates an empty 8×8 board.
  3. System places the 16 White pieces on ranks 1 and 2.
  4. System places the 16 Black pieces on ranks 7 and 8.
  5. System sets White as the current player.
  6. System displays the board in the GUI window.
- **Alternate Flows**:
  - 1a. Player chooses "New Game" while a game is in progress.
    1. System asks the player to confirm abandoning the current game.
    2. If the player confirms, resume at step 2. Otherwise, the current game continues unchanged.
- **Postconditions**:
  - The board holds exactly 32 pieces, all on their starting squares.
  - White is the current player.
  - The game is ready for the first move.

## Ideas for Later (not part of this feature)
- Entering player names
- Choosing which side is shown at the bottom of the board
