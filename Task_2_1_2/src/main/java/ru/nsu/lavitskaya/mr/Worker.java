package ru.nsu.lavitskaya.mr;

import java.io.IOException;
import java.util.Arrays;

/**
 * Worker process that connects to the Master via multicast discovery,
 * receives tasks, checks for composite numbers, and sends back answers.
 * <p>
 * Uses {@link MasterGateway} to discover the master and communicate task/request messages.
 * </p>
 */
public class Worker {
    private final MasterGateway gateway;

    /**
     * Constructs a Worker instance.
     *
     * @param useLoopback if true, use the loopback interface for network operations
     */
    public Worker(boolean useLoopback) {
        this.gateway = new MasterGateway(useLoopback);
    }

    /**
     * Starts the worker's main loop: connects to the master,
     * processes incoming TASK and TERMINATE messages, and sends responses.
     */
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

    /**
     * Application entry point for the worker.
     * <p>
     * Accepts an optional 'test' flag to force loopback usage.
     * </p>
     *
     * @param args command-line arguments; include 'test' to use loopback interface
     */
    public static void main(String[] args) {
        boolean useLoopback = Arrays.asList(args).contains("test");
        new Worker(useLoopback).start();
    }
}
