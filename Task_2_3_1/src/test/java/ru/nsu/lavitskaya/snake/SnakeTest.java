package ru.nsu.lavitskaya.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Snake} class.
 * <p>
 * These tests verify that the snake moves correctly with or without growing,
 * and that self-collision detection works as expected.
 * </p>
 */
class SnakeTest {
    @Test
    public void testSnakeMoveWithGrow() {
        Snake snake = new Snake(new Point(5, 5));
        int initialLength = snake.length();
        snake.move(true);
        assertEquals(initialLength + 1, snake.length());
    }

    @Test
    public void testSnakeMoveWithoutGrow() {
        Snake snake = new Snake(new Point(5, 5));
        int initialLength = snake.length();
        snake.move(false);
        assertEquals(initialLength, snake.length());
    }

    @Test
    public void testSelfCollision() {
        Snake snake = new Snake(new Point(5, 5));
        snake.setDirection(Direction.RIGHT);
        snake.move(true);
        snake.setDirection(Direction.DOWN);
        snake.move(true);
        snake.setDirection(Direction.LEFT);
        snake.move(true);
        snake.setDirection(Direction.UP);
        snake.move(true);
        assertTrue(snake.checkSelfCollision());
    }

}