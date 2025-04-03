package ru.nsu.lavitskaya.snake;

/**
 * Represents a food item in the game.
 */
public class Food {
    private final Point position;

    public Food(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }
}
