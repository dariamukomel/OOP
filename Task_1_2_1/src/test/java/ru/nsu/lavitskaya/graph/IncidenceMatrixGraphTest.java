package ru.nsu.lavitskaya.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the IncidenceMatrixGraph class.
 *
 * <p>This class contains various test cases to validate the functionality of
 * the IncidenceMatrixGraph, including adding and removing vertices and edges,
 * retrieving neighbors, checking equality, generating hash codes, and
 * reading graphs from files.</p>
 */
class IncidenceMatrixGraphTest {
    @Test
    public void testAddRemoveVerticesAndEdges() {
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Vertex<String> v3 = new Vertex<>("C");

        graph.addVertex(v1);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        assertEquals("A | \n" + "B | \n" + "C | \n", graph.toString());

        Vertex<String> v4 = new Vertex<>("D");
        Vertex<String> v5 = new Vertex<>("E");
        graph.addEdge(new Edge<>(v4, v5));
        graph.addEdge(new Edge<>(v1, v2));
        assertEquals("A | 0 1 \n" + "B | 0 -1 \n" + "C | 0 0 \n" + "D | 1 0 \n"
                + "E | -1 0 \n", graph.toString());

        graph.removeVertex(v4);
        assertEquals("A | 1 \n" + "B | -1 \n" + "C | 0 \n" + "E | 0 \n", graph.toString());
    }

    @Test
    public void testGetNeighbors() {
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Vertex<String> v3 = new Vertex<>("C");
        graph.addEdge(new Edge<>(v1, v2));
        graph.addEdge(new Edge<>(v1, v2));
        graph.addEdge(new Edge<>(v1, v3));
        List<Vertex<String>> expected = new ArrayList<>();
        expected.add(v2);
        expected.add(v2);
        expected.add(v3);
        List<Vertex<String>> neighbors = graph.getNeighbors(v1);
        assertEquals(expected, neighbors);
    }

    @Test
    public void testEquals() {
        IncidenceMatrixGraph<String> graph1 = new IncidenceMatrixGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Vertex<String> v3 = new Vertex<>("C");
        graph1.addEdge(new Edge<>(v1, v2));
        graph1.addEdge(new Edge<>(v1, v2));
        graph1.addEdge(new Edge<>(v1, v3));
        IncidenceMatrixGraph<String> graph2 = new IncidenceMatrixGraph<>();
        graph2.addEdge(new Edge<>(v1, v3));
        graph2.addEdge(new Edge<>(v1, v3));
        graph2.addEdge(new Edge<>(v1, v2));
        assertFalse(graph1.equals(graph2));

        IncidenceMatrixGraph<String> graph3 = new IncidenceMatrixGraph<>();
        graph3.addVertex(v1);
        graph3.addVertex(v2);
        graph3.addEdge(new Edge<>(v1, v2));
        IncidenceMatrixGraph<String> graph4 = new IncidenceMatrixGraph<>();
        graph4.addVertex(v2);
        graph4.addVertex(v1);
        graph4.addEdge(new Edge<>(v1, v2));
        assertTrue(graph3.equals(graph4));

    }

    @Test
    public void testHashCode() {
        IncidenceMatrixGraph<String> graph1 = new IncidenceMatrixGraph<>();
        IncidenceMatrixGraph<String> graph2 = new IncidenceMatrixGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        graph1.addEdge(new Edge<>(v1, v2));
        graph2.addEdge(new Edge<>(v1, v2));
        assertTrue(graph1.hashCode() == graph2.hashCode());
    }

    @Test
    public void testReadFromFile() throws IOException {
        File tempFile;
        tempFile = File.createTempFile("testGraph", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("A|1 1 \n");
            writer.write("B|-1 0 \n");
            writer.write("C|0 -1 \n");
        }
        IncidenceMatrixGraph<String> graph = new IncidenceMatrixGraph<>();
        graph.readFromFile(tempFile);
        List<Vertex<String>> vertices = graph.getVertices();
        assertEquals(3, vertices.size());
        assertTrue(vertices.contains(new Vertex<>("A")));
        assertTrue(vertices.contains(new Vertex<>("B")));
        assertTrue(vertices.contains(new Vertex<>("C")));

        List<Vertex<String>> neighborsOfA = graph.getNeighbors(new Vertex<>("A"));
        assertEquals(2, neighborsOfA.size());
        assertTrue(neighborsOfA.contains(new Vertex<>("B")));
        assertTrue(neighborsOfA.contains(new Vertex<>("C")));

        if (tempFile.exists()) {
            tempFile.delete();
        }
    }


}