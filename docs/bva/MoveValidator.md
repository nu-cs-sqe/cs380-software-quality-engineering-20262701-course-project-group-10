# MoveValidator

## Contract and scope

Method under test for all cases: `isLegalMove(Board board, Position from, Position to)`.
The validator observes the board and returns a boolean without moving pieces.
Null board, from, or to throws `NullPointerException`; an empty source or equal
positions returns false. Pawn rules include occupancy and opponent-only captures.
Knight and King check geometry only. Rook, Bishop, and Queen check geometry
and require a clear path. Game owns destination-color and turn checks.
Check, castling, en passant, and promotion are out of scope.
The implicit public no-argument constructor has no inputs or branches; every case
constructs a fresh validator, so construction is covered by every implemented row.

## 1. Inputs and outputs

Inputs: board reference, source/destination positions, moving piece type/color,
and empty, friendly, or opposing pieces on relevant squares. Outputs: true,
false, or a null-argument exception; all board squares retain their piece references.
White moves toward rank 7; Black toward rank 0. Pawn starting ranks are 1 and 6.

## 2. Catalog clues

BVA Catalog version 4.0: **Cases** (p. 4), **Boolean** (p. 4),
**Intervals** (pp. 5-6), **Pointers** (p. 7), and **Pairs of references / object
equality** (p. 12). Exercise every owned piece type, both colors, both boolean
outcomes, null and real references, identical and distinct-but-equal positions.
For file/rank [0,7], use both endpoints. -1 and 8 are CAN'T SET for this API:
Position rejects them before validation; PositionTests already exercise those
constructor boundaries. Java enum values outside the declared cases are also
CAN'T SET; null moving pieces are represented by empty source squares.

## 3. Concrete boundaries and isolation

Pawn: rank changes 0, 1, 2, 3 and the wrong direction; file changes 0, 1, 2;
starting rank and its immediate neighbors; both empty squares versus either
blocked square for double advances; empty/friendly/enemy diagonal targets;
both capture directions; files/ranks 0 and 7. Reaching a last rank validates
movement only and does not promote the piece. No path loops are needed: a double
advance has exactly one intermediate square.
Knight: every signed (1,2)/(2,1), nearby invalid 0/1/2/3 displacements, extreme
(7,7), corners, occupied intermediate squares, and either destination color.
King: all eight neighboring squares, zero displacement, two-square boundaries
on each axis and both axes, maximum displacement, corners, either target color.
Real Board instances test observable behavior. Strict EasyMock Boards check
that Knight/King do not read paths/targets and Pawn reads the required squares.
The sliding-piece tests use EasyMock for the source and intermediate squares.
Null arguments are each isolated with other arguments valid, plus all null
together.

## 4. Concrete test cases

All coordinates are zero-based `(file, rank)`. Unlisted squares are empty.
Tests using a real Board also assert that all 64 squares keep the same piece
references. Mock Board tests check expected calls. IDs appear in test method names.

### pawn one

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| P01 | WHITE PAWN at (3, 1); to (3, 2) | `true`; board unchanged | yes |

### pawn direction

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| P02 | BLACK PAWN at (3, 6); to (3, 5) | `true`; board unchanged | yes |
| P03 | WHITE PAWN at (3, 3); to (3, 2) | `false`; board unchanged | yes |
| P04 | BLACK PAWN at (3, 4); to (3, 5) | `false`; board unchanged | yes |
| P05 | WHITE PAWN at (3, 3); to (4, 3) | `false`; board unchanged | yes |
| P06 | WHITE PAWN at (3, 1); to (3, 4) | `false`; board unchanged | yes |
| P07 | BLACK PAWN at (3, 6); to (3, 3) | `false`; board unchanged | yes |

### pawn blocked

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| P08 | WHITE PAWN at (3, 1); to (3, 2); also WHITE KNIGHT at (3, 2) | `false`; board unchanged | yes |
| P09 | WHITE PAWN at (3, 1); to (3, 2); also BLACK KNIGHT at (3, 2) | `false`; board unchanged | yes |
| P10 | BLACK PAWN at (3, 6); to (3, 5); also WHITE KNIGHT at (3, 5) | `false`; board unchanged | yes |
| P11 | BLACK PAWN at (3, 6); to (3, 5); also BLACK KNIGHT at (3, 5) | `false`; board unchanged | yes |

