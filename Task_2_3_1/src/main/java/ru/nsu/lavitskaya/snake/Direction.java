package ru.nsu.lavitskaya.snake;

/**
 * Enum representing possible movement directions for the snake.
 */
public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    /**
     * Checks if the specified direction is opposite to the current direction.
     *
     * @param other the direction to compare with
     * @return true if the given direction is opposite, false otherwise
     */
    public boolean isOpposite(Direction other) {
        return (this == UP && other == DOWN) ||
                (this == DOWN && other == UP) ||
                (this == LEFT && other == RIGHT) ||
                (this == RIGHT && other == LEFT);
    }
}
