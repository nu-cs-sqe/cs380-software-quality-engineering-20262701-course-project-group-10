package ui;

import domain.Game;
import domain.Position;

public class BoardClickHandler {
    private final Game game;
    private Position selected;

    public BoardClickHandler(Game game) {
        this.game = game;
    }

    public Position getSelected() {
        return selected;
    }
}
