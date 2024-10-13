package ru.nsu.lavitskaya.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AdjacencyMatrixGraph<T> implements Graph<T>{
    private List<Vertex<T>> vertices;
    private int[][] adjacencyMatrix;

    public AdjacencyMatrixGraph() {
        vertices = new ArrayList<>();
        adjacencyMatrix = new int[0][0];
    }

    @Override
    public void addVertex(Vertex<T> vertex) {
        vertices.add(vertex);
        int newSize = vertices.size();
        int[][] newMatrix = new int[newSize][newSize];

        for (int i = 0; i < newSize - 1; i++) {
            newMatrix[i] = Arrays.copyOf(adjacencyMatrix[i], newSize);
        }
        adjacencyMatrix = newMatrix;

    }

    @Override
    public void removeVertex(Vertex<T> vertex) {
        int index = vertices.indexOf(vertex);
        if (index != -1) {
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

    @Override
    public void addEdge(Edge<T> edge) {
        int fromIndex = vertices.indexOf(edge.getFrom());
        int toIndex = vertices.indexOf(edge.getTo());
        if (fromIndex != -1 && toIndex != -1) {
            adjacencyMatrix[fromIndex][toIndex] += 1;
        }
    }

    @Override
    public void removeEdge(Edge<T> edge) {
        int fromIndex = vertices.indexOf(edge.getFrom());
        int toIndex = vertices.indexOf(edge.getTo());
        if (fromIndex != -1 && toIndex != -1) {
            adjacencyMatrix[fromIndex][toIndex] -= 1;
        }
    }

    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        List<Vertex<T>> neighbors = new ArrayList<>();
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            for (int i = 0; i < vertices.size(); i++) {
                if (adjacencyMatrix[index][i] != 0 && i != index) {
                    neighbors.add(vertices.get(i));
                }
            }
        }
        return neighbors;
    }

    @Override
    public List<Vertex<T>> getVertices() {
        return vertices;
    }

    @Override
    public void readFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                String[] vertexNames = line.trim().split("\\s+");
                for (int i = 1; i < vertexNames.length; i++) {
                    addVertex(new Vertex<>((T) vertexNames[i]));
                }
            }
            int vertexCount = vertices.size();
            adjacencyMatrix = new int[vertexCount][vertexCount];

            for (int i = 0; i < vertexCount; i++) {
                line = reader.readLine();
                if (line != null) {
                    String[] values = line.trim().split("\\s+");
                    for (int j = 1; j < values.length; j++) {
                        adjacencyMatrix[i][j - 1] = Integer.parseInt(values[j]);
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading the graph from file", e);
        } catch (NumberFormatException e) {
            throw new IOException("Error parsing number from file", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AdjacencyMatrixGraph<?> that = (AdjacencyMatrixGraph<?>) obj;
        if (this.vertices.size() != that.vertices.size()) return false;
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            if (!Arrays.equals(this.adjacencyMatrix[i], that.adjacencyMatrix[i])) {
                return false;
            }
        }

        return true;
    }

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
