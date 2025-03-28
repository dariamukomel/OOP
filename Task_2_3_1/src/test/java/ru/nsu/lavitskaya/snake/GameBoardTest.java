package ru.nsu.lavitskaya.snake;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Test
    public void testGameOverWhenSnakeHitsWall() {
        int rows = 10;
        int cols = 10;
        int foodCount = 0;
        int targetLength = 5;
        int obstaclesCount = 0;

        GameBoard board = new GameBoard(rows, cols, foodCount, targetLength, obstaclesCount);

        int iterations = 0;
        while (iterations < 5) {
            board.update();
            iterations++;
        }

        assertTrue(board.isGameOver());
    }


}