package ru.nsu.lavitskaya.hashtable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a hash table that stores key-value pairs.
 *
 * @param <K> the type of keys maintained by this hash table
 * @param <V> the type of mapped values
 */
public class HashTable<K, V> implements Iterable<Node<K, V>> {
    private static final int INITIAL_CAPACITY = 16;

    private Node<K, V>[] table;
    private int size;
    private int modCount;

    /**
     * Constructs an empty hash table with an initial capacity.
     */
    public HashTable() {
        modCount = 0;
        size = 0;
        table = new Node[INITIAL_CAPACITY];
    }

    /**
     * Returns the length of the hash table.
     * Needed for tests.
     *
     * @return the length of the hash table
     */
    public int getLength() {
        return table.length;
    }

    /**
     * Returns a list of nodes at the specified index in the hash table.
     *
     * @param index the index in the hash table
     * @return a list of nodes at the specified index
     */
    public List<Node<K, V>> getList(int index) {
        List<Node<K, V>> nodes = new ArrayList<>();
        Node<K, V> temp = table[index];
        while (temp != null) {
            nodes.add(temp);
            temp = temp.next;
        }
        return nodes;
    }

    /**
     * Inserts a key-value pair into the hash table. If the key already exists,
     * the value will be updated.
     *
     * @param key the key to be inserted
     * @param value the value to be associated with the key
     */
    public void put(K key, V value) {
        if (size >= (table.length * 0.75)) {
            resize();
        }
        int index = indexForKey(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> existingNode = table[index];
        while (existingNode != null) {
            if (existingNode.key.equals(key)) {
                existingNode.value = value; // Update value
                return;
            }
            existingNode = existingNode.next;
        }
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        modCount++;
    }

    /**
     * Removes the key-value pair associated with the specified key from the hash table.
     *
     * @param key the key to be removed
     */
    public void remove(K key) {
        int index = indexForKey(key);
        Node<K, V> node = table[index];
        Node<K, V> previousNode = null;

        while (node != null) {
            if (node.key.equals(key)) {
                if (previousNode == null) {
                    table[index] = node.next; // Remove head
                } else {
                    previousNode.next = node.next; // Remove from middle
                }
                size--;
                modCount++;
                return;
            }
            previousNode = node;
            node = node.next;
        }
    }

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value associated with the key, or {@code null} if the key does not exist
     */
    public V get(K key) {
        int index = indexForKey(key);
        Node<K, V> node = table[index];

        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    /**
     * Checks if a key exists in the hash table.
     *
     * @param key the key to check for existence
     * @return {@code true} if the key exists, {@code false} otherwise
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Computes the index for a given key.
     *
     * @param key the key for which to compute the index
     * @return the index in the hash table where the key is stored
     */
    private int indexForKey(K key) {
        return (key == null) ? 0 : (key.hashCode() & 0x7FFFFFFF) % table.length;
    }

    /**
     * Resizes the hash table to a new capacity, reallocating existing entries.
     */
    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> headNode : table) {
            Node<K, V> currentNode = headNode;
            while (currentNode != null) {
                int newIndex = (currentNode.key == null) ? 0 : (currentNode.key.hashCode()
                        & 0x7FFFFFFF) % newCapacity;

                Node<K, V> nodeToMove = currentNode;
                currentNode = currentNode.next;

                nodeToMove.next = newTable[newIndex];
                newTable[newIndex] = nodeToMove;
            }
        }

        table = newTable;
    }

    /**
     * Returns an iterator over the elements in this hash table.
     *
     * @return an iterator for iterating through the hash table
     */
    @Override
    public Iterator<Node<K, V>> iterator() {
        return new HashTableIterator();
    }

    /**
     * Inner class that implements the Iterator interface for the hash table.
     */
    private class HashTableIterator implements Iterator<Node<K, V>> {
        private int currentIndex = 0;
        private Node<K, V> currentNode = null;
        private int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            if (currentNode != null) {
                return true;
            }
            while (currentIndex < table.length) {
                if (table[currentIndex] != null) {
                    return true;
                }
                currentIndex++;
            }
            return false;
        }

        @Override
        public Node<K, V> next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (currentNode == null) {

                while (currentNode == null && currentIndex < table.length) {
                    currentNode = table[currentIndex++];
                }

                if (currentNode == null) {
                    throw new NoSuchElementException();
                }
            }

            Node<K, V> nodeToReturn = currentNode;
            currentNode = currentNode.next;
            return nodeToReturn;
        }
    }


    /**
     * Compares the specified object with this hash table for equality.
     * Returns {@code true} if the specified object is also a hash table
     * and the two hash tables have the same size and corresponding keys and values.
     *
     * @param obj the object to be compared for equality with this hash table
     * @return {@code true} if the specified object is equal to this hash table
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HashTable<?, ?> other)) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }

        for (int i = 0; i < table.length; i++) {
            Node<K, V> thisNode = this.table[i];
            Node<?, ?> otherNode = other.table[i];

            while (thisNode != null || otherNode != null) {
                if (thisNode == null || otherNode == null) {
                    return false;
                }
                if (!thisNode.equals(otherNode)) {
                    return false;
                }
                thisNode = thisNode.next;
                otherNode = otherNode.next;
            }
        }
        return true;
    }

    /**
     * Returns a string representation of this hash table. The string
     * representation consists of the indices of the table, each associated
     * with its linked list of nodes.
     *
     * @return a string representation of the hash table
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                sb.append(i).append(" {");
                Node<K, V> temp = table[i];
                while (temp != null) {
                    sb.append(temp).append(", ");
                    temp = temp.next;
                }
                sb.setLength(sb.length() - 2);
                sb.append("}\n");
            }

        }
        return sb.toString();
    }
}

