package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.Board;
import domain.Color;
import domain.Game;
import domain.GameStatus;
import domain.Piece;
import domain.PieceType;
import domain.Position;
import org.junit.jupiter.api.Test;

/** Integration tests for multiple turns using real game components. */
public class MultipleTurnsIntegrationTests {

    @Test
    public void IT01NormalMovesAndCaptures() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // 1. White: e2 -> e4.
        game.makeMove(new Position(4, 1), new Position(4, 3));

        Piece whitePawn = board.getPiece(new Position(4, 3));

        assertEquals(PieceType.PAWN, whitePawn.getType());
        assertEquals(Color.WHITE, whitePawn.getColor());
        assertNull(board.getPiece(new Position(4, 1)));
        assertSame(Color.BLACK, game.getCurrentPlayer());

        // 2. Black: d7 -> d5.
        game.makeMove(new Position(3, 6), new Position(3, 4));

        Piece blackPawn = board.getPiece(new Position(3, 4));

        assertEquals(PieceType.PAWN, blackPawn.getType());
        assertEquals(Color.BLACK, blackPawn.getColor());
        assertSame(Color.WHITE, game.getCurrentPlayer());

        // 3. White captures Black pawn: e4 -> d5.
        game.makeMove(new Position(4, 3), new Position(3, 4));

        // The original White pawn now occupies d5.
        assertSame(whitePawn, board.getPiece(new Position(3, 4)));

        // Both original squares are empty.
        assertNull(board.getPiece(new Position(4, 3)));
        assertNull(board.getPiece(new Position(3, 6)));

        // Black pawn was captured.
        // The piece on d5 must not be the original Black pawn.
        org.junit.jupiter.api.Assertions.assertNotSame(
                blackPawn, board.getPiece(new Position(3, 4)));

        // Turn switches to Black.
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void IT02CastlingAfterMultipleMoves() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // 1. White moves the knight away from g1.
        game.makeMove(new Position(6, 0), new Position(5, 2)); // Ng1-f3

        // 2. Black moves a pawn.
        game.makeMove(new Position(0, 6), new Position(0, 5)); // a7-a6

        // 3. White moves the e-pawn to open the bishop's diagonal.
        game.makeMove(new Position(4, 1), new Position(4, 3)); // e2-e4

        // 4. Black moves another pawn.
        game.makeMove(new Position(0, 5), new Position(0, 4)); // a6-a5

        // 5. White moves the bishop away from f1.
        game.makeMove(new Position(5, 0), new Position(4, 1)); // Bf1-e2

        // 6. Black makes another legal move.
        game.makeMove(new Position(1, 6), new Position(1, 5)); // b7-b6

        // 7. White castles king-side: e1-g1.
        game.makeMove(new Position(4, 0), new Position(6, 0));

        // Verify the King moved to g1.
        Piece king = board.getPiece(new Position(6, 0));
        assertEquals(PieceType.KING, king.getType());
        assertEquals(Color.WHITE, king.getColor());

        // Verify the Rook moved from h1 to f1.
        Piece rook = board.getPiece(new Position(5, 0));
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals(Color.WHITE, rook.getColor());

        // Original squares are now empty.
        assertNull(board.getPiece(new Position(4, 0)));
        assertNull(board.getPiece(new Position(7, 0)));

        // Castling rights are permanently lost after castling.
        org.junit.jupiter.api.Assertions.assertFalse(
                game.canCastleKingSide(Color.WHITE));
        org.junit.jupiter.api.Assertions.assertFalse(
                game.canCastleQueenSide(Color.WHITE));

        // Turn switches to Black.
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void IT03EnPassantAfterMultipleMoves() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // 1. White pawn advances two squares: e2 -> e4.
        game.makeMove(new Position(4, 1), new Position(4, 3));

        final Piece whitePawn = board.getPiece(new Position(4, 3));

        assertSame(Color.BLACK, game.getCurrentPlayer());

        // 2. Black plays a7 -> a6.
        game.makeMove(new Position(0, 6), new Position(0, 5));

