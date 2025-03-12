package ru.nsu.lavitskaya.pizza;

/**
 * The {@code Cook} class represents a worker responsible for preparing pizzas.
 * Each cook processes orders from the order queue and moves completed pizzas to storage.
 */
public class Cook extends Thread {
    private final int id;
    private final int speed;
    private final OrderQueue orderQueue;
    private final StorageQueue storageQueue;

    /**
     * Constructs a cook with a unique ID, speed, order queue and storage queue.
     *
     * @param id the unique identifier of the cook
     * @param speed the time (in seconds) required to prepare one pizza
     * @param orderQueue the queue from which the cook retrieves orders
     * @param storageQueue the queue where cooked pizzas are stored
     */
    public Cook(int id, int speed, OrderQueue orderQueue, StorageQueue storageQueue) {
        this.id = id;
        this.speed = speed;
        this.orderQueue = orderQueue;
        this.storageQueue = storageQueue;
    }

    /**
     * Executes the cooking process in a loop, continuously retrieving and preparing pizzas
     * until no more orders remain.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Pizza pizza = orderQueue.take();
                if (pizza == null) {
                    break;
                }

                System.out.println("Cook " + id + " started to cook pizza " + pizza);
                Thread.sleep(speed * 1000L);
                System.out.println("Cook " + id + " finished to cook pizza " + pizza);

                storageQueue.add(pizza);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getSpeed() {
        return speed;
    }


}
