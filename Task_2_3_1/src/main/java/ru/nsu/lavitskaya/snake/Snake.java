package ru.nsu.lavitskaya.snake;

import java.util.Deque;
import java.util.LinkedList;

public class Snake {
    private final Deque<Point> body = new LinkedList<>();
    private Direction direction = Direction.RIGHT;

    public Snake(Point start) {
        body.add(start);
    }

    public Point getHead() {
        return body.peekFirst();
    }

    public Deque<Point> getBody() {
        return body;
    }

    public void setDirection(Direction newDirection) {
        if (length() ==1 || !newDirection.isOpposite(direction)) {
            this.direction = newDirection;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public Point move(boolean grow) {
        Point newHead = getHead().move(direction);
        body.addFirst(newHead);
        if (!grow) {
            body.removeLast();
        }
        return newHead;
    }

    public boolean checkSelfCollision() {
        Point head = getHead();
        return body.stream().skip(1).anyMatch(p -> p.equals(head));
    }

    public int length() {
        return body.size();
    }

}
