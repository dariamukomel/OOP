package ru.nsu.lavitskaya.snake;

import java.util.List;

/**
 * Represents an obstacle on the game board, defined by a list of points.
 */
public class Obstacle {
    private final List<Point> cells;

    public Obstacle(List<Point> cells) {
        this.cells = cells;
    }

    public List<Point> getCells() {
        return cells;
    }
}
