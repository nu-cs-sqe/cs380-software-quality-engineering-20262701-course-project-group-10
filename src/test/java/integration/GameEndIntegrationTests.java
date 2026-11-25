
package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Integration tests for game end conditions using real components. */
public class GameEndIntegrationTests {

    private Position square(String name) {
        return new Position(name.charAt(0) - 'a',
                name.charAt(1) - '1');
    }

    private void move(Game game, String from, String to) {
        game.makeMove(square(from), square(to));
    }

    @Test
    public void GET01FoolsMateEndsTheGame() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // White: f2-f3.
        move(game, "f2", "f3");

        // Black: e7-e5.
        move(game, "e7", "e5");

        // White: g2-g4.
        move(game, "g2", "g4");

        // Black: Qd8-h4#.
        move(game, "d8", "h4");

        // Black wins by checkmate.
        assertSame(GameStatus.CHECKMATE, game.getStatus());
        assertEquals(Optional.of(Color.BLACK), game.getWinner());
        assertTrue(game.isGameOver());

        // White is checkmated.
        assertSame(Color.WHITE, game.getCurrentPlayer());
        assertTrue(game.isCurrentPlayerInCheck());
        assertFalse(game.hasAnyLegalMove());

        // The Black Queen is on h4.
        Piece queen = board.getPiece(square("h4"));
        assertSame(PieceType.QUEEN, queen.getType());
        assertSame(Color.BLACK, queen.getColor());

        // Its previous square is empty.
        assertNull(board.getPiece(square("d8")));
    }

    @Test
    public void GET02StalemateEndsWithoutWinner() {
        Board board = new Board();

        // Construct a valid position one move before stalemate.
        board.placePiece(
                new Piece(PieceType.KING, Color.WHITE),
                square("c6"));
        board.placePiece(
                new Piece(PieceType.QUEEN, Color.WHITE),
                square("b5"));
        board.placePiece(
                new Piece(PieceType.KING, Color.BLACK),
                square("a8"));

        Game game = new Game(board);

        // Before the move, the game is in progress.
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
        assertFalse(game.isGameOver());

        // White: Qb5-b6, producing stalemate.
        move(game, "b5", "b6");

        // Black has no legal moves but is not in check.
        assertSame(Color.BLACK, game.getCurrentPlayer());
        assertFalse(game.isCurrentPlayerInCheck());
        assertFalse(game.hasAnyLegalMove());

        // Stalemate has no winner.
        assertSame(GameStatus.STALEMATE, game.getStatus());
        assertTrue(game.getWinner().isEmpty());
        assertTrue(game.isGameOver());

        // Check the final board.
        assertSame(PieceType.QUEEN,
                board.getPiece(square("b6")).getType());
        assertNull(board.getPiece(square("b5")));
        assertSame(PieceType.KING,
                board.getPiece(square("a8")).getType());
    }

    @Test
    public void GET03ResignationEndsTheGame() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // Play normal opening moves.
        move(game, "e2", "e4");
        move(game, "e7", "e5");
        move(game, "g1", "f3");

        assertSame(Color.BLACK, game.getCurrentPlayer());
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());

        // Black resigns; White should win.
        game.resign(Color.BLACK);

        assertSame(GameStatus.RESIGNED, game.getStatus());
        assertEquals(Optional.of(Color.WHITE), game.getWinner());
        assertTrue(game.isGameOver());

        // Resignation should not change the board.
        Piece knight = board.getPiece(square("f3"));
        assertSame(PieceType.KNIGHT, knight.getType());
        assertSame(Color.WHITE, knight.getColor());
        assertNull(board.getPiece(square("g1")));

        // Resignation should not advance the turn.
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }

    @Test
    public void GET04NoMovesAcceptedAfterResignation() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // White resigns immediately.
        game.resign(Color.WHITE);

        assertSame(GameStatus.RESIGNED, game.getStatus());
        assertEquals(Optional.of(Color.BLACK), game.getWinner());
        assertTrue(game.isGameOver());

        // Save the original White pawn and turn.
        final Piece whitePawn = board.getPiece(square("e2"));

        // e2-e4 is normally legal, but the game has ended.
        assertThrows(IllegalArgumentException.class,
                () -> move(game, "e2", "e4"));

        // The rejected move must not change the board.
        assertSame(whitePawn, board.getPiece(square("e2")));
        assertNull(board.getPiece(square("e4")));

        // The result and current player must remain unchanged.
        assertSame(Color.WHITE, game.getCurrentPlayer());
        assertSame(GameStatus.RESIGNED, game.getStatus());
        assertEquals(Optional.of(Color.BLACK), game.getWinner());
        assertTrue(game.isGameOver());
    }

    @Test
    public void GET05NewGameResetsFinishedGameState() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // End the first game.
        game.resign(Color.WHITE);

        assertTrue(game.isGameOver());
        assertSame(GameStatus.RESIGNED, game.getStatus());

        // Start a new game using the same Game object.
        game.startNewGame();

        // All result state must be reset.
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
        assertTrue(game.getWinner().isEmpty());
        assertFalse(game.isGameOver());
        assertSame(Color.WHITE, game.getCurrentPlayer());

        // Normal moves must work again.
        move(game, "e2", "e4");

        assertSame(PieceType.PAWN,
                board.getPiece(square("e4")).getType());
        assertNull(board.getPiece(square("e2")));
        assertSame(Color.BLACK, game.getCurrentPlayer());
        assertSame(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    public void GET06NoMovesAfterCheckmate() {
        Board board = new Board();
        Game game = new Game(board);
        game.startNewGame();

        // Fool's Mate.
        move(game, "f2", "f3");
        move(game, "e7", "e5");
        move(game, "g2", "g4");
        move(game, "d8", "h4");

        assertSame(GameStatus.CHECKMATE, game.getStatus());
        assertTrue(game.isGameOver());

        final Piece whitePawn = board.getPiece(square("a2"));

        // White cannot move after checkmate.
        assertThrows(IllegalArgumentException.class,
                () -> move(game, "a2", "a3"));

        assertSame(whitePawn, board.getPiece(square("a2")));
        assertNull(board.getPiece(square("a3")));
        assertSame(GameStatus.CHECKMATE, game.getStatus());
        assertEquals(Optional.of(Color.BLACK), game.getWinner());
        assertSame(Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    public void GET07NoMovesAfterStalemate() {
        Board board = new Board();

        board.placePiece(new Piece(PieceType.KING, Color.WHITE),
                square("c6"));
        board.placePiece(new Piece(PieceType.QUEEN, Color.WHITE),
                square("b5"));
        board.placePiece(new Piece(PieceType.KING, Color.BLACK),
                square("a8"));

        Game game = new Game(board);

        // White produces stalemate.
        move(game, "b5", "b6");

        assertSame(GameStatus.STALEMATE, game.getStatus());
        assertTrue(game.isGameOver());

        final Piece blackKing = board.getPiece(square("a8"));

        // Moves must be rejected after stalemate.
        assertThrows(IllegalArgumentException.class,
                () -> move(game, "a8", "a7"));

        assertSame(blackKing, board.getPiece(square("a8")));
        assertNull(board.getPiece(square("a7")));
        assertSame(GameStatus.STALEMATE, game.getStatus());
        assertTrue(game.getWinner().isEmpty());
        assertSame(Color.BLACK, game.getCurrentPlayer());
    }
}
