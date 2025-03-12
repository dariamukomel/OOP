package ru.nsu.lavitskaya.pizza;

/**
 * The {@code Status} enum represents the different stages a pizza order
 * can go through in the pizzeria system.
 */
public enum Status {
    INIT,
    ORDERED,
    STORED,
    DELIVERING,
    DELIVERED
}