        // 3. White advances to e5.
        game.makeMove(new Position(4, 3), new Position(4, 4));

        assertSame(whitePawn, board.getPiece(new Position(4, 4)));

        // 4. Black advances two squares: d7 -> d5.
        game.makeMove(new Position(3, 6), new Position(3, 4));

        Piece blackPawn = board.getPiece(new Position(3, 4));

        assertSame(PieceType.PAWN, blackPawn.getType());
        assertSame(Color.BLACK, blackPawn.getColor());
        assertSame(Color.WHITE, game.getCurrentPlayer());

        // 5. White captures en passant: e5 -> d6.
        game.makeMove(new Position(4, 4), new Position(3, 5));

        // White pawn now occupies d6.
        assertSame(whitePawn, board.getPiece(new Position(3, 5)));

        // The original square e5 is empty.
        assertNull(board.getPiece(new Position(4, 4)));

        // The captured black pawn has been removed from d5.
        assertNull(board.getPiece(new Position(3, 4)));

        // Turn switches to Black.
        assertSame(Color.BLACK, game.getCurrentPlayer());

        // The game should still be in progress.
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    public void IT04PromotionAfterMultipleMoves() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // Keep a reference to the original White pawn.
        final Piece whitePawn = board.getPiece(new Position(0, 1)); // a2

        // 1. White: a2 -> a4; Black: h7 -> h5.
        game.makeMove(new Position(0, 1), new Position(0, 3));
        game.makeMove(new Position(7, 6), new Position(7, 4));

        // 2. White: a4 -> a5; Black: h5 -> h4.
        game.makeMove(new Position(0, 3), new Position(0, 4));
        game.makeMove(new Position(7, 4), new Position(7, 3));

        // 3. White: a5 -> a6; Black: h4 -> h3.
        game.makeMove(new Position(0, 4), new Position(0, 5));
        game.makeMove(new Position(7, 3), new Position(7, 2));

        // 4. White captures the Black pawn: a6 -> b7.
        game.makeMove(new Position(0, 5), new Position(1, 6));

        assertSame(whitePawn, board.getPiece(new Position(1, 6)));

        // Black captures the White pawn: h3 -> g2.
        game.makeMove(new Position(7, 2), new Position(6, 1));

        // Before promotion, the White pawn is on b7.
        assertSame(whitePawn, board.getPiece(new Position(1, 6)));
        assertSame(Color.WHITE, game.getCurrentPlayer());

        // The Black rook is still on a8.
        Piece blackRook = board.getPiece(new Position(0, 7));
        assertEquals(PieceType.ROOK, blackRook.getType());
        assertEquals(Color.BLACK, blackRook.getColor());

        // 5. White captures the rook and promotes: b7 -> a8 = Queen.
        game.makeMove(
                new Position(1, 6),
                new Position(0, 7),
                PieceType.QUEEN);

        // The original square b7 is empty.
        assertNull(board.getPiece(new Position(1, 6)));

        // The destination contains a White Queen.
        Piece promoted = board.getPiece(new Position(0, 7));

        assertEquals(PieceType.QUEEN, promoted.getType());
        assertEquals(Color.WHITE, promoted.getColor());

        // Promotion replaces the pawn with a different Piece object.
        org.junit.jupiter.api.Assertions.assertNotSame(whitePawn, promoted);

        // The original Black rook has been captured.
        org.junit.jupiter.api.Assertions.assertNotSame(blackRook, promoted);

        // Turn switches to Black.
        assertSame(Color.BLACK, game.getCurrentPlayer());

        // The game remains in progress.
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    public void IT05IllegalMoveLeavesKingInCheck() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // 1. White: e2 -> e4.
        game.makeMove(new Position(4, 1), new Position(4, 3));

        // 2. Black: e7 -> e5.
        game.makeMove(new Position(4, 6), new Position(4, 4));

        // 3. White: d2 -> d3.
        game.makeMove(new Position(3, 1), new Position(3, 2));

        // 4. Black: Bf8 -> b4+, checking the White King.
        game.makeMove(new Position(5, 7), new Position(1, 3));

