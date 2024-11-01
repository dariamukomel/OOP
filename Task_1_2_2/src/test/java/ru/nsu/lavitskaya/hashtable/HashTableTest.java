package ru.nsu.lavitskaya.hashtable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Unit tests for the HashTable class.
 */
class HashTableTest {
    private static HashTable<Integer, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>();
    }

    @Test
    public void testCollisions() {
        hashTable.put(1, 1);
        hashTable.put(17, 1);
        List<Node<Integer, Integer>> expected = new ArrayList<>();
        expected.add(new Node<>(17, 1));
        expected.add(new Node<>(1, 1));
        assertEquals(expected, hashTable.getList(1));

    }

    @Test
    public void testResize() {
        for (int i = 0; i < 12; i++) {
            hashTable.put(i, i);
        }
        assertEquals(16, hashTable.getLength());
        hashTable.put(13, 13);
        assertEquals(32, hashTable.getLength());
    }

    @Test
    public void testUpdate() {
        hashTable.put(1, 1);
        assertEquals(1, hashTable.get(1));
        hashTable.put(1, 2);
        assertEquals(2, hashTable.get(1));
        assertNull(hashTable.get(2));
    }

    @Test
    public void testRemove() {
        hashTable.put(1, 1);
        hashTable.put(17, 1);
        hashTable.put(2, 2);
        hashTable.put(18, 2);
        hashTable.put(34, 2);
        hashTable.put(3, 3);

        hashTable.remove(17);
        hashTable.remove(18);
        hashTable.remove(3);
        List<Node<Integer, Integer>> expected1 = new ArrayList<>();
        expected1.add(new Node<>(1, 1));
        assertEquals(expected1, hashTable.getList(1), "remove first node of list");
        List<Node<Integer, Integer>> expected2 = new ArrayList<>();
        expected2.add(new Node<>(34, 2));
        expected2.add(new Node<>(2, 2));
        assertEquals(expected2, hashTable.getList(2), "remove from middle");
        assertEquals(new ArrayList<>(), hashTable.getList(3));
    }

    @Test
    public void testContains() {
        hashTable.put(1, 1);
        assertTrue(hashTable.containsKey(1));
        assertFalse(hashTable.containsKey(17));
    }

    @Test
    public void testIterator() {
        hashTable.put(1, 1);
        hashTable.put(17, 1);
        hashTable.put(2, 2);
        hashTable.put(18, 2);
        hashTable.put(34, 2);
        hashTable.put(4, 4);
        List<Node<Integer, Integer>> nodes = new ArrayList<>();
        for (Node<Integer, Integer> node : hashTable) {
            nodes.add(node);
        }
        assertEquals("[17: 1, 1: 1, 34: 2, 18: 2, 2: 2, 4: 4]", nodes.toString());
    }

    @Test
    public void testEquals() {
        assertTrue(hashTable.equals(hashTable));

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        assertFalse(hashTable.equals(hashMap), "different type");

        HashTable<Integer, Integer> hashTable2 = new HashTable<>();
        hashTable.put(1, 1);
        assertFalse(hashTable.equals(hashTable2), "different size");

        hashTable2.put(2, 2);
        assertFalse(hashTable.equals(hashTable2), "different list");

        hashTable2.remove(2);
        hashTable2.put(17, 1);
        assertFalse(hashTable.equals(hashTable2), "different key, same list");

        hashTable.remove(1);
        hashTable.put(6, 6);
        HashTable<String, Integer> hashTable3 = new HashTable<>();
        hashTable3.put("One", 6);
        assertFalse(hashTable.equals(hashTable3), "One.hashCode%16 = 6");

    }

}