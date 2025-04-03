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
        Point pUp = p.move(Direction.UP);
        assertEquals(5, pUp.x);
        assertEquals(4, pUp.y);
    }

    @Test
    public void testMoveDown() {
        Point p = new Point(5, 5);
        Point pDown = p.move(Direction.DOWN);
        assertEquals(5, pDown.x);
        assertEquals(6, pDown.y);
    }

    @Test
    public void testMoveLeft() {
        Point p = new Point(5, 5);
        Point pLeft = p.move(Direction.LEFT);
        assertEquals(4, pLeft.x);
        assertEquals(5, pLeft.y);
    }

    @Test
    public void testMoveRight() {
        Point p = new Point(5, 5);
        Point pRight = p.move(Direction.RIGHT);
        assertEquals(6, pRight.x);
        assertEquals(5, pRight.y);
    }

    @Test
    public void testEquality() {
        Point p1 = new Point(5, 5);
        Point p2 = new Point(5, 5);
        assertEquals(p1, p2);
    }

}