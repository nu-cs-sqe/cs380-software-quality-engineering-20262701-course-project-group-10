package domain;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Tests en passant captures and their boundary conditions. */
public class GameEnPassantTests {

    @Test
    public void EP06WhitePawnCapturesEnPassant() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 4)); // e5
        board.placePiece(blackPawn, new Position(3, 6)); // d7

        Game game = new Game(board);

        // White makes a legal move to pass the turn.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black pawn advances two squares: d7 -> d5.
        game.makeMove(new Position(3, 6), new Position(3, 4));

        // White captures en passant: e5 -> d6.
        game.makeMove(new Position(4, 4), new Position(3, 5));

        assertSame(whitePawn, board.getPiece(new Position(3, 5)));
        assertNull(board.getPiece(new Position(4, 4)));
        assertNull(board.getPiece(new Position(3, 4)));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void EP07BlackPawnCapturesEnPassant() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(3, 1)); // d2
        board.placePiece(blackPawn, new Position(4, 3)); // e4

        Game game = new Game(board);

        // White pawn advances two squares: d2 -> d4.
        game.makeMove(new Position(3, 1), new Position(3, 3));

        // Black captures en passant: e4 -> d3.
        game.makeMove(new Position(4, 3), new Position(3, 2));

        assertSame(blackPawn, board.getPiece(new Position(3, 2)));
        assertNull(board.getPiece(new Position(4, 3)));
        assertNull(board.getPiece(new Position(3, 3)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    public void EP08CannotCaptureEnPassantAfterSingleStep() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 4)); // e5
        board.placePiece(blackPawn, new Position(3, 5)); // d6

        Game game = new Game(board);

        // White passes the turn.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black pawn advances only one square: d6 -> d5.
        game.makeMove(new Position(3, 5), new Position(3, 4));

        // White cannot capture en passant.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(4, 4),
                        new Position(3, 5)));

        // Verify that the rejected move changes nothing.
        assertSame(whitePawn, board.getPiece(new Position(4, 4)));
        assertSame(blackPawn, board.getPiece(new Position(3, 4)));
        assertNull(board.getPiece(new Position(3, 5)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    public void EP09CannotCaptureEnPassantWhenPawnIsNotAdjacent() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 4)); // e5
        board.placePiece(blackPawn, new Position(2, 6)); // c7

        Game game = new Game(board);

        // White passes the turn.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black pawn advances two squares: c7 -> c5.
        game.makeMove(new Position(2, 6), new Position(2, 4));

        // White cannot capture en passant: e5 -> d6.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(4, 4),
                        new Position(3, 5)));

        // The rejected move must not change the board.
        assertSame(whitePawn, board.getPiece(new Position(4, 4)));
        assertSame(blackPawn, board.getPiece(new Position(2, 4)));
        assertNull(board.getPiece(new Position(3, 5)));

        // White keeps the turn.
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    public void EP10CannotCaptureEnPassantAfterOpportunityExpires() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 4)); // e5
        board.placePiece(blackPawn, new Position(3, 6)); // d7

        Game game = new Game(board);

        // White: a1 -> a2.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black: d7 -> d5 (two-square pawn advance).
        game.makeMove(new Position(3, 6), new Position(3, 4));

        // White does not capture immediately.
        game.makeMove(new Position(0, 1), new Position(0, 2));

        // Black makes another move.
        game.makeMove(new Position(7, 7), new Position(7, 6));

        // En passant is no longer available.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(4, 4),
                        new Position(3, 5)));

        // Verify the rejected move does not change the board.
        assertSame(whitePawn, board.getPiece(new Position(4, 4)));
        assertSame(blackPawn, board.getPiece(new Position(3, 4)));
        assertNull(board.getPiece(new Position(3, 5)));

        // White keeps the turn.
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    public void EP13CannotCaptureEnPassantWhenKingWouldBeInCheck() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);
        Piece whiteKnight = new Piece(PieceType.KNIGHT, Color.WHITE);
        Piece blackRook = new Piece(PieceType.ROOK, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 4));   // a5
        board.placePiece(blackKing, new Position(7, 7));   // h8
        board.placePiece(whitePawn, new Position(1, 4));   // b5
        board.placePiece(blackPawn, new Position(2, 6));   // c7
        board.placePiece(whiteKnight, new Position(0, 0)); // a1
        board.placePiece(blackRook, new Position(7, 4));   // h5

        Game game = new Game(board);

        // White: a1 -> b3.
        game.makeMove(new Position(0, 0), new Position(1, 2));

        // Black: c7 -> c5.
        game.makeMove(new Position(2, 6), new Position(2, 4));

        // White: b5 -> c6 would expose the King to the Black Rook.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(1, 4),
                        new Position(2, 5)));

        // Rejected move must leave both pawns in place.
        assertSame(whitePawn, board.getPiece(new Position(1, 4)));
        assertSame(blackPawn, board.getPiece(new Position(2, 4)));
        assertNull(board.getPiece(new Position(2, 5)));

        // White still has the turn.
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    public void EP04IllegalMoveDoesNotClearEnPassantOpportunity() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 4)); // e5
        board.placePiece(blackPawn, new Position(3, 6)); // d7

        Game game = new Game(board);

        // White: a1 -> a2.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black: d7 -> d5.
        game.makeMove(new Position(3, 6), new Position(3, 4));

        // White attempts an illegal two-square move from e5.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(4, 4),
                        new Position(4, 6)));

        // The failed move must not change the turn or board.
        assertSame(Color.WHITE, game.getCurrentPlayer());
        assertSame(whitePawn, board.getPiece(new Position(4, 4)));
        assertSame(blackPawn, board.getPiece(new Position(3, 4)));

        // En passant must still be available.
        game.makeMove(new Position(4, 4), new Position(3, 5));

        assertSame(whitePawn, board.getPiece(new Position(3, 5)));
        assertNull(board.getPiece(new Position(4, 4)));
        assertNull(board.getPiece(new Position(3, 4)));
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void EP05StartNewGameClearsEnPassantHistory() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 4)); // e5
        board.placePiece(blackPawn, new Position(3, 6)); // d7

        Game game = new Game(board);

        // White: a1 -> a2.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black: d7 -> d5.
        game.makeMove(new Position(3, 6), new Position(3, 4));

        // Start a new game.
        game.startNewGame();

        assertSame(Color.WHITE, game.getCurrentPlayer());

        // Reconstruct the old pawn positions using the same Piece objects.
        // This verifies that the previous move record was really cleared.
        board.placePiece(whitePawn, new Position(4, 4)); // e5
        board.placePiece(blackPawn, new Position(3, 4)); // d5

        // Old en passant rights must not carry over.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(4, 4),
                        new Position(3, 5)));

        assertSame(whitePawn, board.getPiece(new Position(4, 4)));
        assertSame(blackPawn, board.getPiece(new Position(3, 4)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    public void EP15CannotCaptureEnPassantAfterPawnIsReplaced() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece whitePawn = new Piece(PieceType.PAWN, Color.WHITE);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(0, 0)); // a1
        board.placePiece(blackKing, new Position(7, 7)); // h8
        board.placePiece(whitePawn, new Position(4, 4)); // e5
        board.placePiece(blackPawn, new Position(3, 6)); // d7

        Game game = new Game(board);

        // White: a1 -> a2.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black: d7 -> d5.
        game.makeMove(new Position(3, 6), new Position(3, 4));

        // Replace the pawn at d5 with a different Pawn object.
        Piece replacementPawn = new Piece(PieceType.PAWN, Color.BLACK);
        board.placePiece(replacementPawn, new Position(3, 4));

        // En passant must be rejected because the original pawn is gone.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(4, 4),
                        new Position(3, 5)));

        // The rejected move must not change the board.
        assertSame(whitePawn, board.getPiece(new Position(4, 4)));
        assertSame(replacementPawn, board.getPiece(new Position(3, 4)));
        assertNull(board.getPiece(new Position(3, 5)));

        // White keeps the turn.
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }
}
