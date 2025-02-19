package ru.nsu.lavitskaya.primenums;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of {@link PrimeChecker} using multiple threads.
 * This class splits the array into chunks and processes them in parallel using threads.
 */
public class ParallelThreadsPrimeChecker implements PrimeChecker{
    private final int threadCount;

    public ParallelThreadsPrimeChecker(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public boolean checkNumbers(int[] numbers) throws InterruptedException {
        int length = numbers.length;
        int chunkSize = (int) Math.ceil((double) length / threadCount);
        Thread[] threads = new Thread[threadCount];
        AtomicBoolean result = new AtomicBoolean(false);

        for (int i = 0; i < threadCount; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, length);

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (!isPrime(numbers[j])) {
                        result.set(true);
                        return;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        return result.get();
    }
}
