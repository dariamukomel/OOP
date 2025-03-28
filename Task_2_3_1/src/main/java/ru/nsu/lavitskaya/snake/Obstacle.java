package ru.nsu.lavitskaya.snake;

import java.util.List;

public class Obstacle {
    private final List<Point> cells;

    public Obstacle(List<Point> cells) {
        this.cells = cells;
    }

    public List<Point> getCells() {
        return cells;
    }
}
