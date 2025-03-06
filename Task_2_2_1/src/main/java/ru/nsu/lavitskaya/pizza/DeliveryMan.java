package ru.nsu.lavitskaya.pizza;

import java.util.List;

public class DeliveryMan extends Thread {
    private final int id;
    private final int capacity;
    private final StorageQueue storageQueue;

    public DeliveryMan(int id, int capacity, StorageQueue storageQueue) {
        this.id = id;
        this.capacity = capacity;
        this.storageQueue = storageQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                List<Pizza> pizzas = storageQueue.take(capacity);
                if(pizzas.getFirst() == null){
                    break;
                }
                System.out.println("DeliveryMan " + id + " delivers " + pizzas);
                Thread.sleep(5000);
                System.out.println("DeliveryMan " + id + " delivered " + pizzas);

                for(Pizza pizza : pizzas) {
                    pizza.changeStatus("delivered");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
