# MoveValidator

## Rook, bishop, and queen

Method under test: `isLegalMove(Board board, Position from, Position to)`.

### Common cases

| ID | State of the system | Expected result | Implemented? |
| --- | --- | --- | --- |
| C01 | Null board, a1 to a2 | `false` | no |
| C02 | Null start, valid board and destination | `false`, no board calls | no |
| C03 | Null destination, valid board and start | `false`, no board calls | no |
| C04 | Empty start at a1, destination a2 | `false` | no |
| C05 | Separate Position objects for a1 to a1 | `false`, no board calls | no |

### Rook

| ID | Move | Board state | Expected result | Implemented? |
| --- | --- | --- | --- | --- |
| R01 | a1 to b1 | Adjacent destination | `true` | no |
| R02 | h1 to a1 | g1, f1, e1, d1, c1, b1 clear | `true` | no |
| R03 | a1 to a3 | a2 clear | `true` | no |
| R04 | a8 to a6 | a7 clear | `true` | no |
| R05 | a1 to b2 | Wrong movement pattern | `false` | no |
| R06 | a1 to a4 | a2 occupied | `false` | no |
| R07 | a1 to a4 | a2 clear. a3 occupied | `false` | no |

### Bishop

| ID | Move | Board state | Expected result | Implemented? |
| --- | --- | --- | --- | --- |
| B01 | a1 to b2 | Adjacent destination | `true` | no |
| B02 | a1 to h8 | b2, c3, d4, e5, f6, g7 clear | `true` | no |
| B03 | h8 to f6 | g7 clear | `true` | no |
| B04 | a8 to c6 | b7 clear | `true` | no |
| B05 | h1 to f3 | g2 clear | `true` | no |
| B06 | a1 to c2 | Wrong movement pattern | `false` | no |
| B07 | a1 to a3 | Wrong movement pattern | `false` | no |
| B08 | a1 to e5 | b2, c3 clear. d4 occupied | `false` | no |

### Queen

| ID | Move | Board state | Expected result | Implemented? |
| --- | --- | --- | --- | --- |
| Q01 | h4 to f4 | g4 clear | `true` | no |
| Q02 | d6 to d4 | d5 clear | `true` | no |
| Q03 | d4 to f6 | e5 clear | `true` | no |
| Q04 | a1 to b3 | Wrong movement pattern | `false` | no |
| Q05 | a1 to a3 | a2 occupied | `false` | no |
| Q06 | c1 to f4 | d2 clear. e3 occupied | `false` | no |
