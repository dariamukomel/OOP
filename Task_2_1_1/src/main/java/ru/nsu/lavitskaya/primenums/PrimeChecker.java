package ru.nsu.lavitskaya.primenums;

import java.util.Arrays;

/**
 * PrimeChecker provides methods for checking if an array contains non-prime numbers.
 * It includes sequential and parallel implementations using threads and parallel streams.
 */
public class PrimeChecker {

    /**
     * Checks if there is at least one non-prime number in the array using a sequential approach.
     *
     * @param numbers the array of integers to check
     * @return true if there is a non-prime number, false otherwise
     */
    public static boolean sequential(int[] numbers) {
        for (int num : numbers) {
            if (!isPrime(num)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is at least one non-prime number in the array using multiple threads.
     *
     * @param numbers the array of integers to check
     * @param threadCount the number of threads to use
     * @return true if there is a non-prime number, false otherwise
     * @throws InterruptedException if the thread execution is interrupted
     */
    public static boolean parallelThreads(int[] numbers, int threadCount)
            throws InterruptedException {
        int length = numbers.length;
        int chunkSize = (int) Math.ceil((double) length / threadCount);
        Thread[] threads = new Thread[threadCount];
        final boolean[] result = {false};

        for (int i = 0; i < threadCount; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, length);

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (!isPrime(numbers[j])) {
                        synchronized (result) {
                            result[0] = true;
                        }
                        return;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        return result[0];
    }

    /**
     * Checks if there is at least one non-prime number in the array using Java parallel streams.
     *
     * @param numbers the array of integers to check
     * @return true if there is a non-prime number, false otherwise
     */
    public static boolean parallelStream(int[] numbers) {
        return Arrays.stream(numbers).parallel().anyMatch(num -> !isPrime(num));
    }

    private static boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }
}
