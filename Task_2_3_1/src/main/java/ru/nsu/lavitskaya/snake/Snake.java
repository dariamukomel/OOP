package ru.nsu.lavitskaya.snake;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Represents the snake in the game.
 * Maintains the snake's body, direction, and provides methods for movement and collision detection.
 */
public class Snake {
    private final Deque<Point> body = new LinkedList<>();
    private Direction direction = Direction.RIGHT;

    public Snake(Point start) {
        body.add(start);
    }

    public Point getHead() {
        return body.peekFirst();
    }

    public Point getNextHead() {
        return getHead().move(direction);
    }

    public Deque<Point> getBody() {
        return body;
    }

    /**
     * Sets the snake's direction. If the new direction is directly opposite to the current
     * direction, the change is ignored.
     *
     * @param newDirection the new direction to set
     */
    public void setDirection(Direction newDirection) {
        if (length() == 1 || !newDirection.isOpposite(direction)) {
            this.direction = newDirection;
        }
    }

    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Moves the snake in the current direction.
     * If the 'grow' flag is true, the snake grows by not removing the tail.
     *
     * @param grow if true, the snake grows after moving
     * @return the new head position after the move
     */
    public Point move(boolean grow) {
        Point newHead = getHead().move(direction);
        body.addFirst(newHead);
        if (!grow) {
            body.removeLast();
        }
        return newHead;
    }

    /**
     * Checks if the snake has collided with itself.
     *
     * @return true if the snake's head collides with any other part of its body, false otherwise
     */
    public boolean checkSelfCollision() {
        Point head = getHead();
        return body.stream().skip(1).anyMatch(p -> p.equals(head));
    }

    public int length() {
        return body.size();
    }

}
