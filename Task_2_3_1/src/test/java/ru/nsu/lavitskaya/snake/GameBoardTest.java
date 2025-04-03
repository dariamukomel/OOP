package ru.nsu.lavitskaya.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Unit tests for the {@link GameBoard} class.
 * <p>
 * These tests verify that the game board correctly detects game over conditions,
 * food consumption increases the snake's length, collisions with obstacles result in a game over,
 * and win conditions are met when the snake reaches the target length.
 * </p>
 */
class GameBoardTest {

    @Test
    public void testGameOverWhenSnakeHitsWall() {
        GameBoard board = new GameBoard(10, 10, 0,
                5, 0);

        int iterations = 0;
        while (iterations < 5) {
            board.update();
            iterations++;
        }

        assertTrue(board.isGameOver());
    }

    @Test
    public void testEatFoodIncreasesSnakeLength() {
        GameBoard board = new GameBoard(10, 10, 1, 100,
                0);
        Snake snake = board.getSnake();
        Food food = new Food(new Point(snake.getHead().x + 1, snake.getHead().y));
        board.getFoodList().clear();
        board.getFoodList().add(food);
        int initialLength = snake.length();
        board.update();
        assertEquals(initialLength + 1, snake.length());
    }

    @Test
    public void testCollisionWithObstacle() {
        GameBoard board = new GameBoard(10, 10, 1, 10,
                1);
        board.getObstacles().clear();
        Snake snake = board.getSnake();
        Point head= snake.getHead();
        Obstacle obstacle = new Obstacle(List.of(new Point(head.x + 6, head.y)));
        board.getObstacles().add(obstacle);
        for(int i = 0; i <= 6; i++) {
            board.update();
        }
        assertTrue(board.isGameOver());
    }

    @Test
    public void testWinCondition() {
        GameBoard board = new GameBoard(10, 10, 1, 3,
                1);
        board.getFoodList().clear();
        Snake snake = board.getSnake();
        Point foodPos1 = snake.getNextHead();
        board.getFoodList().add(new Food(foodPos1));
        board.update();
        board.getFoodList().clear();
        Point foodPos2 = snake.getNextHead();
        board.getFoodList().add(new Food(foodPos2));
        board.update();
        assertTrue(board.isGameWon());
    }

}