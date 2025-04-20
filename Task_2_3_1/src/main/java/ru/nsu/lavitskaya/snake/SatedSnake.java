package ru.nsu.lavitskaya.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A “sated” enemy snake that wanders randomly in any safe direction,
 * avoiding collisions.
 * <p>
 * Unlike {@link EnemySnake}, this snake does not pursue food; it
 * simply picks a random safe direction each turn and moves one step.
 * </p>
 */
public class SatedSnake extends EnemySnake{
    private final Random random = new Random();

    /**
     * Creates a new {@code SatedSnake} at the specified starting position.
     * The snake’s {@link SnakeType} is set to {@code SATED}.
     *
     * @param start the initial head position of this snake
     */
    public SatedSnake(Point start) {
        super(start);
        this.type = SnakeType.SATED;
    }

    /**
     * Updates this snake’s position by choosing a random safe direction,
     * checking for collisions with the player or obstacles, and moving one cell.
     * <p>
     * The snake will die (be marked {@code DEAD}) if it has no safe moves
     * or if it collides with the player’s snake.
     * </p>
     *
     * @param mapRows      the number of rows on the game board
     * @param mapColumns   the number of columns on the game board
     * @param foodList     the list of {@link Food} items currently on the board
     * @param obstacles    the list of {@link Obstacle} objects on the board
     * @param playerSnake  the player’s {@link Snake}
     * @param enemySnakes  the list of all enemy snakes (including this one)
     */
    @Override
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
            type = SnakeType.DEAD;
            return;
        }

        Direction chosen = safeDirections.get(random.nextInt(safeDirections.size()));

        super.setDirection(chosen);

        if (hasCollidedWithPlayer(playerSnake)) {
            type = SnakeType.DEAD;
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
}