### pawn double

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| P12 | WHITE PAWN at (3, 1); to (3, 3) | `true`; board unchanged | yes |
| P13 | BLACK PAWN at (3, 6); to (3, 4) | `true`; board unchanged | yes |

### pawn start

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| P14 | WHITE PAWN at (3, 0); to (3, 2) | `false`; board unchanged | yes |
| P15 | WHITE PAWN at (3, 2); to (3, 4) | `false`; board unchanged | yes |
| P16 | BLACK PAWN at (3, 5); to (3, 3) | `false`; board unchanged | yes |
| P17 | BLACK PAWN at (3, 7); to (3, 5) | `false`; board unchanged | yes |

### pawn double blocked

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| P18 | WHITE PAWN at (3, 1); to (3, 3); also WHITE BISHOP at (3, 2) | `false`; board unchanged | yes |
| P19 | WHITE PAWN at (3, 1); to (3, 3); also BLACK BISHOP at (3, 2) | `false`; board unchanged | yes |
| P20 | WHITE PAWN at (3, 1); to (3, 3); also WHITE BISHOP at (3, 3) | `false`; board unchanged | yes |
| P21 | WHITE PAWN at (3, 1); to (3, 3); also BLACK BISHOP at (3, 3) | `false`; board unchanged | yes |
| P22 | BLACK PAWN at (3, 6); to (3, 4); also WHITE BISHOP at (3, 5) | `false`; board unchanged | yes |
| P23 | BLACK PAWN at (3, 6); to (3, 4); also BLACK BISHOP at (3, 5) | `false`; board unchanged | yes |
| P24 | BLACK PAWN at (3, 6); to (3, 4); also WHITE BISHOP at (3, 4) | `false`; board unchanged | yes |
| P25 | BLACK PAWN at (3, 6); to (3, 4); also BLACK BISHOP at (3, 4) | `false`; board unchanged | yes |

### pawn capture

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| P26 | WHITE PAWN at (3, 3); to (2, 4); also BLACK ROOK at (2, 4) | `true`; board unchanged | yes |
| P27 | WHITE PAWN at (3, 3); to (4, 4); also BLACK ROOK at (4, 4) | `true`; board unchanged | yes |
| P28 | BLACK PAWN at (3, 4); to (2, 3); also WHITE ROOK at (2, 3) | `true`; board unchanged | yes |
| P29 | BLACK PAWN at (3, 4); to (4, 3); also WHITE ROOK at (4, 3) | `true`; board unchanged | yes |

### pawn capture bounds

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| P30 | WHITE PAWN at (3, 3); to (2, 4) | `false`; board unchanged | yes |
| P31 | WHITE PAWN at (3, 3); to (4, 4) | `false`; board unchanged | yes |
| P32 | BLACK PAWN at (3, 4); to (2, 3) | `false`; board unchanged | yes |
| P33 | BLACK PAWN at (3, 4); to (4, 3) | `false`; board unchanged | yes |
| P34 | WHITE PAWN at (3, 3); to (2, 4); also WHITE PAWN at (2, 4) | `false`; board unchanged | yes |
| P35 | WHITE PAWN at (3, 3); to (4, 4); also WHITE PAWN at (4, 4) | `false`; board unchanged | yes |
| P36 | BLACK PAWN at (3, 4); to (2, 3); also BLACK PAWN at (2, 3) | `false`; board unchanged | yes |
| P37 | BLACK PAWN at (3, 4); to (4, 3); also BLACK PAWN at (4, 3) | `false`; board unchanged | yes |
| P38 | WHITE PAWN at (3, 3); to (5, 4); also BLACK ROOK at (5, 4) | `false`; board unchanged | yes |
| P39 | WHITE PAWN at (3, 1); to (4, 3); also BLACK ROOK at (4, 3) | `false`; board unchanged | yes |
| P40 | WHITE PAWN at (3, 3); to (4, 2); also BLACK ROOK at (4, 2) | `false`; board unchanged | yes |
| P41 | BLACK PAWN at (3, 4); to (1, 3); also WHITE ROOK at (1, 3) | `false`; board unchanged | yes |
| P42 | BLACK PAWN at (3, 6); to (2, 4); also WHITE ROOK at (2, 4) | `false`; board unchanged | yes |
| P43 | BLACK PAWN at (3, 4); to (2, 5); also WHITE ROOK at (2, 5) | `false`; board unchanged | yes |

