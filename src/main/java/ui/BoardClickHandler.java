package ui;

import domain.Game;
import domain.Position;

import java.util.Objects;

public class BoardClickHandler {
    private final Game game;
    private Position selected;

    public BoardClickHandler(Game game) {
        this.game = Objects.requireNonNull(game, "game");
    }

    public Position getSelected() {
        return selected;
    }
}
