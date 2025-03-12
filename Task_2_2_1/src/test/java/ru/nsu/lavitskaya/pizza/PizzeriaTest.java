package ru.nsu.lavitskaya.pizza;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * The {@code PizzeriaTest} class contains unit tests to verify the functionality
 * of the pizzeria system, including order processing, cooking, and delivery.
 */
class PizzeriaTest {

    /**
     * Tests the pizzeria's ability to process orders, deliver pizzas, and handle closing
     *     operations.
     *
     * @throws IOException if there is an error reading the configuration file
     * @throws InterruptedException if the thread execution is interrupted
     */
    @Test
    void testPizzeria() throws IOException, InterruptedException {
        Pizzeria pizzeria = new Pizzeria("config1.json");
        List<Pizza> pizzas = new ArrayList<>();
        pizzeria.start();
        for (int i = 1; i <= 50; i++) {
            Pizza pizza = new Pizza(i);
            pizzas.add(pizza);
            if (i == 41) {
                pizzeria.closePizzeria();
            }
            pizzeria.makeOrder(pizza);
            Thread.sleep(100);
        }
        pizzeria.join();
        int deliveredCount = 0;
        for (Pizza pizza : pizzas) {
            if (pizza.getStatus() == Status.DELIVERED) {
                deliveredCount++;
            }
        }
        int initialCount = 0;
        for (Pizza pizza : pizzas) {
            if (pizza.getStatus() == Status.INIT) {
                initialCount++;
            }
        }
        assertEquals(40, deliveredCount);
        assertEquals(10, initialCount);
    }

    /**
     * Tests whether the pizzeria correctly handles an emptying the order queue
     * and resumes processing after a delay.
     */
    @Test
    void testPizzeriaPauseInOrders() throws IOException, InterruptedException {
        Pizzeria pizzeria = new Pizzeria("config1.json");
        List<Pizza> pizzas = new ArrayList<>();
        pizzeria.start();
        for (int i = 1; i <= 10; i++) {
            Pizza pizza = new Pizza(i);
            pizzas.add(pizza);
            pizzeria.makeOrder(pizza);
        }
        long slowestSpeed = pizzeria.getSlowestSpeed() * 1000L;
        int cooksCount = pizzeria.getCooksCount();
        Thread.sleep(10 * slowestSpeed / cooksCount);
        int readyPizzas = 0;
        for (Pizza pizza : pizzas) {
            if (pizza.getStatus() == Status.STORED || pizza.getStatus() == Status.DELIVERED
                    || pizza.getStatus() == Status.DELIVERING) {
                readyPizzas++;
            }
        }
        assertEquals(10, readyPizzas); //order queue is empty

        for (int i = 1; i <= 10; i++) {
            Pizza pizza = new Pizza(i);
            pizzas.add(pizza);
            pizzeria.makeOrder(pizza);
        }
        pizzeria.closePizzeria();
        pizzeria.join();
        int deliveredPizzas = 0;
        for (Pizza pizza : pizzas) {
            if (pizza.getStatus() == Status.DELIVERED) {
                deliveredPizzas++;
            }
        }
        assertEquals(20, deliveredPizzas);
    }

    /**
     * Tests whether the pizzeria correctly handles storage overflow.
     *
     * @throws IOException if there is an error reading the configuration file
     * @throws InterruptedException if the thread execution is interrupted
     */
    @Test
    void testPizzeriaStorageOverflow() throws IOException, InterruptedException {
        Pizzeria pizzeria = new Pizzeria("config2.json");
        List<Pizza> pizzas = new ArrayList<>();
        pizzeria.start();
        for (int i = 1; i <= 6; i++) {
            Pizza pizza = new Pizza(i);
            pizzas.add(pizza);
            pizzeria.makeOrder(pizza);
        }
        Thread.sleep(5 * 1000L);
        int storedPizzas = 0;
        for (Pizza pizza : pizzas) {
            if (pizza.getStatus() == Status.STORED) {
                storedPizzas++;
            }
        }
        assertEquals(5, storedPizzas);

        pizzeria.closePizzeria();
        pizzeria.join();
        int deliveredPizzas = 0;
        for (Pizza pizza : pizzas) {
            if (pizza.getStatus() == Status.DELIVERED) {
                deliveredPizzas++;
            }
        }
        assertEquals(6, deliveredPizzas);
    }
}