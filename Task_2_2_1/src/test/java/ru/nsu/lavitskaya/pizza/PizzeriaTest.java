package ru.nsu.lavitskaya.pizza;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PizzeriaTest {
    @Test
    void testPizzeria() throws IOException, InterruptedException {
        Pizzeria pizzeria = new Pizzeria("config.json");
        List<Pizza> pizzas= new ArrayList<>();
        pizzeria.start();
        for (int i = 1; i <= 50; i++) {
            if (i == 11) {
                pizzeria.closePizzeria();
            }
            Pizza pizza = new Pizza(i);
            pizzas.add(pizza);
            pizzeria.orderQueue.add(pizza);
            Thread.sleep(100);
        }
        pizzeria.join();
        int deliveredCount = 0;
        int initialCount =0;
        for (Pizza pizza : pizzas) {
            if(pizza.getStatus() == "delivered") {
                deliveredCount++;
            }
            if(pizza.getStatus() == "init") {
                initialCount++;
            }
        }
        assertTrue(deliveredCount == 10);
        assertTrue(initialCount == 40);
    }

}