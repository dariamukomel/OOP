package ru.nsu.lavitskaya.pizza;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code StorageQueue} class manages the storage of prepared pizzas before delivery.
 * It ensures that the storage does not exceed capacity and allows delivery personnel
 * to retrieve pizzas for delivery.
 */
public class StorageQueue {
    private final int capacity;
    private final LinkedList<Pizza> queue = new LinkedList<>();
    private final SharedStates sharedStates;

    public StorageQueue(int capacity, SharedStates sharedStates) {
        this.capacity = capacity;
        this.sharedStates = sharedStates;
    }

    /**
     * Adds a pizza to the storage. If storage is full, the thread waits until space is available.
     *
     * @param pizza the pizza to be stored
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized void add(Pizza pizza) throws InterruptedException {
        while (queue.size() >= capacity) {
            wait();
        }
        queue.add(pizza);
        System.out.println(pizza + " pizza stored to storage");
        pizza.changeStatus(Status.STORED);
        notifyAll();
    }

    /**
     * Retrieves a batch of pizzas from storage for delivery. If storage is empty and cooks
     * are active, the thread waits until pizzas are available.
     *
     * @param maxCount the maximum number of pizzas to retrieve in a batch
     * @return a list of pizzas ready for delivery; may contain {@code null} if no pizzas are
     *     available
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized List<Pizza> take(int maxCount) throws InterruptedException {
        while (queue.isEmpty() && sharedStates.anyCooks()) {
            wait();
        }
        List<Pizza> batch = new ArrayList<>();
        if (queue.isEmpty()) {
            batch.add(null);
        }
        while (!queue.isEmpty() && batch.size() < maxCount) {
            batch.add(queue.poll());
        }
        notifyAll();
        return batch;
    }
}
