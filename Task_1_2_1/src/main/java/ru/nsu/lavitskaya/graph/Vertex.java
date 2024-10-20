package ru.nsu.lavitskaya.graph;

import java.util.Objects;

/**
 * Represents a vertex in a graph.
 *
 * @param <T> The type of the value stored in the vertex.
 */
public class Vertex<T> {
    private final T value;
    
    /**
     * Creates a new vertex with the specified value.
     *
     * @param value The value to be stored in the vertex.
     */
    public Vertex(T value) {
        this.value = value;
    }

    /**
     * Retrieves the value stored in the vertex.
     *
     * @return The value of this vertex.
     */
    public T getValue() {
        return value;
    }

    /**
     * Compares this vertex to the specified object for equality.
     *
     * @param obj The object to be compared with this vertex.
     * @return {@code true} if the specified object is equal to this vertex; {@code false}
     *     otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return getValue().equals(((Vertex<?>) obj).getValue());
    }

    /**
     * Returns the hash code for this vertex.
     *
     * @return The hash code for this vertex.
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Returns a string representation of the vertex.
     *
     * @return A string representing the value of this vertex.
     */

    @Override
    public String toString() {
        return value.toString();
    }
}
