package ru.nsu.lavitskaya.hashtable;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTable<K, V> implements Iterable<Node<K, V>>{
    private static final int INITIAL_CAPACITY = 16;

    private Node<K, V>[] table;
    private int size;
    private int modCount;

    public HashTable() {
        modCount = 0;
        size = 0;
        table = new Node[INITIAL_CAPACITY];
    }

    public void put(K key, V value) {
        if (size >= (table.length * 0.75)) {
            resize();
        }
        int index = indexForKey(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> existingNode = table[index];
        if (existingNode == null) {
            table[index] = newNode;
            size++;
            modCount++;
        }else {
            while (existingNode != null) {
                if (existingNode.key.equals(key)) {
                    existingNode.value = value;// Update value
                    return;
                }
                existingNode = existingNode.next;
            }
            newNode.next = table[index];
            table[index] = newNode;
            size++;
            modCount++;
        }
    }

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

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    private int indexForKey(K key) {
        return (key == null) ? 0 : (key.hashCode() & 0x7FFFFFFF) % table.length;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> headNode : table) {
            Node<K, V> currentNode = headNode;
            while (currentNode != null) {
                int newIndex = (currentNode.key == null) ? 0 : (currentNode.key.hashCode() & 0x7FFFFFFF) % newCapacity;

                Node<K, V> nodeToMove = currentNode;
                currentNode = currentNode.next;

                nodeToMove.next = newTable[newIndex];
                newTable[newIndex] = nodeToMove;
            }
        }

        table = newTable;
    }

    @Override
    public Iterator<Node<K, V>> iterator() {
        return new HashTableIterator();
    }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < table.length; i++){
            if (table[i] != null) {
                sb.append(i).append(" {");
                Node<K, V> temp = table[i];
                while (temp!=null) {
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

