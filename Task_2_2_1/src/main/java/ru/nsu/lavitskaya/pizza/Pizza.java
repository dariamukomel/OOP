package ru.nsu.lavitskaya.pizza;

/**
 * The {@code Pizza} class represents a pizza order in the pizzeria system.
 * Each pizza has a unique identifier and a status that tracks its progress
 * through the order, cooking, and delivery process.
 */
public class Pizza {
    private final int id;
    private Status status;

    public Pizza(int id) {
        this.id = id;
        this.status = Status.INIT;
    }

    public void changeStatus(Status newStatus) {
        this.status = newStatus;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
