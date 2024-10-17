package ru.nsu.lavitskaya.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Represents a graph using an adjacency list representation.
 *
 * @param <T> the type of the vertex values in the graph
 */
public class AdjacencyListGraph<T> implements Graph<T> {
    private HashMap<Vertex<T>, List<Vertex<T>>> adjacencyList;

    /**
     * Constructs an empty AdjacencyListGraph.
     */
    public AdjacencyListGraph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex the vertex to be added
     */
    @Override
    public void addVertex(Vertex<T> vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex the vertex to be removed
     */
    @Override
    public void removeVertex(Vertex<T> vertex) {
        adjacencyList.remove(vertex);
        for (List<Vertex<T>> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    /**
     * Adds an edge to the graph.
     *
     * @param edge the edge to be added
     */
    @Override
    public void addEdge(Edge<T> edge) {
        adjacencyList.putIfAbsent(edge.getFrom(), new ArrayList<>());
        adjacencyList.putIfAbsent(edge.getTo(), new ArrayList<>());
        adjacencyList.get(edge.getFrom()).add(edge.getTo());
    }

    /**
     * Removes an edge from the graph.
     *
     * @param edge the edge to be removed
     */
    @Override
    public void removeEdge(Edge<T> edge) {
        List<Vertex<T>> neighbors = adjacencyList.get(edge.getFrom());
        if (neighbors != null) {
            neighbors.remove(edge.getTo());
        }
    }

    /**
     * Returns a list of neighbors for a given vertex.
     *
     * @param vertex the vertex for which neighbors are to be retrieved
     * @return a list of neighboring vertices
     */
    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        List<Vertex<T>> neighbors = new ArrayList<>(adjacencyList.getOrDefault(vertex,
                new ArrayList<>()));
        return neighbors;
    }

    /**
     * Returns an unmodifiable list of all vertices in the graph.
     *
     * @return a list of vertices in the graph
     */
    public List<Vertex<T>> getVertices() {
        return Collections.unmodifiableList(new ArrayList<>(adjacencyList.keySet()));
    }

    /**
     * Reads a graph from a specified file.
     *
     * @param file the file from which to read the graph
     * @throws IOException if an error occurs while reading the file
     */
    @Override
    @SuppressWarnings("unchecked")
    public void readFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split(":");
                if (parts.length != 2) {
                    throw new IOException("Invalid line format: " + line);
                }
                String vertexName = parts[0].trim();
                String neighborsPart = parts[1].trim();

                Vertex<T> vertex = new Vertex<>((T) vertexName);
                addVertex(vertex);

                if (neighborsPart.startsWith("[") && neighborsPart.endsWith("]")) {
                    String neighborsList = neighborsPart.substring(1, neighborsPart.length() - 1).trim();
                    if (!neighborsList.isEmpty()) {
                        String[] neighbors = neighborsList.split(",");
                        for (String neighborName : neighbors) {
                            neighborName = neighborName.trim();
                            Vertex<T> neighbor = new Vertex<>((T) neighborName);
                            addVertex(neighbor);
                            addEdge(new Edge<>(vertex, neighbor));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading the graph from file", e);
        }
    }

    /**
     * Checks if this graph is equal to another object.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AdjacencyListGraph<T> that = (AdjacencyListGraph<T>) obj;
        if (this.getVertices().size() != that.getVertices().size()) return false;
        for (Vertex<T> currVertex : this.getVertices()) {
            List<Vertex<T>> thisNeighbors = getNeighbors(currVertex);
            List<Vertex<T>> thatNeighbors = that.getNeighbors(currVertex);
            if (thatNeighbors.size() != thisNeighbors.size()) {
                return false;
            }
            for (int i = 0; i < thisNeighbors.size(); i++) {
                for (int j = 0; j < thatNeighbors.size(); j++) {
                    if (thisNeighbors.get(i).equals(thatNeighbors.get(j))) {
                        thatNeighbors.remove(j);
                        break;
                    }
                }
            }
            if (!thatNeighbors.isEmpty()) {
                return false;
            }

        }
        return true;
    }

    /**
     * Generates a hash code for this graph.
     *
     * @return a hash code value for this graph
     */
    @Override
    public int hashCode() {
        return Objects.hash(adjacencyList);
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return a string representation of the graph
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Vertex<T> vertex : adjacencyList.keySet()) {
            builder.append(vertex.toString()).append(": ");
            List<Vertex<T>> neighbors = adjacencyList.get(vertex);
            if (neighbors.isEmpty()) {
                builder.append("[]");
            } else {
                builder.append(neighbors.toString());
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
