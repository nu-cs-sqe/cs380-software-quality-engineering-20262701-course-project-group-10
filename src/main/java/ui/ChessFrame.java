package ui;

import domain.Game;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/** The main window: shows the board and offers a "New Game" menu item. */
public class ChessFrame extends JFrame {
    private final Game game;
    private final BoardPanel boardPanel;

    public ChessFrame(Game game) {
        super("Chess");
        this.game = game;
        this.boardPanel = new BoardPanel(game.getBoard());

        setJMenuBar(createMenuBar());
        add(boardPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private JMenuBar createMenuBar() {
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(event -> confirmNewGame());

        JMenu gameMenu = new JMenu("Game");
        gameMenu.add(newGame);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(gameMenu);
        return menuBar;
    }

    // Use case 1a: abandoning a game in progress needs the player's confirmation.
    private void confirmNewGame() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Abandon the current game and start a new one?",
                "New Game", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            game.startNewGame();
            // The board object is reused (see Game G06), so a repaint shows the reset.
            boardPanel.repaint();
        }
    }
}
