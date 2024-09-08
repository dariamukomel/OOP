package ru.nsu.lavitskaya;

/**
 * The HeapSort class provides a static method for heap sort algorithm.
 */

public class HeapSort {

    /**
     * Sorts the input array using the heap sort algorithm.
     *
     * @param arr the input array to be sorted
     */

    public static void heapSort(int[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, i, n);
        }

        for (int i = n - 1; i >= 0; i--) {
            int tmp = arr[i];
            arr[i] = arr[0];
            arr[0] = tmp;
            heapify(arr, 0, i);
        }
    }

    /**
     * Heapify the subtree rooted at index i, n is size of heap.
     *
     * @param arr the input array to be heapified
     * @param i the index of the current node
     * @param n the size of the heap
     */

    private static void heapify(int[] arr, int i, int n) {
        int l = i * 2 + 1;
        int r = i * 2 + 2;
        int largest = i;

        if (l < n && arr[l] > arr[largest]) {
            largest = l;
        }
        if (r < n && arr[r] > arr[largest]) {
            largest = r;
        }
        if (i != largest) {
            int tmp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = tmp;

            heapify(arr, largest, n);
        }

    }
}
