package ru.nsu.lavitskaya.pizza;

import java.util.LinkedList;

public class OrderQueue {
    private final LinkedList<Pizza> queue = new LinkedList<>();
    private final SharedState sharedState;

    public OrderQueue(SharedState sharedState){
        this.sharedState = sharedState;
    }

    public synchronized void add(Pizza pizza) {
        if (!sharedState.isAcceptingOrders()) return;
        queue.add(pizza);
        System.out.println(pizza.getId() + " added to order queue");
        pizza.changeStatus("ordered");
        notifyAll();
    }

    public synchronized Pizza take() throws InterruptedException {
        while (queue.isEmpty() && sharedState.isAcceptingOrders()) {
            wait();
        }
        return queue.isEmpty() ? null : queue.poll();
    }

}
