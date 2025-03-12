package ru.nsu.lavitskaya.pizza;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The {@code SharedStates} class maintains the shared state of the pizzeria,
 * including order acceptance and tracking the number of working cooks.
 */
class SharedStates {
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

/**
 * The {@code Pizzeria} class represents a pizza restaurant. It coordinates the interactions
 * between orders, storage, cooks, and delivery staff.
 */
public class Pizzeria extends Thread {
    private final OrderQueue orderQueue;
    private final StorageQueue storageQueue;
    private final List<Cook> cooks = new ArrayList<>();
    private final List<DeliveryMan> deliveryMen = new ArrayList<>();
    private final SharedStates sharedStates = new SharedStates();

    /**
     * Constructs a new Pizzeria instance based on the provided configuration file.
     *
     * @param configFile the name of the configuration file
     * @throws IOException if there is an error reading the file
     */
    public Pizzeria(String configFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = Pizzeria.class.getClassLoader().getResourceAsStream(configFile);
        Config config = objectMapper.readValue(inputStream, Config.class);

        this.orderQueue = new OrderQueue(sharedStates);
        this.storageQueue = new StorageQueue(config.storageCapacity, sharedStates);

        for (int i = 0; i < config.deliveryManCapacity.length; i++) {
            deliveryMen.add(new DeliveryMan(i, config.deliveryManCapacity[i], storageQueue,
                    config.defaultDeliverySpeed));
        }

        for (int i = 0; i < config.cooksSpeed.length; i++) {
            cooks.add(new Cook(i, config.cooksSpeed[i], orderQueue, storageQueue));
        }

    }

    /**
     * Starts the pizzeria operation, initializing cooks and delivery threads.
     */
    @Override
    public void run() {
        try {
            for (Cook cook : cooks) {
                cook.start();
                sharedStates.incWorkingCooks();
            }
            for (DeliveryMan deliveryMan : deliveryMen) {
                deliveryMan.start();
            }

            for (Cook cook : cooks) {
                cook.join();
            }
            for (DeliveryMan deliveryMan : deliveryMen) {
                deliveryMan.join();
            }

            System.out.println("Pizzeria is closed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Stops accepting new orders and notifies all waiting cooks.
     */
    public void closePizzeria() {
        sharedStates.stopAcceptingOrders();
        synchronized (orderQueue) {
            orderQueue.notifyAll();
        }
    }

    /**
     * Places an order for a pizza.
     *
     * @param pizza the pizza order to be added
     */
    public void makeOrder(Pizza pizza) {
        orderQueue.add(pizza);
    }

    /**
     * Retrieves the slowest cook speed from the list of cooks.
     *
     * @return the slowest cooking speed value among cooks, or 0 if no cooks exist
     */
    public int getSlowestSpeed() {
        return cooks.stream().mapToInt(Cook::getSpeed).max().orElse(0);
    }

    /**
     * Retrieves the total number of cooks working in the pizzeria.
     *
     * @return the number of cooks
     */
    public int getCooksCount() {
        return cooks.size();
    }

}
