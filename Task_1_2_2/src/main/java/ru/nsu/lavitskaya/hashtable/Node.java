package ru.nsu.lavitskaya.hashtable;

import java.util.Objects;

/**
 * Represents a node in the hash table, which holds a key-value pair
 * and a reference to the next node in the linked list.
 *
 * @param <K> the type of keys maintained by this node
 * @param <V> the type of values associated with the keys
 */
public class Node<K, V> {
    final K key;
    V value;
    Node<K, V> next;

    /**
     * Constructs a new node with the specified key and value.
     *
     * @param key   the key to be associated with this node
     * @param value the value to be associated with this node
     */
    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Indicates whether some other object is "equal to" this node.
     * The equality is determined based on the key and value of the node.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if the specified object is equal to this node
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Node<?, ?> otherNode)) {
            return false;
        }
        return (this.key.equals(otherNode.key) && this.value.equals(otherNode.value));
    }

    /**
     * Returns a hash code value for the node. The hash code
     * is computed based on the key and value.
     *
     * @return a hash code value for this node
     */
    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    /**
     * Returns a string representation of the node. The string representation
     * consists of the key and value in the form "key: value".
     *
     * @return a string representation of this node
     */
    @Override
    public String toString() {
        return (key == null ? "null" : key.toString()) + ": " +
                (value == null ? "null" : value.toString());
    }
}
