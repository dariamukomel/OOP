package ru.nsu.lavitskaya.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents an enemy snake.
 * <p>
 * This enemy snake attempts to move in a "safe" direction toward the nearest food item.
 * It evaluates possible moves based on collisions with the map boundaries, obstacles,
 * the player's snake, and other enemy snakes. If no safe moves are available or if the
 * enemy snake would collide with the player's snake, it is marked as dead.
 * </p>
 */
public class EnemySnake extends Snake {
    private final Random random = new Random();
    private boolean alive = true;

    public EnemySnake(Point start) {
        super(start);
    }

    /**
     * Checks whether the enemy snake is alive.
     *
     * @return true if the enemy snake is still active; false if it has died.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Updates the state of the enemy snake.
     * <p>
     * This method evaluates all four directions to determine which ones are safe (i.e. will not
     * result in a collision with map boundaries, obstacles, the player's snake, or other enemy
     * snakes). If safe directions exist, it selects one that reduces the distance to the closest
     * food item. If no safe directions are available, the enemy snake is marked as dead.
     * Additionally, if moving in the chosen direction results in a collision with the player's
     * snake, the enemy is marked as dead.
     * </p>
     *
     * @param mapRows       the number of rows in the game map.
     * @param mapColumns    the number of columns in the game map.
     * @param foodList      the list of food objects available on the map.
     * @param obstacles     the list of obstacles present on the map.
     * @param playerSnake   the player's snake.
     * @param enemySnakes   the list of all enemy snakes.
     */
    public void update(int mapRows, int mapColumns, List<Food> foodList, List<Obstacle> obstacles,
                       Snake playerSnake, List<EnemySnake> enemySnakes) {
        Point head = getHead();
        List<Direction> safeDirections = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            Point next = head.move(dir);
            if (!hasCollision(next, mapRows, mapColumns, obstacles, playerSnake, enemySnakes)) {
                safeDirections.add(dir);
            }
        }
        if (safeDirections.isEmpty()) {
            alive = false;
            return;
        }

        Food targetFood = chooseTargetFood(foodList, head);
        int currentDistance = getDistance(head, targetFood.getPosition());

        List<Direction> candidateDirections = new ArrayList<>();
        for (Direction dir : safeDirections) {
            int newDistance = getDistance(head.move(dir), targetFood.getPosition());
            if (newDistance < currentDistance) {
                candidateDirections.add(dir);
            }
        }

        Direction chosen;
        if (!candidateDirections.isEmpty()) {
            chosen = candidateDirections.get(random.nextInt(candidateDirections.size()));
        } else {
            chosen = safeDirections.get(random.nextInt(safeDirections.size()));
        }

        super.setDirection(chosen);

        if (hasCollidedWithPlayer(playerSnake)) {
            alive = false;
            return;
        }

        boolean grow = false;
        Point nextHead = head.move(chosen);
        for (Food food : foodList) {
            if (food.getPosition().equals(nextHead)) {
                grow = true;
                foodList.remove(food);
                break;
            }
        }
        super.move(grow);
    }

    /**
     * Determines if moving to the specified point will result in a collision.
     * <p>
     * This method checks if the next point is outside the boundaries of the map, or if it collides
     * with any obstacles, the player's snake (using the point after two additional moves in the
     * current direction),
     * other enemy snakes, or the enemy snake's own body.
     * </p>
     *
     * @param next          the candidate point to move into.
     * @param mapRows       the number of rows in the game map.
     * @param mapColumns    the number of columns in the game map.
     * @param obstacles     the list of obstacles.
     * @param playerSnake   the player's snake.
     * @param enemySnakes   the list of all enemy snakes.
     * @return true if a collision is detected with any of the checked objects; false otherwise.
     */
    private boolean hasCollision(Point next, int mapRows, int mapColumns, List<Obstacle> obstacles,
                                 Snake playerSnake, List<EnemySnake> enemySnakes) {
        if (next.coordX < 0 || next.coordX >= mapColumns ||
                next.coordY < 0 || next.coordY >= mapRows) {
            return true;
        }

        for (Obstacle obs : obstacles) {
            for (Point cell : obs.getCells()) {
                if (cell.equals(next)) return true;
            }
        }

        Point headAfterTwoSteps = next.move(this.getDirection()).move(this.getDirection());
        for (Point p : playerSnake.getBody()) {
            if (p.equals(headAfterTwoSteps)) return true;
        }

        for (EnemySnake enemy : enemySnakes) {
            if (enemy == this) continue;
            for (Point p : enemy.getBody()) {
                if (p.equals(next)) return true;
            }
        }

        for (Point p : super.getBody()) {
            if (p.equals(next)) return true;
        }
        return false;
    }

    /**
     * Calculates the distance between two points.
     *
     * @param a the first point.
     * @param b the second point.
     * @return the Manhattan distance between the two points.
     */
    private int getDistance(Point a, Point b) {
        return Math.abs(a.coordX - b.coordX) + Math.abs(a.coordY - b.coordY);
    }

    /**
     * Selects the closest food item from the list based on the distance.
     *
     * @param foodList the list of food items.
     * @param head     the current head position of the enemy snake.
     * @return the food item closest to the enemy snake.
     */
    private Food chooseTargetFood(List<Food> foodList, Point head) {
        Food target = foodList.get(0);
        int bestDistance = getDistance(head, target.getPosition());
        for (Food food : foodList) {
            int dist = getDistance(head, food.getPosition());
            if (dist < bestDistance) {
                bestDistance = dist;
                target = food;
            }
        }
        return target;
    }

    /**
     * Checks whether the enemy snake's next head (the cell it is about to move into)
     * would collide with any part of the player's snake.
     *
     * @param playerSnake the player's snake to check for collision.
     * @return true if a collision is detected with any cell of the player's snake; false otherwise.
     */
    private boolean hasCollidedWithPlayer(Snake playerSnake) {
        Point enemyHead = this.getNextHead();
        for (Point p : playerSnake.getBody()) {
            if (enemyHead.equals(p)) {
                return true;
            }
        }
        return false;
    }
}
