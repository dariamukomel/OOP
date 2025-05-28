package ru.nsu.lavitskaya.mr;

/**
 * Class for prime number checks.
 * <p>
 * Provides methods to determine if an array contains any composite numbers
 * and to test the primality of individual integers.
 * </p>
 */
public class PrimeChecker {

    public static boolean containsComposite(int[] nums) {
        for (int n : nums) {
            if (n < 2 || !isPrime(n)) {
                return true;
            }
        }
        return false;
    }

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
