package ru.nsu.lavitskaya.snake;


import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
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
                if (up.coordY >= 0 && !occupied.contains(up) && !cells.contains(up)) {
                    candidates.add(up);
                }
                Point down = cell.move(Direction.DOWN);
                if (down.coordY < mapRows && !occupied.contains(down) && !cells.contains(down)) {
                    candidates.add(down);
                }
                Point left = cell.move(Direction.LEFT);
                if (left.coordX >= 0 && !occupied.contains(left) && !cells.contains(left)) {
                    candidates.add(left);
                }
                Point right = cell.move(Direction.RIGHT);
                if (right.coordX < mapColumns && !occupied.contains(right)
                        && !cells.contains(right)) {
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

    /**
     * Generates a new enemy snake at a random location that is not present in the forbidden set.
     *
     * @param forbidden a set of points where an enemy snake cannot be generated
     * @return a new {@link EnemySnake} object created at a valid, unoccupied position
     */
    public EnemySnake generateEnemySnake(Set<Point> forbidden) {
        while (true) {
            int x = random.nextInt(mapColumns);
            int y = random.nextInt(mapRows);
            Point point = new Point(x, y);
            if (!forbidden.contains(point)) {
                return new EnemySnake(point);
            }
        }
    }

    /**
     * Generates a new {@link SatedSnake} at a random free position with a randomly determined
     *     length.
     * <p>
     * The method attempts to place the snake’s head in a cell not in the given {@code forbidden}
     * set. It then grows the body in a random valid direction up to a desired length of 3–5
     * segments; if fewer than 3 segments can be placed, it retries with a new head position.
     * Finally, it orients the snake’s direction away from its second segment and assigns the
     * generated segment list to the snake’s body.
     * </p>
     *
     * @param forbidden a set of {@link Point}s where the snake’s head or body must not appear
     * @return a {@code SatedSnake} instance with 3–5 segments, positioned and oriented safely
     */
    public SatedSnake generateSatedSnake(Set<Point> forbidden) {
        while (true) {
            int x = random.nextInt(mapColumns);
            int y = random.nextInt(mapRows);
            Point head = new Point(x, y);
            if (forbidden.contains(head)) continue;

            int desiredLen = random.nextInt(4) + 2;

            List<Point> body = new ArrayList<>();
            body.add(head);
            Set<Point> used = new HashSet<>(forbidden);
            used.add(head);
            Point current = head;

            for (int i = 1; i < desiredLen; i++) {
                List<Point> neigh = new ArrayList<>();
                for (Direction d : Direction.values()) {
                    Point np = current.move(d);
                    if (np.coordX >= 0 && np.coordX < mapColumns
                            && np.coordY >= 0 && np.coordY < mapRows
                            && !used.contains(np)) {
                        neigh.add(np);
                    }
                }
                if (neigh.isEmpty()) {
                    break;
                }
                Point next = neigh.get(random.nextInt(neigh.size()));
                body.add(next);
                used.add(next);
                current = next;
            }

            if (body.size() < 3) {
                continue;
            }

            SatedSnake s = new SatedSnake(head);

            Point second = body.get(1);
            int dx = head.coordX - second.coordX;
            int dy = head.coordY - second.coordY;
            Direction initDir;
            if (dx == 1) {
                initDir = Direction.RIGHT;
            }
            else if (dx == -1) {
                initDir = Direction.LEFT;
            }
            else if (dy == 1)  {
                initDir = Direction.DOWN;
            }
            else     {
                initDir = Direction.UP;
            }

            s.setDirection(initDir);

            Deque<Point> dq = s.getBody();
            dq.clear();
            for (Point p : body) {
                dq.addLast(p);
            }

            return s;


        }
    }

}
