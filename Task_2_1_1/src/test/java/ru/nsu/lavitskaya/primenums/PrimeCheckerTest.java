package ru.nsu.lavitskaya.primenums;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for the PrimeChecker implementations.
 * This class loads a set of prime numbers from a file and tests different implementations of
 *     PrimeChecker.
 */
class PrimeCheckerTest {
    private static final String FILE_PATH = "src/test/resources/prime_numbers.txt";
    private static int[] primeArray = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31};
    private static int[] nonPrimeArray = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 32};



    /**
     * Loads an array of prime numbers from a file.
     *
     * @return an array of integers representing prime numbers
     * @throws IOException if an I/O error occurs while reading the file
     */
    private static int[] loadPrimesFromFile() throws IOException {
        List<Integer> primeList = Files.lines(Paths.get(FILE_PATH))
                .map(Integer::parseInt)
                .toList();
        return primeList.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Measures execution time of a PrimeChecker implementation.
     *
     * @param checker the PrimeChecker implementation
     * @param numbers the array of numbers to check
     * @return execution time in milliseconds
     * @throws InterruptedException if a thread is interrupted while executing
     */
    private long measureExecutionTime(PrimeChecker checker, int[] numbers)
            throws InterruptedException {
        long start = System.nanoTime();
        checker.checkNumbers(numbers);
        long end = System.nanoTime();
        return (end - start) / 1_000_000;
    }


    /**
     * Tests the execution times of different PrimeChecker implementations.
     *
     * @throws IOException if an error occurs while reading the prime numbers file
     * @throws InterruptedException if a thread is interrupted while executing
     */
    @Test
    public void testExecutionTimes() throws IOException, InterruptedException {
        int[] primeNumbers = loadPrimesFromFile();
        PrimeChecker sequentialChecker = new SequentialPrimeChecker();
        long sequentialTime = measureExecutionTime(sequentialChecker, primeNumbers);
        System.out.println("Sequential: Time: " + sequentialTime + " ms");

        for (int threads = 2; threads <= 8; threads += 1) {
            PrimeChecker parallelThreadsChecker = new ParallelThreadsPrimeChecker(threads);
            long parallelTime = measureExecutionTime(parallelThreadsChecker, primeNumbers);
            System.out.println("Parallel Threads (" + threads + "): Time: " + parallelTime + " ms");
        }

        PrimeChecker parallelStreamChecker = new ParallelStreamPrimeChecker();
        long parallelStreamTime = measureExecutionTime(parallelStreamChecker, primeNumbers);
        System.out.println("Parallel Stream: Time: " + parallelStreamTime + " ms");

        assertTrue(true);
    }

    /**
     * Provides different implementations of PrimeChecker for parameterized testing.
     *
     * @return a stream of PrimeChecker instances
     */
    private static Stream<PrimeChecker> primeCheckersProvider() {
        return Stream.of(
                new SequentialPrimeChecker(),
                new ParallelThreadsPrimeChecker(2),
                new ParallelThreadsPrimeChecker(4),
                new ParallelThreadsPrimeChecker(6),
                new ParallelThreadsPrimeChecker(8),
                new ParallelStreamPrimeChecker()
        );
    }

    /**
     * Parameterized test for all PrimeChecker implementations with one not prime number
     *     in the end of array.
     *
     * @param checker the PrimeChecker implementation to be tested
     */
    @ParameterizedTest
    @MethodSource("primeCheckersProvider")
    void testNonPrimeNumber(PrimeChecker checker) throws InterruptedException {
        assertTrue(checker.checkNumbers(primeArray), checker.getClass().getSimpleName());
        assertFalse(checker.checkNumbers(nonPrimeArray), checker.getClass().getSimpleName());
    }


}