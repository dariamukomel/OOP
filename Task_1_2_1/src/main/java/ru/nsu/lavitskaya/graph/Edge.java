package ru.nsu.lavitskaya.graph;

import java.util.Objects;

/**
 * Represents an edge connecting two vertices in a graph.
 *
 * @param <T> The type of the value stored in the vertices.
 */
public class Edge<T> {
    private final Vertex<T> from;
    private final Vertex<T> to;

    /**
     * Creates a new edge from the specified source vertex to the target vertex.
     *
     * @param from The source vertex of the edge.
     * @param to   The target vertex of the edge.
     */
    public Edge(Vertex<T> from, Vertex<T> to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Retrieves the source vertex of the edge.
     *
     * @return The source vertex of the edge.
     */
    public Vertex<T> getFrom() {
        return from;
    }

    /**
     * Retrieves the target vertex of the edge.
     *
     * @return The target vertex of the edge.
     */
    public Vertex<T> getTo() {
        return to;
    }

    /**
     * Compares this edge to the specified object for equality.
     *
     * @param obj The object to be compared with this edge.
     * @return {@code true} if the specified object is equal to this edge; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return getFrom().equals(((Edge<?>) obj).getFrom()) &&
                getTo().equals(((Edge<?>) obj).getTo());
    }

    /**
     * Returns the hash code for this edge.
     *
     * @return The hash code for this edge.
     */
    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    /**
     * Returns a string representation of the edge.
     *
     * @return A string representing the edge in the format "from->to".
     */
    @Override
    public String toString() {
        return from + "->" + to;
    }
}
