package ru.nsu.lavitskaya.mr;

/**
 * Class for prime number checks.
 * <p>
 * Provides methods to determine if an array contains any composite numbers
 * and to test the primality of individual integers.
 * </p>
 */
public class PrimeChecker {

    /**
     * Checks whether the given array of integers contains any composite numbers.
     * <p>
     * A number is considered composite if it is less than 2 or not prime.
     * </p>
     *
     * @param nums array of integers to check for compositeness
     * @return {@code true} if any element is composite or less than 2;
     *         {@code false} if all elements are prime (>= 2)
     */
    public static boolean containsComposite(int[] nums) {
        for (int n : nums) {
            if (n < 2 || !isPrime(n)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether a single integer is prime.
     * <p>
     * Uses trial division by 2 and odd numbers up to the square root of {@code n}.
     * </p>
     *
     * @param n integer to test for primality
     * @return {@code true} if {@code n} is a prime number (>= 2);
     *         {@code false} otherwise
     */
    private static boolean isPrime(int n) {
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
}
