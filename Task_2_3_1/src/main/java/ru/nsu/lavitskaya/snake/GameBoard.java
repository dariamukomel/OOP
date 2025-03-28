package ru.nsu.lavitskaya.snake;

import java.util.*;

public class GameBoard {
    private final int rows;
    private final int cols;
    private final int targetLength;
    private final int foodCount;
    private final int obstaclesCount;

    private final Snake snake;
    private final List<Food> foodList = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final Random random = new Random();

    private boolean gameOver = false;
    private boolean gameWon = false;

    public GameBoard(int rows, int cols, int foodCount, int targetLength, int obstaclesCount) {
        this.rows = rows;
        this.cols = cols;
        this.foodCount = foodCount;
        this.targetLength = targetLength;
        this.obstaclesCount = obstaclesCount;

        Point start = new Point(cols / 2, rows / 2);
        snake = new Snake(start);

        generateFood();
        generateObstacles();
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

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void update() {
        if (gameOver || gameWon) return;

        Point nextHead = snake.getHead().move(snake.getDirection());

        if (nextHead.x < 0 || nextHead.x >= cols || nextHead.y < 0 || nextHead.y >= rows) {
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

        if (snake.checkSelfCollision()) {
            gameOver = true;
            return;
        }

        if (snake.length() >= targetLength) {
            gameWon = true;
            return;
        }

        while (foodList.size() < foodCount) {
            generateOneFood();
        }
    }

    private void generateFood() {
        while (foodList.size() < foodCount) {
            generateOneFood();
        }
    }

    private void generateOneFood() {
        Set<Point> occupied = new HashSet<>(snake.getBody());

        while (true) {
            int x = random.nextInt(cols);
            int y = random.nextInt(rows);
            Point point = new Point(x, y);

            if (!occupied.contains(point) && foodList.stream().noneMatch(f -> f.getPosition().equals(point))) {
                foodList.add(new Food(point));
                break;
            }
        }
    }

    private void generateObstacles() {
        Set<Point> occupied = new HashSet<>(snake.getBody());
        Point snakeStart = snake.getHead();
        int safetyRadius = 3;
        for (int dx = -safetyRadius; dx <= safetyRadius; dx++) {
            for (int dy = -safetyRadius; dy <= safetyRadius; dy++) {
                int newX = snakeStart.x + dx;
                int newY = snakeStart.y + dy;
                if(newX >= 0 && newX < cols && newY >= 0 && newY < rows){
                    occupied.add(new Point(newX, newY));
                }
            }
        }
        foodList.forEach(f -> occupied.add(f.getPosition()));

        while (obstacles.size() < obstaclesCount) {
            Obstacle obs = generateObstacle(occupied);
            if (obs != null) {
                obstacles.add(obs);
                occupied.addAll(obs.getCells());
            }
        }
    }

    private Obstacle generateObstacle(Set<Point> occupied) {
        int attempts = 0;
        Point start = null;

        while (attempts < 100) {
            int x = random.nextInt(cols);
            int y = random.nextInt(rows);
            Point candidate = new Point(x, y);
            if (!occupied.contains(candidate)) {
                start = candidate;
                break;
            }
            attempts++;
        }
        if (start == null) {
            gameWon = true;
            return null;
        }

        List<Point> cells = new ArrayList<>();
        cells.add(start);

        int obstacleSize = random.nextInt(4) + 1;
        for (int i = 1; i < obstacleSize; i++) {
            List<Point> candidates = new ArrayList<>();
            for (Point cell : cells) {
                Point up = new Point(cell.x, cell.y - 1);
                Point down = new Point(cell.x, cell.y + 1);
                Point left = new Point(cell.x - 1, cell.y);
                Point right = new Point(cell.x + 1, cell.y);
                if (up.y >= 0 && !occupied.contains(up) && !cells.contains(up))
                    candidates.add(up);
                if (down.y < rows && !occupied.contains(down) && !cells.contains(down))
                    candidates.add(down);
                if (left.x >= 0 && !occupied.contains(left) && !cells.contains(left))
                    candidates.add(left);
                if (right.x < cols && !occupied.contains(right) && !cells.contains(right))
                    candidates.add(right);
            }
            if (candidates.isEmpty()) break;
            Point nextCell = candidates.get(random.nextInt(candidates.size()));
            cells.add(nextCell);
            occupied.add(nextCell);
        }
        return new Obstacle(cells);
    }
}
