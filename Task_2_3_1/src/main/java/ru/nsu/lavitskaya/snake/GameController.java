package ru.nsu.lavitskaya.snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameController {
    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label statusLabel;

    int ROWS, COLS;
    private static final int CELL_SIZE = 25;
    private static final int FOOD_COUNT = 3;
    private static final int TARGET_LENGTH = 10;
    private static final int OBSTACLES_COUNT = 3;

    private GameBoard gameBoard;
    private Timeline gameLoop;

    @FXML
    public void initialize() {
        ROWS = (int)(gameCanvas.getHeight()/CELL_SIZE);
        COLS = (int)(gameCanvas.getWidth()/CELL_SIZE);

        gameBoard = new GameBoard(ROWS, COLS, FOOD_COUNT, TARGET_LENGTH, OBSTACLES_COUNT);

        gameCanvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
            }
        });

        setupGameLoop();

        draw();
    }

    private void setupGameLoop() {
        gameLoop = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            gameBoard.update();
            draw();

            if (gameBoard.isGameOver()) {
                statusLabel.setText("Game Over! Press Enter to restart.");
                gameLoop.stop();
            } else if (gameBoard.isGameWon()) {
                statusLabel.setText("You Win! Press Enter to restart.");
                gameLoop.stop();
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (gameLoop != null) {
                gameLoop.stop();
            }
            initialize();
        }

        Snake snake = gameBoard.getSnake();
        switch (event.getCode()) {
            case UP -> snake.setDirection(Direction.UP);
            case DOWN -> snake.setDirection(Direction.DOWN);
            case LEFT -> snake.setDirection(Direction.LEFT);
            case RIGHT -> snake.setDirection(Direction.RIGHT);
        }
    }


    private void draw() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        // Очистка
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Сетка (по желанию)
        gc.setStroke(Color.LIGHTBLUE);
        for (int i = 0; i <= COLS; i++)
            gc.strokeLine(i * CELL_SIZE, 0, i * CELL_SIZE, ROWS * CELL_SIZE);
        for (int i = 0; i <= ROWS; i++)
            gc.strokeLine(0, i * CELL_SIZE, COLS * CELL_SIZE, i * CELL_SIZE);

        gc.setFill(Color.DARKGRAY.darker());
        for (Obstacle obstacle : gameBoard.getObstacles()) {
            for (Point p : obstacle.getCells()) {
                drawCell(gc, p.x, p.y);
            }
        }

        // Отрисовка еды
        gc.setFill(Color.RED);
        for (Food food : gameBoard.getFoodList()) {
            drawCell(gc, food.getPosition().x, food.getPosition().y);
        }

        // Отрисовка змейки
        gc.setFill(Color.GREEN);
        for (Point part : gameBoard.getSnake().getBody()) {
            drawCell(gc, part.x, part.y);
        }

        // Голова змейки — темнее
        gc.setFill(Color.DARKGREEN);
        Point head = gameBoard.getSnake().getHead();
        drawCell(gc, head.x, head.y);
    }

    private void drawCell(GraphicsContext gc, int x, int y) {
        gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);
    }
}
