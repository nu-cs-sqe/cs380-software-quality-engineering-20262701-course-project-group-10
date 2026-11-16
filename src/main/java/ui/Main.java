package ui;

import domain.Board;
import domain.Game;
import javax.swing.SwingUtilities;

/** Starts a new chess game and opens its Swing window. */
public class Main {
    public static void main(String[] args) {
        Game game = new Game(new Board());
        game.startNewGame();
        SwingUtilities.invokeLater(() -> new ChessFrame(game).setVisible(true));
    }
}
