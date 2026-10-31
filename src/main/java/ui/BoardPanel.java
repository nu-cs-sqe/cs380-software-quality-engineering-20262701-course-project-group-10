package ui;

import domain.Board;
import domain.Color;
import domain.Piece;
import domain.Position;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Draws the 8x8 board and its pieces by reading the Board. White is at the bottom. */
public class BoardPanel extends JPanel {
    private static final int SIZE = 8;
    private static final int DEFAULT_SQUARE_SIZE = 64;
    private static final java.awt.Color LIGHT_SQUARE = new java.awt.Color(240, 217, 181);
    private static final java.awt.Color DARK_SQUARE = new java.awt.Color(181, 136, 99);

    private final Board board;

    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(SIZE * DEFAULT_SQUARE_SIZE, SIZE * DEFAULT_SQUARE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int square = Math.min(getWidth(), getHeight()) / SIZE;
        // Segoe UI Symbol has the chess glyphs on Windows; Java falls back to Dialog elsewhere.
        g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, square * 3 / 4));

        for (int rank = 0; rank < SIZE; rank++) {
            for (int file = 0; file < SIZE; file++) {
                // Rank 8 is drawn on the top row, so screen rows run opposite to ranks.
                int x = file * square;
                int y = (SIZE - 1 - rank) * square;

                // a1 (file 0, rank 0) is dark, h1 (file 7, rank 0) is light.
                g2.setColor((file + rank) % 2 == 0 ? DARK_SQUARE : LIGHT_SQUARE);
                g2.fillRect(x, y, square, square);

                Piece piece = board.getPiece(new Position(file, rank));
                if (piece != null) {
                    drawPiece(g2, piece, x, y, square);
                }
            }
        }
    }

    private void drawPiece(Graphics2D g2, Piece piece, int x, int y, int square) {
        String symbol = symbolFor(piece);
        FontMetrics metrics = g2.getFontMetrics();
        int textX = x + (square - metrics.stringWidth(symbol)) / 2;
        int textY = y + (square - metrics.getHeight()) / 2 + metrics.getAscent();
        g2.setColor(java.awt.Color.BLACK);
        g2.drawString(symbol, textX, textY);
    }

    private static String symbolFor(Piece piece) {
        boolean white = piece.getColor() == Color.WHITE;
        switch (piece.getType()) {
            case KING:
                return white ? "♔" : "♚";
            case QUEEN:
                return white ? "♕" : "♛";
            case ROOK:
                return white ? "♖" : "♜";
            case BISHOP:
                return white ? "♗" : "♝";
            case KNIGHT:
                return white ? "♘" : "♞";
            case PAWN:
                return white ? "♙" : "♟";
            default:
                throw new IllegalStateException("Unknown piece type: " + piece.getType());
        }
    }
}
