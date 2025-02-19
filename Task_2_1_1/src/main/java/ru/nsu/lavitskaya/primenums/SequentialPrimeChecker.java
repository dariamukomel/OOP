package ru.nsu.lavitskaya.primenums;

/**
 * Implementation of {@link PrimeChecker} using a sequential approach.
 */
public class SequentialPrimeChecker implements PrimeChecker{
    @Override
    public boolean checkNumbers(int[] numbers) {
        for (int num : numbers) {
            if (!isPrime(num)) {
                return true;
            }
        }
        return false;
    }
}
