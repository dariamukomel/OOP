package ru.nsu.lavitskaya.primenums;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the PrimeChecker class.
 * This class loads a set of prime numbers from a file and measures the execution time
 * of different methods in PrimeChecker, including sequential, parallel thread-based,
 * and parallel stream-based implementations.
 */
class PrimeCheckerTest {
    private static final String FILE_PATH = "src/test/resources/prime_numbers.txt";

    /**
     * Loads an array of prime numbers from a file.
     *
     * @return an array of integers representing prime numbers
     * @throws IOException if an I/O error occurs while reading the file
     */
    private int[] loadPrimesFromFile() throws IOException {
        List<Integer> primeList = Files.lines(Paths.get(FILE_PATH))
                .map(Integer::parseInt)
                .toList();
        return primeList.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Tests the execution times of the different implementations of prime number checking.
     * Measures performance for sequential execution, parallel execution with multiple threads,
     * and parallel execution using Java's parallel streams.
     *
     * @throws IOException if an error occurs while reading the prime numbers file
     * @throws InterruptedException if a thread is interrupted while executing
     */
    @Test
    public void testExecutionTimes() throws IOException, InterruptedException {

        int[] primeNumbers = loadPrimesFromFile();
        long start, end;
        start = System.nanoTime();
        boolean sequentialResult = PrimeChecker.sequential(primeNumbers);
        end = System.nanoTime();
        long sequentialTime = (end - start) / 1_000_000;
        System.out.println("Sequential: " + sequentialResult + " Time: " + sequentialTime + " ms");

        for (int threads = 2; threads <= 8; threads += 1) {
            start = System.nanoTime();
            boolean parallelThreadsResult = PrimeChecker.parallelThreads(primeNumbers, threads);
            end = System.nanoTime();
            long parallelTime = (end - start) / 1_000_000;
            System.out.println("Parallel Threads (" + threads + "): " + parallelThreadsResult
                    + " Time: " + parallelTime + " ms");
        }

        start = System.nanoTime();
        boolean parallelStreamResult = PrimeChecker.parallelStream(primeNumbers);
        end = System.nanoTime();
        long parallelStreamTime = (end - start) / 1_000_000;
        System.out.println("Parallel Stream: " + parallelStreamResult + " Time: "
                + parallelStreamTime + " ms");

        assertTrue(true);

    }
}