package ru.nsu.lavitskaya.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SatedSnake} behavior.
 */
public class SatedSnakeTest {
    private static final int MAP_ROWS = 3;
    private static final int MAP_COLS = 3;
    private SatedSnake satedSnake;
    private Snake playerSnake;
    private List<Food> foodList;
    private List<Obstacle> obstacles;
    private List<EnemySnake> enemySnakes;

    @BeforeEach
    void setUp() {
        satedSnake = new SatedSnake(new Point(1, 1));
        playerSnake = new Snake(new Point(-1, -1));
        foodList = new ArrayList<>();
        obstacles = new ArrayList<>();
        enemySnakes = new ArrayList<>();
        enemySnakes.add(satedSnake);
    }

    /**
     * If the snake has no safe directions (all blocked by walls or obstacles), it should die.
     */
    @Test
    void testNoSafeMovesDies() {
        obstacles.add(new Obstacle(List.of(new Point(1, 0))));
        obstacles.add(new Obstacle(List.of(new Point(1, 2))));
        obstacles.add(new Obstacle(List.of(new Point(0, 1))));
        obstacles.add(new Obstacle(List.of(new Point(2, 1))));
        satedSnake.update(MAP_ROWS, MAP_COLS, foodList, obstacles, playerSnake, enemySnakes);
        assertEquals(SnakeType.DEAD, satedSnake.getType());
    }

    /**
     * If the only safe move leads into the player snake, the sated snake should die.
     */
    @Test
    void testSafeMoveIntoPlayerDies() {
        obstacles.add(new Obstacle(List.of(new Point(1, 2))));
        obstacles.add(new Obstacle(List.of(new Point(0, 1))));
        obstacles.add(new Obstacle(List.of(new Point(2, 1))));
        playerSnake = new Snake(new Point(1, 0));
        enemySnakes.clear();
        enemySnakes.add(satedSnake);
        satedSnake.update(MAP_ROWS, MAP_COLS, foodList, obstacles, playerSnake, enemySnakes);
        assertEquals(SnakeType.DEAD, satedSnake.getType());
    }

    /**
     * If there is exactly one safe move with no food, the snake should move there and remain alive.
     */
    @Test
    void testSingleSafeMoveMoves() {
        obstacles.add(new Obstacle(List.of(new Point(1, 0))));
        obstacles.add(new Obstacle(List.of(new Point(0, 1))));
        obstacles.add(new Obstacle(List.of(new Point(2, 1))));
        playerSnake = new Snake(new Point(-1, -1));
        enemySnakes.clear();
        enemySnakes.add(satedSnake);
        Point initialHead = satedSnake.getHead();
        satedSnake.update(MAP_ROWS, MAP_COLS, foodList, obstacles, playerSnake, enemySnakes);
        Point newHead = satedSnake.getHead();
        assertEquals(SnakeType.SATED, satedSnake.getType());
        assertEquals(initialHead.move(Direction.DOWN), newHead);
        assertEquals(1, satedSnake.getBody().size());
    }

    /**
     * If food is on the only safe move, the snake should grow by one.
     */
    @Test
    void testEatFoodGrows() {
        obstacles.add(new Obstacle(List.of(new Point(1, 1).move(Direction.UP))));
        obstacles.add(new Obstacle(List.of(new Point(1, 1).move(Direction.DOWN))));
        obstacles.add(new Obstacle(List.of(new Point(1, 1).move(Direction.LEFT))));
        foodList.add(new Food(new Point(2, 1)));
        playerSnake = new Snake(new Point(-1, -1));
        enemySnakes.clear();
        enemySnakes.add(satedSnake);
        int initialLength = satedSnake.length();
        satedSnake.update(MAP_ROWS, MAP_COLS, foodList, obstacles, playerSnake, enemySnakes);
        assertEquals(SnakeType.SATED, satedSnake.getType());
        assertEquals(initialLength + 1, satedSnake.length());
    }
}
