package ui;

import domain.Board;
import domain.Game;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(new Board());
        game.startNewGame();
        // Swing components must be created on the event dispatch thread.
        SwingUtilities.invokeLater(() -> new ChessFrame(game).setVisible(true));
    }
}
