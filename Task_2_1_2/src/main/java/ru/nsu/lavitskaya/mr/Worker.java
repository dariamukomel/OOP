package ru.nsu.lavitskaya.mr;

import java.io.IOException;

/**
 * Worker process that connects to the Master via multicast discovery,
 * receives tasks, checks for composite numbers, and sends back answers.
 */
public class Worker {
    private final MasterGateway gateway = new MasterGateway();

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
        new Worker().start();
    }
}

