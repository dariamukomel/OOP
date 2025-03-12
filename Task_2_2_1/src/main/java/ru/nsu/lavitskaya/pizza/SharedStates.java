package ru.nsu.lavitskaya.pizza;

/**
 * The {@code SharedStates} class maintains the shared state of the pizzeria,
 * including order acceptance and tracking the number of working cooks.
 */
public class SharedStates {
    private boolean acceptingOrders = true;
    private int workingCooks = 0;

    /**
     * Checks whether the pizzeria is still accepting new orders.
     *
     * @return {@code true} if new orders are accepted, otherwise {@code false}
     */
    public synchronized boolean isAcceptingOrders() {
        return acceptingOrders;
    }

    /**
     * Stops accepting new orders in the pizzeria.
     */
    public synchronized void stopAcceptingOrders() {
        acceptingOrders = false;
    }

    /**
     * Determines whether there are any active cooks in the pizzeria.
     *
     * @return {@code true} if there is at least one active cook, otherwise {@code false}
     */
    public synchronized boolean anyCooks() {
        return workingCooks != 0;
    }

    public synchronized void incWorkingCooks() {
        workingCooks++;
    }

    public synchronized void decWorkingCooks() {
        workingCooks--;
    }

}
