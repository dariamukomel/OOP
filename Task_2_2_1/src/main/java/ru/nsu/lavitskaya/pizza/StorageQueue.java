package ru.nsu.lavitskaya.pizza;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StorageQueue {
    private final int capacity;
    private final LinkedList<Pizza> queue = new LinkedList<>();
    private final SharedState sharedState;

    public StorageQueue(int capacity, SharedState sharedState) {
        this.capacity = capacity;
        this.sharedState = sharedState;
    }

    public synchronized void add(Pizza pizza) throws InterruptedException {
        while (queue.size() >= capacity) {
            wait();
        }
        queue.add(pizza);
        System.out.println(pizza.getId() + " pizza stored to storage");
        pizza.changeStatus("stored");
        notifyAll();
    }

    public synchronized List<Pizza> take(int maxCount) throws InterruptedException {
        while (queue.isEmpty() && sharedState.isAcceptingOrders()) {
            wait();
        }
        List<Pizza> batch = new ArrayList<>();
        if(queue.isEmpty()) {
            batch.add(null);
        }
        while (!queue.isEmpty() && batch.size() < maxCount) {
            batch.add(queue.poll());
        }
        notifyAll();
        return batch;
    }
}
