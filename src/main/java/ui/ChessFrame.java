package ui;

import domain.Color;
import domain.Game;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;

/** The main window: shows the board, whose turn it is, and a "New Game" menu item. */
public class ChessFrame extends JFrame {
	private static final String WHITE_TO_MOVE = "White to move";
	private static final String BLACK_TO_MOVE = "Black to move";
	private static final int LABEL_PADDING = 6;

	private final Game game;
	private final BoardPanel boardPanel;
	private final JLabel turnLabel = new JLabel();

	public ChessFrame(Game game) {
		super("Chess");
		this.game = game;
		this.boardPanel = new BoardPanel(game, this::updateTurnLabel,
				this::showIllegalMove);

		turnLabel.setBorder(BorderFactory.createEmptyBorder(
				LABEL_PADDING, LABEL_PADDING, LABEL_PADDING, LABEL_PADDING));
		updateTurnLabel();

		setJMenuBar(createMenuBar());
		add(turnLabel, BorderLayout.NORTH);
		add(boardPanel, BorderLayout.CENTER);
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

	private void updateTurnLabel() {
		turnLabel.setText(
				game.getCurrentPlayer() == Color.WHITE
						? WHITE_TO_MOVE : BLACK_TO_MOVE);
	}

	// Use case 2, flow 4a: tell the player why the move was rejected.
	private void showIllegalMove(String message) {
		JOptionPane.showMessageDialog(this, message, "Illegal Move",
				JOptionPane.ERROR_MESSAGE);
	}

	// Use case 1a: abandoning a game in progress needs the player's confirmation.
	private void confirmNewGame() {
		int choice = JOptionPane.showConfirmDialog(this,
				"Abandon the current game and start a new one?",
				"New Game", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			game.startNewGame();
			// The board object is reused (see Game G06), so clearing and repainting
			// shows the reset.
			boardPanel.clearSelection();
			updateTurnLabel();
		}
	}
}
