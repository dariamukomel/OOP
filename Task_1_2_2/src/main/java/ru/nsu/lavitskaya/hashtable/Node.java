package ru.nsu.lavitskaya.hashtable;

import java.util.Objects;

public class Node<K, V> {
    final K key;
    V value;
    Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Node<?, ?> otherNode)) {
            return false;
        }
        if (!this.key.getClass().equals(otherNode.key.getClass()) ||
                !this.value.getClass().equals(otherNode.value.getClass())) {
            return false;
        }
        return (this.key.equals(otherNode.key) && this.value.equals(otherNode.value));
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return key.toString() +": "+ value.toString();
    }
}
