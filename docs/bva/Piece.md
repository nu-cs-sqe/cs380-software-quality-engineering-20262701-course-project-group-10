# Piece

## Contract and scope

`Piece(PieceType type, Color color)` creates an immutable piece. `getType()` and
`getColor()` return the supplied enum references without changing the piece.
A null argument is rejected with `NullPointerException`; no partially initialized
piece is returned. This makes explicit the design requirement that a piece has
both a type and a color. Movement, positions, and value equality are outside this API.

## 1. Identify inputs and outputs

Inputs: type (KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN) and color (WHITE, BLACK).
Outputs: a constructed piece, the two getter results, or a constructor exception.

## 2. Select catalog clues

The BVA Catalog, version 4.0, **Cases** (p. 4) calls for every distinct
possibility and an impossible case. **Pointers** (p. 7) calls for null and a
reference to a real object. Java enums cannot represent arbitrary out-of-range
constants; those impossible enum values are CAN'T SET. Null remains representable
and is tested separately for each constructor input, including both null.
There are no numeric intervals, counts, collections, or mutable operations here.
Enum declaration order is not a numeric boundary.

## 3. Choose concrete cases

Exercise all twelve valid type/color combinations. This exceeds independent
case coverage and checks that neither getter accidentally depends on the other
argument. Read both getters twice to check that observing a piece preserves it.
For invalid inputs, hold the other argument valid, then test both null together.
Color and PieceType are enums and need no separate BVA file under this project's
rules. Their tests check the exact declared cases; Piece tests exercise their use.

## 4. Test cases

For P01-P12, methods under test are `Piece(PieceType, Color)`, `getType()`, and
`getColor()`. Each state starts with no piece and constructs the listed pair.
Expected results apply on both successive getter reads.

| ID | State / constructor arguments | Expected output | Implemented? |
| --- | --- | --- | --- |
| P01 | `KING, WHITE` | construction succeeds; type `KING`, color `WHITE` | no |
| P02 | `QUEEN, WHITE` | construction succeeds; type `QUEEN`, color `WHITE` | no |
| P03 | `ROOK, WHITE` | construction succeeds; type `ROOK`, color `WHITE` | no |
| P04 | `BISHOP, WHITE` | construction succeeds; type `BISHOP`, color `WHITE` | no |
| P05 | `KNIGHT, WHITE` | construction succeeds; type `KNIGHT`, color `WHITE` | no |
| P06 | `PAWN, WHITE` | construction succeeds; type `PAWN`, color `WHITE` | no |
| P07 | `KING, BLACK` | construction succeeds; type `KING`, color `BLACK` | no |
| P08 | `QUEEN, BLACK` | construction succeeds; type `QUEEN`, color `BLACK` | no |
| P09 | `ROOK, BLACK` | construction succeeds; type `ROOK`, color `BLACK` | no |
| P10 | `BISHOP, BLACK` | construction succeeds; type `BISHOP`, color `BLACK` | no |
| P11 | `KNIGHT, BLACK` | construction succeeds; type `KNIGHT`, color `BLACK` | no |
| P12 | `PAWN, BLACK` | construction succeeds; type `PAWN`, color `BLACK` | no |

For P13-P15, the method under test is `Piece(PieceType, Color)`.

| ID | State / constructor arguments | Expected output | Implemented? |
| --- | --- | --- | --- |
| P13 | `null, WHITE` | `NullPointerException` | no |
| P14 | `KING, null` | `NullPointerException` | no |
| P15 | `null, null` | `NullPointerException` | no |
