package ui;

import domain.Game;
import domain.MoveError;
import domain.PieceType;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

/** Displays language selection, the chessboard, and localized game controls. */
public class ChessFrame extends JFrame {
    private static final int LABEL_PADDING = 6;
    private static final int START_PADDING = 60;
    private static final int START_GAP = 12;
    private static final int START_ROWS = 3;
    private static final PieceType[] PROMOTIONS = {
            PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT
    };

    private final Game game;
    private final BoardPanel boardPanel;
    private final Map<Locale, String> languages = Messages.languageOptions();
    private final List<Locale> locales = new ArrayList<>(languages.keySet());
    private final JPanel screens = new JPanel(new CardLayout());
    private final JLabel turnLabel = new JLabel();
    private final JLabel languagePrompt = new JLabel();
    private final JButton startButton = new JButton();
    private final JComboBox<String> languageChoice =
            new JComboBox<>(languages.values().toArray(new String[0]));
    private final JMenu gameMenu = new JMenu();
    private final JMenu languageMenu = new JMenu();
    private final JMenuItem newGameItem = new JMenuItem();
    private Messages messages = new Messages(Locale.ENGLISH);
    private boolean started;

    public ChessFrame(Game game) {
        this.game = game;
        languageChoice.setSelectedItem(languages.get(Locale.ENGLISH));
        boardPanel = new BoardPanel(game, this::refreshGameState,
                this::showIllegalMove, this::choosePromotion);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(
                LABEL_PADDING, LABEL_PADDING, LABEL_PADDING, LABEL_PADDING));
        JPanel boardScreen = new JPanel(new BorderLayout());
        boardScreen.add(turnLabel, BorderLayout.NORTH);
        boardScreen.add(boardPanel, BorderLayout.CENTER);
        screens.add(createStartPanel(), "start");
        screens.add(boardScreen, "board");
        setJMenuBar(createMenuBar());
        add(screens);
        refreshTexts();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createStartPanel() {
        JPanel panel = new JPanel(new GridLayout(START_ROWS, 1, 0, START_GAP));
        panel.setBorder(BorderFactory.createEmptyBorder(
                START_PADDING, START_PADDING, START_PADDING, START_PADDING));
        panel.add(languagePrompt);
        panel.add(languageChoice);
        panel.add(startButton);
        languageChoice.addActionListener(event -> changeLanguage());
        startButton.addActionListener(event -> startGame());
        return panel;
    }

    private JMenuBar createMenuBar() {
        newGameItem.addActionListener(event -> confirmNewGame());
        gameMenu.add(newGameItem);
        ButtonGroup group = new ButtonGroup();
        for (int index = 0; index < locales.size(); index++) {
            final int choice = index;
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(
                    languages.get(locales.get(index)), index == languageChoice.getSelectedIndex());
            item.addActionListener(event -> languageChoice.setSelectedIndex(choice));
            group.add(item);
            languageMenu.add(item);
        }
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(gameMenu);
        menuBar.add(languageMenu);
        return menuBar;
    }

    private void changeLanguage() {
        messages = new Messages(locales.get(languageChoice.getSelectedIndex()));
        refreshTexts();
    }

    private void refreshTexts() {
        setTitle(messages.get("app.title"));
        gameMenu.setText(messages.get("menu.game"));
        newGameItem.setText(messages.get("menu.newGame"));
        newGameItem.setEnabled(started);
        languageMenu.setText(messages.get("menu.language"));
        languagePrompt.setText(messages.get("start.language"));
        startButton.setText(messages.get("start.button"));
        turnLabel.setText(messages.turnText(game.getCurrentPlayer(),
                started && game.isCurrentPlayerInCheck()));
        for (int index = 0; index < languageMenu.getItemCount(); index++) {
            languageMenu.getItem(index).setSelected(index == languageChoice.getSelectedIndex());
        }
    }

    private void startGame() {
        game.startNewGame();
        started = true;
        boardPanel.clearSelection();
        ((CardLayout) screens.getLayout()).show(screens, "board");
        refreshTexts();
    }

    private void refreshGameState() {
        refreshTexts();
        if (game.isGameOver()) {
            showMessage(messages.resultText(game.getStatus(), game.getWinner()),
                    messages.get("result.title"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private PieceType choosePromotion() {
        String[] choices = new String[PROMOTIONS.length];
        for (int index = 0; index < PROMOTIONS.length; index++) {
            choices[index] = messages.get("piece." + PROMOTIONS[index].name());
        }
        int choice = JOptionPane.showOptionDialog(this, messages.get("promotion.prompt"),
                messages.get("promotion.title"), JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        return choice < 0 ? null : PROMOTIONS[choice];
    }

    private void showIllegalMove(MoveError error) {
        showMessage(messages.errorText(error), messages.get("error.title"),
                JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String text, String title, int type) {
        JOptionPane.showOptionDialog(this, text, title, JOptionPane.DEFAULT_OPTION, type,
                null, new String[] {messages.get("button.ok")}, messages.get("button.ok"));
    }

    private void confirmNewGame() {
        String[] choices = {messages.get("button.yes"), messages.get("button.no")};
        int choice = JOptionPane.showOptionDialog(this, messages.get("confirm.newGame"),
                messages.get("menu.newGame"), JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]);
        if (choice == 0) {
            startGame();
        }
    }
}
