package ru.nsu.lavitskaya.pizza;

import java.util.List;

/**
 * The {@code DeliveryMan} class represents a worker responsible for delivering pizzas.
 * Each delivery worker retrieves pizzas from the storage and delivers them to customers.
 */
public class DeliveryMan extends Thread {
    private final int id;
    private final int capacity;
    private final StorageQueue storageQueue;
    private final int deliverySpeed;

    public DeliveryMan(int id, int capacity, StorageQueue storageQueue, int deliverySpeed) {
        this.id = id;
        this.capacity = capacity;
        this.storageQueue = storageQueue;
        this.deliverySpeed = deliverySpeed;
    }

    /**
     * Executes the delivery process in a loop, continuously retrieving and delivering pizzas
     * until no more pizzas remain.
     */
    @Override
    public void run() {
        try {
            while (true) {
                List<Pizza> pizzas = storageQueue.take(capacity);
                if (pizzas.getFirst() == null) {
                    break;
                }
                for (Pizza pizza : pizzas) {
                    pizza.changeStatus(Status.DELIVERING);
                }

                System.out.println("DeliveryMan " + id + " delivering " + pizzas);
                Thread.sleep(deliverySpeed * 1000L);
                System.out.println("DeliveryMan " + id + " delivered " + pizzas);

                for (Pizza pizza : pizzas) {
                    pizza.changeStatus(Status.DELIVERED);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
