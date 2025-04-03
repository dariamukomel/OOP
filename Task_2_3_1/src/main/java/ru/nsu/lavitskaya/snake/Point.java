package ru.nsu.lavitskaya.snake;

import java.util.Objects;

/**
 * Represents a point (cell) on the game board.
 */
public class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a new Point moved in the specified direction.
     *
     * @param direction the direction in which to move
     * @return a new Point shifted in the given direction
     */
    public Point move(Direction direction) {
        return switch (direction) {
            case UP    -> new Point(x, y - 1);
            case DOWN  -> new Point(x, y + 1);
            case LEFT  -> new Point(x - 1, y);
            case RIGHT -> new Point(x + 1, y);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
