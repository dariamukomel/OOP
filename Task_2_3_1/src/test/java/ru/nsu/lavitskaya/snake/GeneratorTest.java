package ru.nsu.lavitskaya.snake;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Generator} class.
 * <p>
 * These tests verify that the generator creates food and obstacles at valid positions,
 * and that obstacles do not overlap with already occupied cells.
 * </p>
 */
class GeneratorTest {

    @Test
    public void testGenerateOneFood() {
        Generator generator = new Generator(10, 10);
        Set<Point> forbidden = new HashSet<>();
        forbidden.add(new Point(5, 5));
        Food food = generator.generateOneFood(forbidden);
        assertNotNull(food);
        assertFalse(forbidden.contains(food.getPosition()));
    }

    @Test
    public void testGenerateObstacle() {
        Generator generator = new Generator(10, 10);
        Set<Point> occupied = new HashSet<>();
        occupied.add(new Point(5, 5));
        Obstacle obstacle = generator.generateObstacle(occupied);
        assertNotNull(obstacle);
        for (Point p : obstacle.getCells()) {
            assertFalse(new Point(5, 5).equals(p));
        }
    }

    @Test
    public void testObstacleNoOverlap() {
        Generator generator = new Generator(10, 10);
        Set<Point> occupied = new HashSet<>();
        Obstacle obstacle1 = generator.generateObstacle(occupied);
        assertNotNull(obstacle1);
        occupied.addAll(obstacle1.getCells());
        Obstacle obstacle2 = generator.generateObstacle(occupied);
        if (obstacle2 != null) {
            for (Point p : obstacle2.getCells()) {
                assertFalse(obstacle1.getCells().contains(p));
            }
        }
    }

    @Test
    void testGenerateSatedSnake() {
        Set<Point> forbidden = new HashSet<>();
        forbidden.add(new Point(0, 0));
        forbidden.add(new Point(9, 9));
        Generator generator = new Generator(10, 10);
        SatedSnake snake = generator.generateSatedSnake(forbidden);
        assertNotNull(snake);
        Point head = snake.getHead();
        assertFalse(forbidden.contains(head));
        int length = snake.length();
        assertTrue(length >= 3 && length <= 5);
        Set<Point> seen = new HashSet<>();
        for (Point p : snake.getBody()) {
            assertFalse(forbidden.contains(p));
            assertTrue(seen.add(p));
        }
    }

}