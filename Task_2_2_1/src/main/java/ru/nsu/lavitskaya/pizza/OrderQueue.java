package ru.nsu.lavitskaya.pizza;

import java.util.LinkedList;

/**
 * The {@code OrderQueue} class represents a queue for pizza orders.
 * It ensures synchronization between cooks retrieving orders and new orders being placed.
 */
public class OrderQueue {
    private final LinkedList<Pizza> queue = new LinkedList<>();
    private final SharedStates sharedStates;

    public OrderQueue(SharedStates sharedStates) {
        this.sharedStates = sharedStates;
    }

    /**
     * Adds a new pizza order to the queue if orders are still being accepted.
     *
     * @param pizza the pizza order to be added
     */
    public synchronized void add(Pizza pizza) {
        if (!sharedStates.isAcceptingOrders()) {
            return;
        }
        queue.add(pizza);
        System.out.println(pizza + " added to order queue");
        pizza.changeStatus(Status.ORDERED);
        notifyAll();
    }

    /**
     * Retrieves the next pizza order from the queue.
     * If no orders are available, it waits until one is added or order acceptance stops.
     *
     * @return the next pizza order, or {@code null} if no orders are left and no more can be added
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized Pizza take() throws InterruptedException {
        while (queue.isEmpty() && sharedStates.isAcceptingOrders()) {
            wait();
        }
        if (queue.isEmpty()) {
            sharedStates.decWorkingCooks();
            return null;
        }
        return queue.poll();
    }

}
