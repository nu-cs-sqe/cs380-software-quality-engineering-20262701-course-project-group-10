package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class GameEndReplyContractTests {
    // Simulates the search result only, not an actual en passant or promotion move.
    private void assertReportedReply(boolean reply, boolean inCheck, GameStatus expected) {
        Board board = new Board();
        board.placePiece(new Piece(PieceType.KING, Color.WHITE), new Position(2, 5));
        board.placePiece(new Piece(PieceType.KING, Color.BLACK), new Position(0, 7));
        Position from = new Position(1, inCheck ? 5 : 4);
        Position to = new Position(1, inCheck ? 6 : 5);
        board.placePiece(new Piece(PieceType.QUEEN, Color.WHITE), from);
        final Board expectedBoard = board.copy();
        expectedBoard.movePiece(from, to);

        Game game = EasyMock.partialMockBuilder(Game.class)
                .withConstructor(board)
                .addMockedMethod("hasAnyLegalMove")
                .createStrictMock();
        EasyMock.expect(game.hasAnyLegalMove()).andReturn(reply).once();
        EasyMock.replay(game);

        game.makeMove(from, to);

        EasyMock.verify(game);
        assertSame(expected, game.getStatus());
        Optional<Color> winner = expected == GameStatus.CHECKMATE
                ? Optional.of(Color.WHITE) : Optional.empty();
        assertEquals(winner, game.getWinner());
        assertEquals(expected != GameStatus.IN_PROGRESS, game.isGameOver());
        assertEquals(inCheck, game.isCurrentPlayerInCheck());
        assertSame(Color.BLACK, game.getCurrentPlayer());
        MoveRuleAssertions.assertSameSquares(expectedBoard, board);
        for (Color color : Color.values()) {
            assertTrue(game.canCastleKingSide(color));
            assertTrue(game.canCastleQueenSide(color));
        }
    }

    @Test
    public void GE38ReportedFutureReplyPreventsCheckmate() {
        assertReportedReply(true, true, GameStatus.IN_PROGRESS);
    }

    @Test
    public void GE39ReportedFutureReplyPreventsStalemate() {
        assertReportedReply(true, false, GameStatus.IN_PROGRESS);
    }

    @Test
    public void GE40NoReportedReplyWhileCheckedIsMate() {
        assertReportedReply(false, true, GameStatus.CHECKMATE);
    }

    @Test
    public void GE41NoReportedReplyWithoutCheckIsStalemate() {
        assertReportedReply(false, false, GameStatus.STALEMATE);
    }
}
