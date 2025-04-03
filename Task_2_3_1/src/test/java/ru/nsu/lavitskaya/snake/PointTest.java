package ru.nsu.lavitskaya.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Point} class.
 * <p>
 * These tests ensure that movement in each direction and equality checking work correctly.
 * </p>
 */
class PointTest {
    @Test
    public void testMoveUp() {
        Point p = new Point(5, 5);
        Point up = p.move(Direction.UP);
        assertEquals(5, up.coordX);
        assertEquals(4, up.coordY);
    }

    @Test
    public void testMoveDown() {
        Point p = new Point(5, 5);
        Point down = p.move(Direction.DOWN);
        assertEquals(5, down.coordX);
        assertEquals(6, down.coordY);
    }

    @Test
    public void testMoveLeft() {
        Point p = new Point(5, 5);
        Point left = p.move(Direction.LEFT);
        assertEquals(4, left.coordX);
        assertEquals(5, left.coordY);
    }

    @Test
    public void testMoveRight() {
        Point p = new Point(5, 5);
        Point right = p.move(Direction.RIGHT);
        assertEquals(6, right.coordX);
        assertEquals(5, right.coordY);
    }

    @Test
    public void testEquality() {
        Point p1 = new Point(5, 5);
        Point p2 = new Point(5, 5);
        assertEquals(p1, p2);
    }

}