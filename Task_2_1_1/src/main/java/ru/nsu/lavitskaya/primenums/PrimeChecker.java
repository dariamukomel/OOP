package ru.nsu.lavitskaya.primenums;

/**
 * Interface for checking if an array contains non-prime numbers.
 * Provides a method for implementing different prime-checking strategies
 * and a default method for checking if a single number is prime.
 */
public interface PrimeChecker {
    boolean checkNumbers(int[] numbers) throws InterruptedException;

    /**
     * Determines whether a given number is prime.
     *
     * @param num the number to check
     * @return {@code true} if the number is prime, {@code false} otherwise
     */
    default boolean isPrime(int num) {
        if (num < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}
