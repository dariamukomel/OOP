package ru.nsu.lavitskaya.pizza;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;


class SharedState {
    private boolean acceptingOrders = true;

    public synchronized boolean isAcceptingOrders() {
        return acceptingOrders;
    }

    public synchronized void stopAcceptingOrders() {
        acceptingOrders = false;
        notifyAll();
    }
}

public class Pizzeria extends Thread {
    OrderQueue orderQueue;
    StorageQueue storageQueue;
    private final List<Cook> cooks = new ArrayList<>();
    private final List<DeliveryMan> deliveryMen = new ArrayList<>();
    private final SharedState sharedState = new SharedState();


    public Pizzeria(String configFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = Pizzeria.class.getClassLoader().getResourceAsStream(configFile);
        Config config = objectMapper.readValue(inputStream, Config.class);

        this.orderQueue = new OrderQueue(sharedState);
        this.storageQueue = new StorageQueue(config.storageCapacity, sharedState);

        for (int i = 0; i < config.deliveryManCapacity.length; i++) {
            deliveryMen.add(new DeliveryMan(i, config.deliveryManCapacity[i], storageQueue));
        }

        for (int i = 0; i < config.cooksSpeed.length; i++) {
            cooks.add(new Cook(i, config.cooksSpeed[i], orderQueue, storageQueue));
        }

    }

    @Override
    public void run() {
        try {
            for (Cook cook : cooks) cook.start();
            for (DeliveryMan deliveryMan : deliveryMen) deliveryMan.start();

            for (Cook cook : cooks) cook.join();
            for (DeliveryMan deliveryMan : deliveryMen) deliveryMan.join();

            System.out.println("Pizzeria is closed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void closePizzeria() {
        sharedState.stopAcceptingOrders();
    }

}
