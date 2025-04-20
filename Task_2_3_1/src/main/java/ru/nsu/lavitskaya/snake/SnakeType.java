package ru.nsu.lavitskaya.snake;

/**
 * Represents the current state or role of an enemy snake.
 * <ul>
 *   <li>{@link #ENEMY}: a hungry snake that pursues the nearest food.</li>
 *   <li>{@link #SATED}: a sated snake that wanders randomly, avoiding collisions.</li>
 *   <li>{@link #DEAD}: a snake that has been eliminated (no longer moves).</li>
 * </ul>
 */
public enum SnakeType {
    ENEMY,
    SATED,
    DEAD;
}
