package ru.nsu.lavitskaya.graph;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a directed graph using an adjacency matrix to store edges.
 *
 * @param <T> The type of the values stored in the vertices.
 */
public class AdjacencyMatrixGraph<T> implements Graph<T> {
    private List<Vertex<T>> vertices;
    private List<Edge<T>> edges;
    private int[][] adjacencyMatrix;

    /**
     * Constructs a new empty graph.
     */
    public AdjacencyMatrixGraph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        adjacencyMatrix = new int[0][0];
    }

    /**
     * Adds a new vertex to the graph.
     *
     * @param vertex The vertex to be added.
     */
    @Override
    public void addVertex(Vertex<T> vertex) {
        int index = vertices.indexOf(vertex);
        if (index == -1) {
            vertices.add(vertex);
            int newSize = vertices.size();
            int[][] newMatrix = new int[newSize][newSize];

            for (int i = 0; i < newSize - 1; i++) {
                newMatrix[i] = Arrays.copyOf(adjacencyMatrix[i], newSize);
            }
            adjacencyMatrix = newMatrix;
        }


    }

    /**
     * Removes a vertex from the graph and all edges associated with it.
     *
     * @param vertex The vertex to be removed.
     */
    @Override
    public void removeVertex(Vertex<T> vertex) {
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            List<Edge<T>> edgesToRemove = new ArrayList<>();
            for (Edge<T> edge : edges) {
                if (edge.getFrom().equals(vertex) || edge.getTo().equals(vertex)) {
                    edgesToRemove.add(edge);
                }
            }
            for (Edge<T> edge : edgesToRemove) {
                removeEdge(edge);
            }

            vertices.remove(index);
            int newSize = vertices.size();
            int[][] newMatrix = new int[newSize][newSize];

            for (int i = 0, newRow = 0; i < adjacencyMatrix.length; i++) {
                if (i != index) {
                    for (int j = 0, newCol = 0; j < adjacencyMatrix[i].length; j++) {
                        if (j != index) {
                            newMatrix[newRow][newCol] = adjacencyMatrix[i][j];
                            newCol++;
                        }
                    }
                    newRow++;
                }
            }
            adjacencyMatrix = newMatrix;
        }
    }

    /**
     * Adds a directed edge between two vertices in the graph.
     * If either vertex does not exist, it will be added to the graph.
     *
     * @param edge The edge to be added.
     */
    @Override
    public void addEdge(Edge<T> edge) {
        int fromIndex = vertices.indexOf(edge.getFrom());
        int toIndex = vertices.indexOf(edge.getTo());
        if (fromIndex == -1) {
            addVertex(edge.getFrom());
            fromIndex = vertices.indexOf(edge.getFrom());
        }
        if (toIndex == -1) {
            addVertex(edge.getTo());
            toIndex = vertices.indexOf(edge.getTo());
        }

        edges.add(new Edge<>(edge.getFrom(), edge.getTo()));
        adjacencyMatrix[fromIndex][toIndex] += 1;

    }

    /**
     * Removes a directed edge between two vertices in the graph.
     *
     * @param edge The edge to be removed.
     */
    @Override
    public void removeEdge(Edge<T> edge) {
        int fromIndex = vertices.indexOf(edge.getFrom());
        int toIndex = vertices.indexOf(edge.getTo());
        if (fromIndex != -1 && toIndex != -1 && adjacencyMatrix[fromIndex][toIndex] > 0) {
            edges.remove(edge);
            adjacencyMatrix[fromIndex][toIndex] -= 1;
        }
    }

    /**
     * Retrieves the list of neighbors for the specified vertex in the graph.
     *
     * @param vertex The vertex whose neighbors are to be retrieved.
     * @return A list of neighboring vertices, or null if the specified vertex is not present
     *     in the graph.
     */
    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        int index = vertices.indexOf(vertex);
        if (index == -1) {
            return null;
        }
        List<Vertex<T>> neighbors = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < adjacencyMatrix[index][i]; j++) {
                neighbors.add(vertices.get(i));
            }
        }
        return neighbors;
    }

    /**
     * Retrieves the list of all vertices in the graph.
     *
     * @return An unmodifiable list of all vertices in the graph.
     */
    @Override
    public List<Vertex<T>> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    /**
     * Reads a graph from a specified file, initializing the vertices and adjacency matrix.
     *
     * @param file The file from which to read the graph data.
     * @throws IOException If an error occurs while reading the file or parsing the numbers.
     */
    @Override
    public void readFromFile(File file, Function<String, T> converter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                String[] vertexNames = line.trim().split("\\s+");
                for (int i = 0; i < vertexNames.length; i++) {
                    addVertex(new Vertex<>(converter.apply(vertexNames[i])));
                }
            }
            int vertexCount = vertices.size();

            for (int i = 0; i < vertexCount; i++) {
                line = reader.readLine();
                if (line != null) {
                    String[] values = line.trim().split("\\s+");
                    for (int j = 1; j < values.length; j++) {
                        int countOfEdges = Integer.parseInt(values[j]);
                        for (int y = 0; y < countOfEdges; y++) {
                            addEdge(new Edge<>(vertices.get(i), vertices.get(j - 1)));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading the graph from file", e);
        } catch (NumberFormatException e) {
            throw new IOException("Error parsing number from file", e);
        }
    }

    /**
     * Compares this graph to the specified object for equality.
     *
     * @param obj The object to be compared with this graph.
     * @return {@code true} if the specified object is equal to this graph; {@code false} otherwise.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AdjacencyMatrixGraph<T> that = (AdjacencyMatrixGraph<T>) obj;
        if (this.vertices.size() != that.vertices.size()) {
            return false;
        }
        for (Vertex<T> currVertex : this.vertices) {
            List<Vertex<T>> thisNeighbors = getNeighbors(currVertex);
            List<Vertex<T>> thatNeighbors = that.getNeighbors(currVertex);
            if (thatNeighbors == null || thatNeighbors.size() != thisNeighbors.size()) {
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
     * Returns the hash code for this graph.
     *
     * @return The hash code for this graph based on its vertices and edges.
     */
    public int hashCode() {
        return Objects.hash(vertices, edges);
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return A string representing the graph in a human-readable format.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("  ");
        for (Vertex<T> vertex : vertices) {
            builder.append(vertex.toString()).append(" ");
        }
        builder.append("\n");

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            builder.append(vertices.get(i).toString()).append(" ");
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                builder.append(adjacencyMatrix[i][j]).append(" ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }

}
