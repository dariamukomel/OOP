package ru.nsu.lavitskaya.graph;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
    String toString();
}
