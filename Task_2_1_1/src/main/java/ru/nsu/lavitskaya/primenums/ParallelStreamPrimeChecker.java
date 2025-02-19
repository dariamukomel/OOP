package ru.nsu.lavitskaya.primenums;

import java.util.Arrays;

/**
 * Implementation of {@link PrimeChecker} using Java parallel streams.
 * This class checks if an array contains any non-prime numbers by utilizing
 * the parallel processing capabilities of Java Streams.
 */
public class ParallelStreamPrimeChecker implements PrimeChecker{
    @Override
    public boolean checkNumbers(int[] numbers) {
        return Arrays.stream(numbers).parallel().anyMatch(num -> !isPrime(num));
    }
}
