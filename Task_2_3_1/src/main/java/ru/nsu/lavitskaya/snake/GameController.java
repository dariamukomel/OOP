package ru.nsu.lavitskaya.snake;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Controller class for the Snake Game UI.
 * Handles initialization, game loop, input events, and rendering.
 */
public class GameController {
    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label statusLabel;

    int rows;
    int cols;
    private static final int CELL_SIZE = 25;
    private int level = 1;
    private GameBoard gameBoard;
    private Timeline gameLoop;

    private int currentFoodCount;
    private int currentObstaclesCount;
    private int currentTargetLength;
    private int currentDelay;

    /**
     * Initializes the game and UI components.
     * Sets up the game board based on the current level and starts the game loop.
     */
    @FXML
    public void initialize() {
        rows = (int) (gameCanvas.getHeight() / CELL_SIZE);
        cols = (int) (gameCanvas.getWidth() / CELL_SIZE);

        currentFoodCount = (level < 3) ? (4 - level) : 1;
        currentObstaclesCount = Math.min(3 + (level - 1), 10);
        currentTargetLength = 10 + (level - 1) * 5;
        currentDelay = Math.max(100, 200 - (level - 1) * 20);

        gameBoard = new GameBoard(rows, cols, currentFoodCount, currentTargetLength,
                currentObstaclesCount);

        gameCanvas.sceneProperty().addListener((obs, oldScene,
                                                newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
            }
        });

        setupGameLoop(currentDelay);
        draw();
        statusLabel.setText("Level " + level + ": Score 0/" + currentTargetLength);
    }

    /**
     * Sets up the game loop with the specified delay between frames.
     *
     * @param delay the delay in milliseconds between game loop iterations
     */
    private void setupGameLoop(int delay) {
        gameLoop = new Timeline(new KeyFrame(Duration.millis(delay), e -> {
            gameBoard.update();
            draw();

            if (gameBoard.isGameOver()) {
                statusLabel.setText("Game Over! Press Enter to restart.");
                gameLoop.stop();
            } else if (gameBoard.isGameWon()) {
                int score = gameBoard.getSnake().length();
                statusLabel.setText("Level " + level + ": Score " + score + "/"
                        + currentTargetLength + ". Next level in 3 sec...");
                gameLoop.stop();

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(ev -> {
                    level++;
                    initialize();
                });
                pause.play();
            } else {
                int score = gameBoard.getSnake().length();
                statusLabel.setText("Level " + level + ": Score " + score + "/"
                        + currentTargetLength);
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    /**
     * Handles key press events to control the snake or restart the game.
     *
     * @param event the key event
     */

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (gameBoard.isGameOver()) {
                level = 1;
                initialize();
            } else if (gameBoard.isGameWon()) {
                level++;
                initialize();
            }
        }
        Snake snake = gameBoard.getSnake();
        switch (event.getCode()) {
            case UP -> snake.setDirection(Direction.UP);
            case DOWN -> snake.setDirection(Direction.DOWN);
            case LEFT -> snake.setDirection(Direction.LEFT);
            case RIGHT -> snake.setDirection(Direction.RIGHT);
            default -> {}
        }
    }

    /**
     * Renders the game board including grid, obstacles, food, and snake.
     */
    private void draw() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        gc.setStroke(Color.LIGHTBLUE);
        for (int i = 0; i <= cols; i++) {
            gc.strokeLine(i * CELL_SIZE, 0, i * CELL_SIZE, rows * CELL_SIZE);
        }
        for (int i = 0; i <= rows; i++) {
            gc.strokeLine(0, i * CELL_SIZE, cols * CELL_SIZE, i * CELL_SIZE);
        }
        gc.setFill(Color.DARKGRAY.darker());
        for (Obstacle obstacle : gameBoard.getObstacles()) {
            for (Point p : obstacle.getCells()) {
                drawCell(gc, p.coordX, p.coordY);
            }
        }

        gc.setFill(Color.RED);
        for (Food food : gameBoard.getFoodList()) {
            drawCell(gc, food.getPosition().coordX, food.getPosition().coordY);
        }

        gc.setFill(Color.GREEN);
        for (Point part : gameBoard.getSnake().getBody()) {
            drawCell(gc, part.coordX, part.coordY);
        }

        gc.setFill(Color.DARKGREEN);
        Point head = gameBoard.getSnake().getHead();
        drawCell(gc, head.coordX, head.coordY);
    }

    private void drawCell(GraphicsContext gc, int x, int y) {
        gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);
    }
}