### pawn edges

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| P44 | WHITE PAWN at (0, 6); to (0, 7) | `true`; board unchanged | yes |
| P45 | WHITE PAWN at (7, 6); to (7, 7) | `true`; board unchanged | yes |
| P46 | BLACK PAWN at (0, 1); to (0, 0) | `true`; board unchanged | yes |
| P47 | BLACK PAWN at (7, 1); to (7, 0) | `true`; board unchanged | yes |
| P48 | WHITE PAWN at (0, 1); to (0, 3) | `true`; board unchanged | yes |
| P49 | BLACK PAWN at (7, 6); to (7, 4) | `true`; board unchanged | yes |
| P50 | WHITE PAWN at (0, 6); to (1, 7); also BLACK QUEEN at (1, 7) | `true`; board unchanged | yes |
| P51 | WHITE PAWN at (7, 6); to (6, 7); also BLACK QUEEN at (6, 7) | `true`; board unchanged | yes |
| P52 | BLACK PAWN at (0, 1); to (1, 0); also WHITE QUEEN at (1, 0) | `true`; board unchanged | yes |
| P53 | BLACK PAWN at (7, 1); to (6, 0); also WHITE QUEEN at (6, 0) | `true`; board unchanged | yes |

### knight shape

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| N01 | WHITE KNIGHT at (3, 3); to (4, 5) | `true`; board unchanged | yes |
| N02 | WHITE KNIGHT at (3, 3); to (5, 4) | `true`; board unchanged | yes |
| N03 | WHITE KNIGHT at (3, 3); to (2, 5) | `true`; board unchanged | yes |
| N04 | WHITE KNIGHT at (3, 3); to (1, 4) | `true`; board unchanged | yes |
| N05 | WHITE KNIGHT at (3, 3); to (4, 1) | `true`; board unchanged | yes |
| N06 | WHITE KNIGHT at (3, 3); to (5, 2) | `true`; board unchanged | yes |
| N07 | WHITE KNIGHT at (3, 3); to (2, 1) | `true`; board unchanged | yes |
| N08 | WHITE KNIGHT at (3, 3); to (1, 2) | `true`; board unchanged | yes |

### knight bounds

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| N09 | BLACK KNIGHT at (3, 3); to (3, 3) | `false`; board unchanged | yes |
| N10 | BLACK KNIGHT at (3, 3); to (3, 4) | `false`; board unchanged | yes |
| N11 | BLACK KNIGHT at (3, 3); to (4, 3) | `false`; board unchanged | yes |
| N12 | BLACK KNIGHT at (3, 3); to (4, 4) | `false`; board unchanged | yes |
| N13 | BLACK KNIGHT at (3, 3); to (5, 5) | `false`; board unchanged | yes |
| N14 | BLACK KNIGHT at (3, 3); to (3, 5) | `false`; board unchanged | yes |
| N15 | BLACK KNIGHT at (3, 3); to (5, 3) | `false`; board unchanged | yes |
| N16 | BLACK KNIGHT at (3, 3); to (4, 6) | `false`; board unchanged | yes |
| N17 | BLACK KNIGHT at (3, 3); to (6, 4) | `false`; board unchanged | yes |
| N18 | BLACK KNIGHT at (0, 0); to (7, 7) | `false`; board unchanged | yes |

