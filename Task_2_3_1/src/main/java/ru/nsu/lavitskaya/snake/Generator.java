package ru.nsu.lavitskaya.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Responsible for generating food items and obstacles on the game board.
 */
public class Generator {
    private final int mapRows;
    private final int mapColumns;
    private final Random random = new Random();

    public Generator(int mapRows, int mapColumns) {
        this.mapRows = mapRows;
        this.mapColumns = mapColumns;
    }

    /**
     * Generates a new food item at a random location that is not in the forbidden set.
     *
     * @param forbidden a set of points where food should not be generated
     * @return a new Food object at a valid position
     */
    public Food generateOneFood(Set<Point> forbidden) {
        while (true) {
            int x = random.nextInt(mapColumns);
            int y = random.nextInt(mapRows);
            Point point = new Point(x, y);
            if (!forbidden.contains(point)) {
                return new Food(point);
            }
        }
    }

    /**
     * Generates a new obstacle starting at a random location not in the occupied set.
     * The obstacle consists of a random number of connected cells.
     *
     * @param occupied a set of points already occupied by other game elements
     * @return a new Obstacle object, or null if a valid starting point cannot be found
     */
    public Obstacle generateObstacle(Set<Point> occupied) {
        int attempts = 0;
        Point start = null;
        while (attempts < 100) {
            int x = random.nextInt(mapColumns);
            int y = random.nextInt(mapRows);
            Point candidate = new Point(x, y);
            if (!occupied.contains(candidate)) {
                start = candidate;
                break;
            }
            attempts++;
        }
        if (start == null) {
            return null;
        }

        List<Point> cells = new ArrayList<>();
        cells.add(start);
        int obstacleSize = random.nextInt(5) + 1;
        for (int i = 1; i < obstacleSize; i++) {
            List<Point> candidates = new ArrayList<>();
            for (Point cell : cells) {
                Point up = cell.move(Direction.UP);
                Point down = cell.move(Direction.DOWN);
                Point left = cell.move(Direction.LEFT);
                Point right = cell.move(Direction.RIGHT);

                if (up.y >= 0 && !occupied.contains(up) && !cells.contains(up)) {
                    candidates.add(up);
                }
                if (down.y < mapRows && !occupied.contains(down) && !cells.contains(down)) {
                    candidates.add(down);
                }
                if (left.x >= 0 && !occupied.contains(left) && !cells.contains(left)) {
                    candidates.add(left);
                }
                if (right.x < mapColumns && !occupied.contains(right) && !cells.contains(right)) {
                    candidates.add(right);
                }

            }
            if (candidates.isEmpty()) {
                break;
            }
            Point nextCell = candidates.get(random.nextInt(candidates.size()));
            cells.add(nextCell);
            occupied.add(nextCell);
        }
        return new Obstacle(cells);
    }

}
