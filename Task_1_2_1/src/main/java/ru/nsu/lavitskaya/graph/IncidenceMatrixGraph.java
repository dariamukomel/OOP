package ru.nsu.lavitskaya.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class IncidenceMatrixGraph<T> implements Graph<T> {
    private List<Vertex<T>> vertices;
    private List<Edge<T>> edges;
    private int[][] incidenceMatrix;

    public IncidenceMatrixGraph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        incidenceMatrix = new int[0][0];
    }

    @Override
    public void addVertex(Vertex<T> vertex) {
        vertices.add(vertex);
        int newSize = vertices.size();
        int[][] newMatrix = new int[newSize][edges.size()];

        for (int i = 0; i < newSize - 1; i++) {
            newMatrix[i] = Arrays.copyOf(incidenceMatrix[i], edges.size());
        }
        incidenceMatrix = newMatrix;
    }

    @Override
    public void removeVertex(Vertex<T> vertex) {
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            List<Edge<T>> edgesToRemove = new ArrayList<>();
            for (Edge<T> edge :edges) {
                if(edge.getFrom().equals(vertex) || edge.getTo().equals(vertex)) {
                    edgesToRemove.add(edge);
                }
            }
            for (Edge<T> edge : edgesToRemove) {
                removeEdge(edge);
            }
            vertices.remove(index);
            int newSize = vertices.size();
            int[][] newMatrix = new int[newSize][edges.size()];

            for (int i = 0, newRow = 0; i < incidenceMatrix.length; i++) {
                if (i != index) {
                    for (int j = 0; j < edges.size(); j++) {
                        newMatrix[newRow][j] = incidenceMatrix[i][j];
                    }
                    newRow++;
                }
            }
            incidenceMatrix = newMatrix;
        }
    }

    @Override
    public void addEdge(Edge<T> edge) {
        edges.add(edge);
        int fromIndex = vertices.indexOf(edge.getFrom());
        int toIndex = vertices.indexOf(edge.getTo());
        if (fromIndex != -1 && toIndex != -1) {
            int newSize = edges.size();
            int[][] newMatrix = new int[vertices.size()][newSize];
            for (int i = 0; i < vertices.size(); i++) {
                newMatrix[i] = Arrays.copyOf(incidenceMatrix[i], newSize);
            }
            incidenceMatrix = newMatrix;
            incidenceMatrix[fromIndex][newSize - 1] = 1;
            incidenceMatrix[toIndex][newSize - 1] = -1;
        }
    }

    @Override
    public void removeEdge(Edge<T> edge) {
        int edgeIndex = edges.indexOf(edge);
        if (edgeIndex != -1) {
            edges.remove(edgeIndex);
            int newSize = edges.size();
            int[][] newMatrix = new int[vertices.size()][newSize];
            for(int i = 0; i < incidenceMatrix.length; i++) {
                for (int j = 0, newCol = 0; j < incidenceMatrix[i].length; j++) {
                    if(j != edgeIndex) {
                        newMatrix[i][newCol] = incidenceMatrix[i][j];
                        newCol++;
                    }
                }
            }
            incidenceMatrix =  newMatrix;
        }
    }

    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        List<Vertex<T>> neighbors = new ArrayList<>();
        int vertexIndex = vertices.indexOf(vertex);
        if (vertexIndex != -1) {
            for (int j = 0; j < edges.size(); j++) {
                if(incidenceMatrix[vertexIndex][j] == 1) {
                    neighbors.add(edges.get(j).getTo());
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
        HashMap<Vertex<T>, int[]> incidenceMap = new HashMap<>();
        List<Vertex<T>> vertexList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int countOfEdges = -1;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split("\\|");

                if (parts.length != 2) {
                    throw new IOException("Invalid line format: " + line);
                }
                String vertexName = parts[0].trim();
                Vertex<T> vertex = new Vertex<>((T) vertexName);
                String[] incidenceStrings = parts[1].trim().split("\\s+");
                int[] incidences = new int[incidenceStrings.length];
                for (int i = 0; i < incidenceStrings.length; i++) {
                    incidences[i] = Integer.parseInt(incidenceStrings[i].trim());
                }
                if(countOfEdges == -1) {
                    countOfEdges = incidenceStrings.length;
                }
                incidenceMap.put(vertex, incidences);
                vertexList.add(vertex);
            }
            for (Vertex<T> v : vertexList) {
                addVertex(v);
            }
            for (int edgeIndex = 0; edgeIndex < countOfEdges; edgeIndex++) {
                Vertex<T> fromVertex = null;
                Vertex<T> toVertex = null;
                for (Map.Entry<Vertex<T>, int[]> entry : incidenceMap.entrySet()) {
                    int[] incidences = entry.getValue();
                    if (incidences[edgeIndex] == 1) {
                        fromVertex = entry.getKey();
                    } else if (incidences[edgeIndex] == -1) {
                        toVertex = entry.getKey();
                    }
                }
                if (fromVertex != null && toVertex != null) {
                    addEdge(new Edge<>(fromVertex, toVertex));
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
        IncidenceMatrixGraph<?> that = (IncidenceMatrixGraph<?>) obj;
        if (this.vertices.size() != that.vertices.size()) return false;
        if (this.edges.size() != that.edges.size()) return false;
        for (int i = 0; i < this.incidenceMatrix.length; i++) {
            if (!Arrays.equals(this.incidenceMatrix[i], that.incidenceMatrix[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < vertices.size(); i++) {
            builder.append(vertices.get(i).toString()).append(" | ");
            for (int j = 0; j < edges.size(); j++) {
                builder.append(incidenceMatrix[i][j]).append(" ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
