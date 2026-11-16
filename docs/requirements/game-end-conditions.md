# Feature: Game End Conditions, Start Screen, and Languages

This feature is complete when a game can end by checkmate, stalemate,
resignation, or agreed draw, the result is shown to the players, the
application opens on a start screen, and every screen can be shown in English
or Spanish.
Builds on the [Multiple Turns requirements](multiple-turns.md) and the
[chess game rules](game-rules.md).

## User Story 1: The Game Ends
As a chess player, I want the game to recognize when it is over and tell me
the result, so that I know who won and cannot keep moving after the end.

### Acceptance Criteria
1. After each accepted move, the game checks the player who is now to move.
2. If that player is in check and has no legal move, the game ends by
   **checkmate** and the other player wins.
3. If that player is not in check and has no legal move, the game ends in a
   **draw by stalemate**.
4. A legal move counts every rule the game supports: normal movement,
   captures, castling, en passant, and promotion. A move that leaves the
   player's own king in check is not legal.
5. The player to move can **resign**. The other player wins.
6. The players can **agree to a draw**. The game ends in a draw.
7. After the game ends, every move, resignation, and draw request is rejected.
   The board stays as it was at the end.
8. When the player to move is in check, the GUI says so.
9. When the game ends, the GUI shows the result, for example "White wins by
   checkmate" or "Draw by stalemate".
10. Starting a new game clears the result and the game is in progress again.

## User Story 2: Start Screen and Language
As a player, I want to start from a welcome screen and choose English or
Spanish, so that I can begin when I am ready and read the game in my language.

### Acceptance Criteria
1. The application opens on a start screen with a **Start Game** button and a
   language choice (English, Español). English is the default.
2. **Start Game** opens the board with a new game in the chosen language.
3. A **Language** menu on the game window switches between English and Spanish
   at any time. All visible text updates right away, and the game itself is
   unchanged.
4. Every user-visible text is translated: window title, menus, buttons, turn
   label, check message, error messages, promotion dialog, and game result
   messages.

## Use Case 4: Finish a Game
- **Actor**: Player
- **Preconditions**: A game is in progress.
- **Main Flow**:
  1. Player makes a legal move (Use Case 3).
  2. System checks whether the opponent is in check and has any legal move.
  3. The opponent is checkmated. System ends the game and shows
     "<color> wins by checkmate".
  4. System ignores further clicks on the board until a new game is started.
- **Alternate Flows**:
  - 1a. Player chooses **Resign** from the Game menu and confirms.
    1. System ends the game and shows "<other color> wins by resignation".
       Resume at step 4.
  - 1b. Player chooses **Offer Draw** from the Game menu.
    1. System asks the opponent to accept.
    2. If the opponent accepts, system ends the game and shows "Draw by
       agreement". Resume at step 4. Otherwise the game continues with the
       same player to move.
  - 3a. The opponent has no legal move and is not in check. System ends the
    game and shows "Draw by stalemate". Resume at step 4.
  - 3b. The opponent is in check and has a legal move. System shows that the
    opponent is in check. The game continues.
  - 3c. The opponent is not in check and has a legal move. The game continues.
- **Postconditions**:
  - The game has a final result and accepts no further moves.

## Use Case 5: Choose a Language
- **Actor**: Player
- **Preconditions**: The application is running.
- **Main Flow**:
  1. Player selects English or Español on the start screen or from the
     Language menu.
  2. System shows all text in the selected language.
- **Postconditions**:
  - The board, turn, and result are unchanged.

## Out of Scope
- Threefold repetition, the fifty-move rule, and insufficient material.
- Remembering the language between runs.
- Languages other than English and Spanish.
