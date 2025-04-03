package ru.nsu.lavitskaya.snake;

import java.util.Objects;

/**
 * Represents a point (cell) on the game board.
 */
public class Point {
    public final int coordX;
    public final int coordY;

    public Point(int x, int y) {
        this.coordX = x;
        this.coordY = y;
    }

    /**
     * Returns a new Point moved in the specified direction.
     *
     * @param direction the direction in which to move
     * @return a new Point shifted in the given direction
     */
    public Point move(Direction direction) {
        return switch (direction) {
            case UP    -> new Point(coordX, coordY - 1);
            case DOWN  -> new Point(coordX, coordY + 1);
            case LEFT  -> new Point(coordX - 1, coordY);
            case RIGHT -> new Point(coordX + 1, coordY);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Point)) {
            return false;
        }
        Point p = (Point) o;
        return coordX == p.coordX && coordY == p.coordY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordX, coordY);
    }
}
