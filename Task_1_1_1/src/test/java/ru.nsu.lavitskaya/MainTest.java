package ru.nsu.lavitskaya;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    public void testHeapSort_sortedInput() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        int[] result = Main.HeapSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testHeapSort_reverseSortedInput() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        int[] result = Main.HeapSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testHeapSort_randomOrderInput() {
        int[] arr = {3, 5, 1, 4, 2};
        int[] expected = {1, 2, 3, 4, 5};
        int[] result = Main.HeapSort(arr);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testHeapSort_duplicateValues() {
        int[] arr = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int[] expected = {1, 2, 2, 3, 3, 4, 5, 5, 6};
        int[] result = Main.HeapSort(arr);
        assertArrayEquals(expected, result);
    }
}