### knight edges

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| N19 | BLACK KNIGHT at (0, 0); to (1, 2) | `true`; board unchanged | yes |
| N20 | BLACK KNIGHT at (7, 7); to (5, 6) | `true`; board unchanged | yes |
| N21 | BLACK KNIGHT at (0, 7); to (2, 6) | `true`; board unchanged | yes |
| N22 | BLACK KNIGHT at (7, 0); to (6, 2) | `true`; board unchanged | yes |
| N23 | BLACK KNIGHT at (1, 2); to (0, 0) | `true`; board unchanged | yes |
| N24 | BLACK KNIGHT at (5, 6); to (7, 7) | `true`; board unchanged | yes |

### knight jumping

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| N25 | WHITE KNIGHT at (3, 3); to (4, 5); also WHITE PAWN at (3, 4), WHITE PAWN at (4, 3), WHITE ROOK at (4, 4), WHITE BISHOP at (4, 5) | `true`; board unchanged | yes |
| N26 | WHITE KNIGHT at (3, 3); to (4, 5); also BLACK PAWN at (3, 4), BLACK PAWN at (4, 3), BLACK ROOK at (4, 4), BLACK BISHOP at (4, 5) | `true`; board unchanged | yes |

### king step

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| K01 | WHITE KING at (3, 3); to (3, 4) | `true`; board unchanged | yes |
| K02 | WHITE KING at (3, 3); to (4, 3) | `true`; board unchanged | yes |
| K03 | WHITE KING at (3, 3); to (4, 4) | `true`; board unchanged | yes |
| K04 | WHITE KING at (3, 3); to (2, 3) | `true`; board unchanged | yes |
| K05 | WHITE KING at (3, 3); to (3, 2) | `true`; board unchanged | yes |
| K06 | WHITE KING at (3, 3); to (2, 2) | `true`; board unchanged | yes |
| K07 | WHITE KING at (3, 3); to (2, 4) | `true`; board unchanged | yes |
| K08 | WHITE KING at (3, 3); to (4, 2) | `true`; board unchanged | yes |

### king bounds

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| K09 | BLACK KING at (3, 3); to (3, 3) | `false`; board unchanged | yes |
| K10 | BLACK KING at (3, 3); to (3, 5) | `false`; board unchanged | yes |
| K11 | BLACK KING at (3, 3); to (5, 3) | `false`; board unchanged | yes |
| K12 | BLACK KING at (3, 3); to (5, 5) | `false`; board unchanged | yes |
| K13 | BLACK KING at (3, 3); to (4, 5) | `false`; board unchanged | yes |
| K14 | BLACK KING at (3, 3); to (5, 4) | `false`; board unchanged | yes |
| K15 | BLACK KING at (3, 3); to (1, 3) | `false`; board unchanged | yes |
| K16 | BLACK KING at (3, 3); to (3, 1) | `false`; board unchanged | yes |
| K17 | BLACK KING at (3, 3); to (1, 1) | `false`; board unchanged | yes |
| K18 | BLACK KING at (0, 0); to (7, 7) | `false`; board unchanged | yes |

### king edges

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| K19 | BLACK KING at (0, 0); to (1, 1) | `true`; board unchanged | yes |
| K20 | BLACK KING at (7, 7); to (6, 6) | `true`; board unchanged | yes |
| K21 | BLACK KING at (0, 7); to (1, 6) | `true`; board unchanged | yes |
| K22 | BLACK KING at (7, 0); to (6, 1) | `true`; board unchanged | yes |

### king occupancy

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| K23 | WHITE KING at (3, 3); to (4, 4); also WHITE PAWN at (4, 4) | `true`; board unchanged | yes |
| K24 | WHITE KING at (3, 3); to (4, 4); also BLACK PAWN at (4, 4) | `true`; board unchanged | yes |

