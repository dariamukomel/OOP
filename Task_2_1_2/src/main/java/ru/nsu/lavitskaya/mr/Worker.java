package ru.nsu.lavitskaya.mr;

import java.io.IOException;
import java.util.Arrays;

/**
 * Worker process that connects to the Master via multicast discovery,
 * receives tasks, checks for composite numbers, and sends back answers.
 */
public class Worker {
    private final MasterGateway gateway;

    public Worker(boolean useLoopback) {
        this.gateway = new MasterGateway(useLoopback);
    }

    public void start() {
        try {
            gateway.connect();

            while (true) {
                Message msg = gateway.getMessage();
                switch (msg.getType()) {
                    case TASK:
                        int[] numbers = msg.getNumbers();
                        boolean hasComposite = PrimeChecker.containsComposite(numbers);
                        String answer = hasComposite ? "Answer true" : "Answer false";
                        gateway.sendAnswer(answer);
                        break;
                    case TERMINATE:
                        return;
                    default:
                        throw new IllegalArgumentException("Unknown command");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            gateway.close();
        }
    }

    public static void main(String[] args) {
        boolean useLoopback = Arrays.asList(args).contains("test");
        new Worker(useLoopback).start();
    }
}
