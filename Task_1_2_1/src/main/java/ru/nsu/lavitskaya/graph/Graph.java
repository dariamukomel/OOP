package ru.nsu.lavitskaya.graph;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Represents a generic graph interface.
 *
 * <p>This interface defines the basic operations that can be performed on a graph,
 * including adding and removing vertices and edges, retrieving neighbors,
 * reading a graph from a file, and checking for equality and hash codes.</p>
 *
 * @param <T> the type of the vertex values in the graph
 */
public interface Graph<T> {
    void addVertex(Vertex<T> vertex);

    void removeVertex(Vertex<T> vertex);

    void addEdge(Edge<T> edge);

    void removeEdge(Edge<T> edge);

    List<Vertex<T>> getNeighbors(Vertex<T> vertex);

    List<Vertex<T>> getVertices();

    void readFromFile(File file) throws IOException;

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();
}
