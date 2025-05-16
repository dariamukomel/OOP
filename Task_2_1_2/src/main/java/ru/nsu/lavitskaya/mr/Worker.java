package ru.nsu.lavitskaya.mr;

import java.io.IOException;

public class Worker {
    private final MasterGateway gateway = new MasterGateway();

    public void start() {
        try {
            gateway.connect();

            while (true) {
                String msg = gateway.getMessage();
                if (msg == null) {
                    break;
                }

                String[] parts = msg.split(" ", 2);
                String command = parts[0].trim();

                if ("Task".equalsIgnoreCase(command)) {
                    if (parts.length < 2) {
                        continue;
                    }
                    String payload = parts[1].trim();
                    int[] numbers = parseNumbers(payload);
                    boolean hasComposite = containsComposite(numbers);

                    String answer = hasComposite ? "Answer true" : "Answer false";
                    gateway.sendAnswer(answer);

                } else if ("Terminate".equalsIgnoreCase(command)) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            gateway.close();
        }
    }

    private int[] parseNumbers(String payload) {
        String[] tokens = payload.split(",");
        int[] nums = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            try {
                nums[i] = Integer.parseInt(tokens[i].trim());
            } catch (NumberFormatException e) {
                nums[i] = 0;
            }
        }
        return nums;
    }

    private boolean containsComposite(int[] nums) {
        for (int n : nums) {
            if (n < 2 || !isPrime(n)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        if (n % 2 == 0) {
            return n == 2;
        }
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        new Worker().start();
    }
}