### API boundaries and interaction checks

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| V01 | null board; from (3,3), to (3,4) | NullPointerException | yes |
| V02 | real board; null from, to (3,4) | NullPointerException | yes |
| V03 | real board; from (3,3), null to | NullPointerException | yes |
| V04 | all three arguments null | NullPointerException | yes |
| V05 | empty board; from (3,3), to (3,4) | false; unchanged | yes |
| V06 | each owned type/color; same Position reference for from/to | false; unchanged | yes |
| V07 | each owned type/color; distinct Position objects with equal (3,3) coordinates | false; unchanged | yes |
| V08 | white knight at (3,3), to (4,5); strict Board expects only source read | true; no other Board calls | yes |
| V09 | black king at (3,3), to (4,4); strict Board expects only source read | true; no other Board calls | yes |
| V10 | white pawn at (3,1), to (3,3); strict Board supplies empty destination and intermediate square | true; required reads and no writes | yes |
| V11 | white pawn at (3,3), to (4,4); strict Board supplies black rook at destination | true; required reads and no writes | yes |
| V12 | WHITE ROOK, BISHOP, and QUEEN separately at (3, 3); to (3, 4) | `true` for rook and queen, `false` for bishop; board unchanged | yes |
| C01 | null board; from (0, 0), to (0, 1) | NullPointerException | yes |
| C02 | mock board; null from, to (0, 1) | NullPointerException; no Board calls | yes |
| C03 | mock board; from (0, 0), null to | NullPointerException; no Board calls | yes |
| C04 | empty source at (0, 0); to (0, 1) | `false`; only source read | yes |
| C05 | distinct Position objects for (0, 0) to (0, 0) | `false`; no Board calls | yes |

### rook moves

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| R01 | WHITE ROOK at (0, 0); to (1, 0) | `true`; expected Board reads only | yes |
| R02 | WHITE ROOK at (7, 0); to (0, 0); (6, 0), (5, 0), (4, 0), (3, 0), (2, 0), (1, 0) clear | `true`; expected Board reads only | yes |
| R03 | WHITE ROOK at (0, 0); to (0, 2); (0, 1) clear | `true`; expected Board reads only | yes |
| R04 | WHITE ROOK at (0, 7); to (0, 5); (0, 6) clear | `true`; expected Board reads only | yes |
| R05 | WHITE ROOK at (0, 0); to (1, 1) | `false`; expected Board reads only | yes |
| R06 | WHITE ROOK at (0, 0); to (0, 3); (0, 1) occupied | `false`; expected Board reads only | yes |
| R07 | WHITE ROOK at (0, 0); to (0, 3); (0, 1) clear; (0, 2) occupied | `false`; expected Board reads only | yes |

### bishop moves

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| B01 | WHITE BISHOP at (0, 0); to (1, 1) | `true`; expected Board reads only | yes |
| B02 | WHITE BISHOP at (0, 0); to (7, 7); (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6) clear | `true`; expected Board reads only | yes |
| B03 | WHITE BISHOP at (7, 7); to (5, 5); (6, 6) clear | `true`; expected Board reads only | yes |
| B04 | WHITE BISHOP at (0, 7); to (2, 5); (1, 6) clear | `true`; expected Board reads only | yes |
| B05 | WHITE BISHOP at (7, 0); to (5, 2); (6, 1) clear | `true`; expected Board reads only | yes |
| B06 | WHITE BISHOP at (0, 0); to (2, 1) | `false`; expected Board reads only | yes |
| B07 | WHITE BISHOP at (0, 0); to (0, 2) | `false`; expected Board reads only | yes |
| B08 | WHITE BISHOP at (0, 0); to (4, 4); (1, 1), (2, 2) clear; (3, 3) occupied | `false`; expected Board reads only | yes |

### queen moves

| ID | State of system | Expected output | Implemented? |
| --- | --- | --- | --- |
| Q01 | WHITE QUEEN at (7, 3); to (5, 3); (6, 3) clear | `true`; expected Board reads only | yes |
| Q02 | WHITE QUEEN at (3, 5); to (3, 3); (3, 4) clear | `true`; expected Board reads only | yes |
| Q03 | WHITE QUEEN at (3, 3); to (5, 5); (4, 4) clear | `true`; expected Board reads only | yes |
| Q04 | WHITE QUEEN at (0, 0); to (1, 2) | `false`; expected Board reads only | yes |
| Q05 | WHITE QUEEN at (0, 0); to (0, 2); (0, 1) occupied | `false`; expected Board reads only | yes |
| Q06 | WHITE QUEEN at (2, 0); to (5, 3); (3, 1) clear; (4, 2) occupied | `false`; expected Board reads only | yes |
