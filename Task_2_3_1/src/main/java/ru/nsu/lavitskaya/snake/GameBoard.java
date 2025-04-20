package ru.nsu.lavitskaya.snake;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Represents the game board which holds the snake, food, and obstacles.
 * Manages the game state including movement, collision detection, and win/loss conditions.
 */
public class GameBoard {
    private final int mapRows;
    private final int mapColumns;
    private final int targetLength;
    private final int foodCount;
    private final int obstaclesCount;
    private final int enemyCount;
    private final int satedCount;

    private final Snake snake;
    private final List<Food> foodList = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final List<EnemySnake> enemySnakes = new ArrayList<>();
    private final Generator generator;

    private boolean gameOver = false;
    private boolean gameWon = false;

    /**
     * Constructs a GameBoard with specified parameters.
     *
     * @param mapRows        number of rows in the game board
     * @param mapColumns     number of columns in the game board
     * @param foodCount      number of food items to generate
     * @param targetLength   the target length of the snake to win
     * @param obstaclesCount number of obstacles to generate
     */
    public GameBoard(int mapRows, int mapColumns, int foodCount, int targetLength,
                     int obstaclesCount, int enemyCount, int satedCount) {
        this.mapRows = mapRows;
        this.mapColumns = mapColumns;
        this.foodCount = foodCount;
        this.targetLength = targetLength;
        this.obstaclesCount = obstaclesCount;
        this.enemyCount = enemyCount;
        this.satedCount = satedCount;
        this.generator = new Generator(mapRows, mapColumns);

        Point start = new Point(mapColumns / 2, mapRows / 2);
        snake = new Snake(start);

        generateObstacles();
        generateFood();
        generateEnemySnakes();

    }

    public Snake getSnake() {
        return snake;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<EnemySnake> getEnemySnakes() {
        return enemySnakes;
    }

    public int getMapRows() {
        return mapRows;
    }

    public int getMapColumns() {
        return mapColumns;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * Updates the game state by moving the snake, checking collisions,
     * handling food consumption, and generating new food as needed.
     */
    public void update() {
        if (gameOver || gameWon) {
            return;
        }

        Point nextHead = snake.getNextHead();

        if (nextHead.coordX < 0 || nextHead.coordX >= mapColumns || nextHead.coordY < 0
                || nextHead.coordY >= mapRows) {
            gameOver = true;
            return;
        }

        for (Obstacle obstacle : obstacles) {
            for (Point cell : obstacle.getCells()) {
                if (cell.equals(nextHead)) {
                    gameOver = true;
                    return;
                }
            }
        }

        for (EnemySnake enemy : enemySnakes) {
            for (Point p : enemy.getBody()) {
                if (p.equals(nextHead)) {
                    gameOver = true;
                    return;
                }
            }
        }

        boolean ateFood = false;
        Iterator<Food> it = foodList.iterator();
        while (it.hasNext()) {
            Food food = it.next();
            if (food.getPosition().equals(nextHead)) {
                ateFood = true;
                it.remove();
                break;
            }
        }

        snake.move(ateFood);

        Iterator<EnemySnake> snakeIterator = enemySnakes.iterator();
        while (snakeIterator.hasNext()) {
            EnemySnake enemy = snakeIterator.next();
            enemy.update(mapRows, mapColumns, foodList, obstacles, snake, enemySnakes);
            if (enemy.getType() == SnakeType.DEAD) {
                snakeIterator.remove();
            }
        }


        if (snake.checkSelfCollision()) {
            gameOver = true;
            return;
        }

        if (snake.length() >= targetLength) {
            gameWon = true;
            return;
        }

        generateFood();

    }


    /**
     * Generates food items until it reaches foodCount.
     */
    private void generateFood() {
        while (foodList.size() < foodCount) {
            foodList.add(generator.generateOneFood(getForbiddenCells()));
        }
    }

    /**
     * Generates and adds enemy snakes to the game.
     * <p>
     * First, creates {@code enemyCount} hungry {@link EnemySnake} instances;
     * then, creates {@code satedCount} sated {@link SatedSnake} instances.
     * Each new snake is placed in a free cell determined by {@link #getForbiddenCells()}.
     * </p>
     */
    private void generateEnemySnakes() {
        while (enemySnakes.size() < enemyCount) {
            enemySnakes.add(generator.generateEnemySnake(getForbiddenCells()));
        }
        while (enemySnakes.size()-enemyCount < satedCount) {
            enemySnakes.add(generator.generateSatedSnake(getForbiddenCells()));
        }
    }


    /**
     * Builds a set of points where food should not be generated,
     * including the snake's body, obstacles, and existing food.
     *
     * @return a set of forbidden points for food placement
     */
    private Set<Point> getForbiddenCells() {
        Set<Point> forbidden = new HashSet<>();
        forbidden.addAll(snake.getBody());
        for (EnemySnake enemy : enemySnakes) {
            forbidden.addAll(enemy.getBody());
        }
        for (Obstacle obstacle : obstacles) {
            forbidden.addAll(obstacle.getCells());
        }
        for (Food food : foodList) {
            forbidden.add(food.getPosition());
        }
        return forbidden;
    }

    /**
     * Generates obstacles on the board while preventing them from spawning
     * in the five cells immediately to the right of the snake's head.
     */
    private void generateObstacles() {
        Set<Point> occupied = new HashSet<>(snake.getBody());
        Point snakeHead = snake.getHead();
        for (int i = 1; i <= 5; i++) {
            int newX = snakeHead.coordX + i;
            int newY = snakeHead.coordY;
            if (newX >= 0 && newX < mapColumns) {
                occupied.add(new Point(newX, newY));
            }
        }

        while (obstacles.size() < obstaclesCount) {
            Obstacle obs = generator.generateObstacle(occupied);
            if (obs != null) {
                obstacles.add(obs);
                occupied.addAll(obs.getCells());
            }
        }
    }
}
