package ru.nsu.lavitskaya;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Random;

/** Testing */
class MainTest {
    @Test
    public void testHeapSort_sortedInput() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        int[] result = Main.heapSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testHeapSort_reverseSortedInput() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        int[] result = Main.heapSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testHeapSort_randomOrderInput() {
        int[] arr = {3, 5, 1, 4, 2};
        int[] expected = {1, 2, 3, 4, 5};
        int[] result = Main.heapSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testHeapSort_duplicateValues() {
        int[] arr = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int[] expected = {1, 2, 2, 3, 3, 4, 5, 5, 6};
        int[] result = Main.heapSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testHeapSortTimeComplexity() {
        int[] arr = generateRandomArray(100000);
        long startTime = System.nanoTime();
        Main.heapSort(arr);
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("Time taken for sorting: " + elapsedTime + " nanoseconds");
        assertTrue(elapsedTime < 1000000000);
    }

    private int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt();
        }
        return arr;
    }
}