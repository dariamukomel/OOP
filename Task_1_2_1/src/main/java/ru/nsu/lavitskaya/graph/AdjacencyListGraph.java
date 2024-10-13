package ru.nsu.lavitskaya.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class AdjacencyListGraph<T> implements Graph<T> {
    private HashMap<Vertex<T>, List<Vertex<T>>> adjacencyList;

    public AdjacencyListGraph() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public void addVertex(Vertex<T> vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(Vertex<T> vertex) {
        adjacencyList.remove(vertex);
        for (List<Vertex<T>> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(Edge<T> edge) {
        adjacencyList.putIfAbsent(edge.getFrom(), new ArrayList<>());
        adjacencyList.putIfAbsent(edge.getTo(), new ArrayList<>());
        adjacencyList.get(edge.getFrom()).add(edge.getTo());
    }

    @Override
    public void removeEdge(Edge<T> edge) {
        List<Vertex<T>> neighbors = adjacencyList.get(edge.getFrom());
        if (neighbors != null) {
            neighbors.remove(edge.getTo());
        }
    }

    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        List<Vertex<T>> neighbors = new ArrayList<>(adjacencyList.getOrDefault(vertex,
                new ArrayList<>()));

        return neighbors;
    }

    public List<Vertex<T>> getVertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    @Override
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AdjacencyListGraph<?> that = (AdjacencyListGraph<?>) obj;
        if (this.adjacencyList.size() != that.adjacencyList.size()) return false;
        for (Vertex<T> vertex : this.adjacencyList.keySet()) {
            List<Vertex<T>> thisNeighbors = this.adjacencyList.get(vertex);
            List<? extends Vertex<?>> thatNeighbors = that.adjacencyList.get(vertex);
            if (!thisNeighbors.equals(thatNeighbors)) {
                return false;
            }
        }
        return true;
    }

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
