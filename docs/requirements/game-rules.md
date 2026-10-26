# Chess Game Rules

Official reference: [FIDE Laws of Chess](https://handbook.fide.com/chapter/E012023).
The summary below is the version of the rules this project implements. Where it differs from the
FIDE laws, the difference is called out under [Out of Scope](#out-of-scope).

## 1. Players and Board
- Two players: **White** and **Black**.
- The board is 8×8 (64 squares) with alternating light and dark squares.
- Columns ("files") are labeled `a`–`h` from White's left to right. Rows ("ranks") are labeled `1`–`8`
  from White's side to Black's side.
- The square `h1` (White's bottom-right corner) is a light square.

## 2. Initial Setup
Each player starts with 16 pieces: 1 King, 1 Queen, 2 Rooks, 2 Bishops, 2 Knights, 8 Pawns.

| Rank | Contents |
|------|----------|
| 8 | Black: Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook (`a8`–`h8`) |
| 7 | Black: 8 Pawns |
| 3–6 | Empty |
| 2 | White: 8 Pawns |
| 1 | White: Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook (`a1`–`h1`) |

- The Queen starts on a square of her own color (White Queen on `d1`, Black Queen on `d8`).
- **White moves first.**

## 3. Turns
- Players alternate turns. On each turn a player must move exactly one of their own pieces.
- A piece cannot move onto a square occupied by a piece of the same color.
- Moving onto a square occupied by an opponent's piece **captures** it (it is removed from the board).
- A player may never make a move that leaves their own King in check.

## 4. Piece Movement
| Piece | Movement |
|-------|----------|
| King | One square in any direction. |
| Queen | Any number of squares along a rank, file, or diagonal. |
| Rook | Any number of squares along a rank or file. |
| Bishop | Any number of squares along a diagonal. |
| Knight | An "L": two squares in one direction along a rank or file, then one square perpendicular. The only piece that can jump over other pieces. |
| Pawn | One square straight forward onto an empty square; two squares forward from its starting rank if both squares are empty; captures one square diagonally forward. |

Queen, Rook, and Bishop cannot jump over pieces.

## 5. Special Moves
- **Castling**: The King moves two squares toward a Rook and the Rook jumps to the square the King crossed.
  Allowed only if neither piece has moved, all squares between them are empty, and the King is not in
  check, does not pass through an attacked square, and does not end in check.
- **En passant**: If a pawn moves two squares and lands beside an opponent's pawn, the opponent's pawn may
  capture it as if it had moved one square. This is only allowed on the very next move.
- **Promotion**: A pawn that reaches the last rank is replaced by a Queen, Rook, Bishop, or Knight of the
  same color (player's choice).

## 6. Check, Checkmate, and Stalemate
- **Check**: A King is attacked by an opponent's piece. The player must get out of check on their move.
- **Checkmate**: The King is in check and the player has no legal move. The player in checkmate loses.
- **Stalemate**: The player to move is not in check but has no legal move. The game is a draw.

## 7. Other Ways the Game Ends
- **Resignation**: A player may resign, and the opponent wins.
- **Draw by agreement**: Both players agree to a draw.

## Out of Scope
The following FIDE rules are not planned for now. The team may add them later:
- Chess clocks / time control
- Threefold repetition and the fifty-move rule
- Draw by insufficient material
