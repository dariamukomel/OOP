package ru.nsu.lavitskaya.snake;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link EnemySnake} class.
 * <p>
 * These tests cover the following scenarios:
 * <ul>
 *   <li>Enemy snake chooses a safe direction and moves toward the closest food.</li>
 *   <li>Enemy snake dies when no safe directions exist.</li>
 *   <li>Enemy snake moves toward the closest food (i.e. reduces the distance to it).</li>
 *   <li>Enemy snake dies upon collision with the player's snake (collision is detected with any
 *       part of the player's snake, including its tail).</li>
 * </ul>
 * </p>
 */
public class EnemySnakeTest {

    /**
     * Tests that the enemy snake chooses a safe direction to reach the food
     * when an obstacle is present directly to its right.
     * <p>
     * In this test, an enemy snake is placed at (5,5), the food is at (7,5),
     * and an obstacle is set at (6,5). This forces the enemy to avoid the direct path
     * to the right and choose an alternative (up, down or even left) so that its next head
     * does not coincide with the obstacle.
     * </p>
     */
    @Test
    public void testChoosesSafeDirection() {
        EnemySnake enemy = new EnemySnake(new Point(5, 5));
        List<EnemySnake> enemySnakes = new ArrayList<>();
        enemySnakes.add(enemy);
        List<Food> foodList = new ArrayList<>();
        foodList.add(new Food(new Point(7, 5)));
        List<Obstacle> obstacles = new ArrayList<>();
        List<Point> obstaclePoints = new ArrayList<>();
        obstaclePoints.add(new Point(6, 5));
        obstacles.add(new Obstacle(obstaclePoints));
        Snake playerSnake = new Snake(new Point(0, 0));
        enemy.update(10, 10, foodList, obstacles, playerSnake, enemySnakes);

        assertTrue(enemy.isAlive());

        assertNotEquals(new Point(6, 5), enemy.getHead());
    }

    /**
     * Tests that the enemy snake dies when no safe direction is available.
     * <p>
     * In this test, the enemy snake is located in the top-left corner (0,0) of a 3x3 board.
     * The only possible moves from this position are to the right (1,0) and down (0,1).
     * Obstacles are placed in both these positions, so no safe moves are available.
     * As a result, after updating, the enemy snake should be marked as dead.
     * </p>
     */
    @Test
    public void testDiesWhenNoSafeDirections() {
        EnemySnake enemy = new EnemySnake(new Point(0, 0));
        List<EnemySnake> enemySnakes = new ArrayList<>();
        enemySnakes.add(enemy);
        List<Food> foodList = new ArrayList<>();
        foodList.add(new Food(new Point(2, 2)));
        List<Obstacle> obstacles = new ArrayList<>();
        List<Point> obsPointsRight = new ArrayList<>();
        obsPointsRight.add(new Point(1, 0));
        obstacles.add(new Obstacle(obsPointsRight));
        List<Point> obsPointsDown = new ArrayList<>();
        obsPointsDown.add(new Point(0, 1));
        obstacles.add(new Obstacle(obsPointsDown));
        Snake playerSnake = new Snake(new Point(2, 0));
        enemy.update(3, 3, foodList, obstacles, playerSnake, enemySnakes);

        assertFalse(enemy.isAlive());
    }

    /**
     * Tests that the enemy snake chooses the closest food.
     * The enemy snake starts at (5,5) with two food items:
     * one at (7,5) (closer) and one at (9,5) (farther).
     * After updating, the enemy snake should move in a way that reduces the distance to the closer
     *     food.
     */
    @Test
    public void testChoosesClosestFood() {
        EnemySnake enemy = new EnemySnake(new Point(5, 5));
        List<EnemySnake> enemySnakes = new ArrayList<>();
        enemySnakes.add(enemy);
        List<Food> foodList = new ArrayList<>();
        foodList.add(new Food(new Point(7, 5)));
        foodList.add(new Food(new Point(9, 5)));
        List<Obstacle> obstacles = new ArrayList<>();
        Snake playerSnake = new Snake(new Point(0, 0));
        int initialDistance = Math.abs(enemy.getHead().coordX - 7)
                + Math.abs(enemy.getHead().coordY - 5);
        enemy.update(10, 10, foodList, obstacles, playerSnake, enemySnakes);
        int newDistance = Math.abs(enemy.getHead().coordX - 7)
                + Math.abs(enemy.getHead().coordY - 5);

        assertTrue(newDistance < initialDistance);
    }

    /**
     * Tests that the enemy snake dies upon collision with the player's snake.
     * The player's snake is positioned so that the enemy's next move results in a collision.
     */
    @Test
    public void testDiesOnPlayerCollision() {
        EnemySnake enemy = new EnemySnake(new Point(5, 5));
        List<EnemySnake> enemySnakes = new ArrayList<>();
        enemySnakes.add(enemy);
        List<Food> foodList = new ArrayList<>();
        foodList.add(new Food(new Point(9, 5)));
        List<Obstacle> obstacles = new ArrayList<>();
        Snake playerSnake = new Snake(new Point(6, 5));
        playerSnake.getBody().add(new Point(6, 6));
        enemy.update(10, 10, foodList, obstacles, playerSnake, enemySnakes);
        assertFalse(enemy.isAlive());
    }
}
