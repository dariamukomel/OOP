package ru.nsu.lavitskaya.pizza;

public class Cook extends Thread{
    private final int id;
    private final int speed;
    private final OrderQueue orderQueue;
    private final StorageQueue storageQueue;

    public Cook(int id, int speed, OrderQueue orderQueue, StorageQueue storageQueue) {
        this.id = id;
        this.speed = speed;
        this.orderQueue = orderQueue;
        this.storageQueue = storageQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pizza pizza = orderQueue.take();
                if (pizza == null) break;

                System.out.println("Cook " + id + " started to cook pizza " + pizza.getId());
                Thread.sleep(speed * 1000L);
                System.out.println("Cook " + id + " finished to cook pizza " + pizza.getId());
                pizza.changeStatus("cooked");

                storageQueue.add(pizza);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