        assertSame(Color.WHITE, game.getCurrentPlayer());
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
        assertTrue(game.isCurrentPlayerInCheck());

        // Save the pieces before attempting an illegal move.
        Piece whitePawn = board.getPiece(new Position(0, 1)); // a2
        final Piece blackBishop = board.getPiece(new Position(1, 3)); // b4

        // 5. White attempts a2 -> a3.
        // This move is illegal because it does not resolve the check.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(0, 1),
                        new Position(0, 2)));

        // The rejected move must not change the board.
        assertSame(whitePawn, board.getPiece(new Position(0, 1)));
        assertNull(board.getPiece(new Position(0, 2)));
        assertSame(blackBishop, board.getPiece(new Position(1, 3)));

        // The turn and game status must remain unchanged.
        assertSame(Color.WHITE, game.getCurrentPlayer());
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
        assertTrue(game.isCurrentPlayerInCheck());

        // 6. White makes a legal move: c2 -> c3.
        // The pawn blocks the Bishop's diagonal attack.
        game.makeMove(new Position(2, 1), new Position(2, 2));

        // The White King is no longer in check.
        assertFalse(game.isCurrentPlayerInCheck());

        // The turn finally switches to Black.
        assertSame(Color.BLACK, game.getCurrentPlayer());
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    public void IT06CastlingRightsNotRestoredAfterRookReturns() {
        Board board = new Board();

        Piece whiteKing = new Piece(PieceType.KING, Color.WHITE);
        Piece whiteRook = new Piece(PieceType.ROOK, Color.WHITE);
        Piece blackKing = new Piece(PieceType.KING, Color.BLACK);
        Piece blackPawn = new Piece(PieceType.PAWN, Color.BLACK);

        board.placePiece(whiteKing, new Position(4, 0)); // e1
        board.placePiece(whiteRook, new Position(7, 0)); // h1
        board.placePiece(blackKing, new Position(4, 7)); // e8
        board.placePiece(blackPawn, new Position(0, 6)); // a7

        Game game = new Game(board);

        // Initially, White has the right to castle king-side.
        assertTrue(game.canCastleKingSide(Color.WHITE));

        // 1. White moves the rook away: h1 -> h2.
        game.makeMove(new Position(7, 0), new Position(7, 1));

        assertFalse(game.canCastleKingSide(Color.WHITE));
        assertSame(Color.BLACK, game.getCurrentPlayer());

        // 2. Black: a7 -> a6.
        game.makeMove(new Position(0, 6), new Position(0, 5));

        // 3. White returns the rook: h2 -> h1.
        game.makeMove(new Position(7, 1), new Position(7, 0));

        assertSame(whiteRook, board.getPiece(new Position(7, 0)));

        // Castling rights must NOT be restored.
        assertFalse(game.canCastleKingSide(Color.WHITE));

        // 4. Black: a6 -> a5.
        game.makeMove(new Position(0, 5), new Position(0, 4));

        assertSame(Color.WHITE, game.getCurrentPlayer());

        // 5. White attempts to castle: e1 -> g1.
        // This must fail because the rook has moved before.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(4, 0),
                        new Position(6, 0)));

        // Rejected castling must leave pieces unchanged.
        assertSame(whiteKing, board.getPiece(new Position(4, 0)));
        assertSame(whiteRook, board.getPiece(new Position(7, 0)));
        assertNull(board.getPiece(new Position(6, 0)));
        assertNull(board.getPiece(new Position(5, 0)));

        // No turn change after the rejected move.
        assertSame(Color.WHITE, game.getCurrentPlayer());
        assertFalse(game.canCastleKingSide(Color.WHITE));

        // 6. White makes an ordinary legal king move: e1 -> f1.
        game.makeMove(new Position(4, 0), new Position(5, 0));

        assertSame(whiteKing, board.getPiece(new Position(5, 0)));
        assertNull(board.getPiece(new Position(4, 0)));
        assertSame(whiteRook, board.getPiece(new Position(7, 0)));

        // The legal move switches the turn normally.
        assertSame(Color.BLACK, game.getCurrentPlayer());
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
    }


    @Test
    public void IT07IllegalMoveDoesNotExpireEnPassant() {
        Board board = new Board();
        board.placePiece(new Piece(PieceType.KING, Color.WHITE),
                new Position(0, 0)); // a1
        board.placePiece(new Piece(PieceType.KING, Color.BLACK),
                new Position(7, 7)); // h8
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE),
                new Position(4, 4)); // e5
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK),
                new Position(3, 6)); // d7

        Game game = new Game(board);
        final Piece whitePawn = board.getPiece(new Position(4, 4));

        // White makes a normal move: a1 -> a2.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black creates an en passant opportunity: d7 -> d5.
        game.makeMove(new Position(3, 6), new Position(3, 4));

        Piece blackPawn = board.getPiece(new Position(3, 4));

        // White attempts an illegal pawn move: e5 -> e7.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(4, 4),
                        new Position(4, 6)));

        // The illegal move must not change the game.
        assertSame(whitePawn, board.getPiece(new Position(4, 4)));
        assertSame(blackPawn, board.getPiece(new Position(3, 4)));
        assertSame(Color.WHITE, game.getCurrentPlayer());

        // White can still capture en passant: e5 -> d6.
        game.makeMove(new Position(4, 4), new Position(3, 5));

        assertSame(whitePawn, board.getPiece(new Position(3, 5)));
        assertNull(board.getPiece(new Position(4, 4)));
        assertNull(board.getPiece(new Position(3, 4)));
        assertSame(Color.BLACK, game.getCurrentPlayer());
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    public void IT08LegalMoveExpiresEnPassantOpportunity() {
        Board board = new Board();
        board.placePiece(new Piece(PieceType.KING, Color.WHITE),
                new Position(0, 0)); // a1
        board.placePiece(new Piece(PieceType.KING, Color.BLACK),
                new Position(7, 7)); // h8
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE),
                new Position(4, 4)); // e5
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK),
                new Position(3, 6)); // d7

        Game game = new Game(board);
        final Piece whitePawn = board.getPiece(new Position(4, 4));

        // White: a1 -> a2.
        game.makeMove(new Position(0, 0), new Position(0, 1));

        // Black: d7 -> d5, creating an en passant opportunity.
        game.makeMove(new Position(3, 6), new Position(3, 4));

        final Piece blackPawn = board.getPiece(new Position(3, 4));

        // White makes a different legal move instead of en passant.
        game.makeMove(new Position(0, 1), new Position(0, 2));

        // Black makes another legal move: h8 -> h7.
        game.makeMove(new Position(7, 7), new Position(7, 6));

        assertSame(Color.WHITE, game.getCurrentPlayer());

        // White tries en passant too late: e5 -> d6.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(4, 4),
                        new Position(3, 5)));

        // The expired capture must not change the board.
        assertSame(whitePawn, board.getPiece(new Position(4, 4)));
        assertSame(blackPawn, board.getPiece(new Position(3, 4)));
        assertNull(board.getPiece(new Position(3, 5)));

        // Turn and game status remain unchanged.
        assertSame(Color.WHITE, game.getCurrentPlayer());
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    public void IT09PromotedQueenCanMoveInLaterTurns() {
        Board board = new Board();

        board.placePiece(new Piece(PieceType.KING, Color.WHITE),
                new Position(4, 0)); // e1
        board.placePiece(new Piece(PieceType.KING, Color.BLACK),
                new Position(7, 7)); // h8
        board.placePiece(new Piece(PieceType.PAWN, Color.WHITE),
                new Position(1, 6)); // b7
        board.placePiece(new Piece(PieceType.ROOK, Color.BLACK),
                new Position(0, 7)); // a8
        board.placePiece(new Piece(PieceType.PAWN, Color.BLACK),
                new Position(7, 5)); // h6

        Game game = new Game(board);

        final Piece originalPawn = board.getPiece(new Position(1, 6));
        final Piece originalRook = board.getPiece(new Position(0, 7));

        // 1. White: Ke1 -> f1.
        game.makeMove(new Position(4, 0), new Position(5, 0));

        // 2. Black: h6 -> h5.
        game.makeMove(new Position(7, 5), new Position(7, 4));

        // 3. White captures the rook and promotes: b7xa8=Q.
        game.makeMove(
                new Position(1, 6),
                new Position(0, 7),
                PieceType.QUEEN);

        Piece queen = board.getPiece(new Position(0, 7));

        assertEquals(PieceType.QUEEN, queen.getType());
        assertEquals(Color.WHITE, queen.getColor());
        assertNotSame(originalPawn, queen);
        assertNotSame(originalRook, queen);
        assertNull(board.getPiece(new Position(1, 6)));

        // The new queen checks the Black King on h8.
        assertTrue(game.isCurrentPlayerInCheck());
        assertSame(Color.BLACK, game.getCurrentPlayer());

        // 4. Black escapes check: Kh8 -> g7.
        game.makeMove(new Position(7, 7), new Position(6, 6));

        assertFalse(game.isCurrentPlayerInCheck());
        assertSame(Color.WHITE, game.getCurrentPlayer());

        // 5. White moves the newly promoted Queen: a8 -> a6.
        game.makeMove(new Position(0, 7), new Position(0, 5));

        assertSame(queen, board.getPiece(new Position(0, 5)));
        assertNull(board.getPiece(new Position(0, 7)));

        // The original White King and Black Pawn remain in place.
        assertEquals(PieceType.KING,
                board.getPiece(new Position(5, 0)).getType());
        assertEquals(PieceType.PAWN,
                board.getPiece(new Position(7, 4)).getType());

        assertSame(Color.BLACK, game.getCurrentPlayer());
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    public void IT10CheckIllegalMoveBlockingAndContinuedPlay() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // 1. White: e2 -> e4.
        game.makeMove(new Position(4, 1), new Position(4, 3));

        // 2. Black: e7 -> e5.
        game.makeMove(new Position(4, 6), new Position(4, 4));

        // 3. White: d2 -> d3.
        game.makeMove(new Position(3, 1), new Position(3, 2));

        // 4. Black: Bf8 -> b4+, checking the White King.
        game.makeMove(new Position(5, 7), new Position(1, 3));

        assertTrue(game.isCurrentPlayerInCheck());
        assertSame(Color.WHITE, game.getCurrentPlayer());

        Piece whitePawn = board.getPiece(new Position(0, 1));
        final Piece blackBishop = board.getPiece(new Position(1, 3));

        // 5. White attempts a2 -> a3 without resolving check.
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(
                        new Position(0, 1),
                        new Position(0, 2)));

        // Illegal move must not change the board or turn.
        assertSame(whitePawn, board.getPiece(new Position(0, 1)));
        assertNull(board.getPiece(new Position(0, 2)));
        assertSame(blackBishop, board.getPiece(new Position(1, 3)));
        assertSame(Color.WHITE, game.getCurrentPlayer());
        assertTrue(game.isCurrentPlayerInCheck());

        // 6. White blocks the Bishop: c2 -> c3.
        game.makeMove(new Position(2, 1), new Position(2, 2));

        assertFalse(game.isCurrentPlayerInCheck());
        assertSame(Color.BLACK, game.getCurrentPlayer());

        // 7. Black retreats the Bishop: b4 -> a5.
        game.makeMove(new Position(1, 3), new Position(0, 4));

        assertSame(blackBishop, board.getPiece(new Position(0, 4)));
        assertSame(Color.WHITE, game.getCurrentPlayer());

        // 8. The previously rejected pawn can now move: a2 -> a3.
        game.makeMove(new Position(0, 1), new Position(0, 2));

        assertSame(whitePawn, board.getPiece(new Position(0, 2)));
        assertNull(board.getPiece(new Position(0, 1)));

        // The game continues normally after resolving check.
        assertSame(Color.BLACK, game.getCurrentPlayer());
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
    }

}