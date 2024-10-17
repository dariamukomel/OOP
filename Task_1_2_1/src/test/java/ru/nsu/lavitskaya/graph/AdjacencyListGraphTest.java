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
 * Unit tests for the AdjacencyListGraph class.
 *
 * <p>This class contains various test cases to validate the functionality
 * of the AdjacencyListGraph, including adding/removing vertices and edges,
 * retrieving neighbors, checking equality, generating hash codes, and
 * reading graphs from files.</p>
 */
class AdjacencyListGraphTest {
    @Test
    public void testAddRemoveVerticesAndEdges() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Vertex<String> v3 = new Vertex<>("C");
        graph.addVertex(v1);
        assertEquals("A: []\n", graph.toString());
        graph.addEdge(new Edge<>(v1, v2));
        assertEquals("A: [B]\n" + "B: []\n", graph.toString());
        graph.addEdge(new Edge<>(v1, v3));
        graph.addEdge(new Edge<>(v2, v3));
        assertEquals("A: [B, C]\n" + "B: [C]\n" + "C: []\n", graph.toString());
        graph.removeVertex(v3);
        assertEquals("A: [B]\n" + "B: []\n", graph.toString());
        graph.removeEdge(new Edge<>(v1, v2));
        assertEquals("A: []\n" + "B: []\n", graph.toString());

    }

    @Test
    public void testGetNeighbors() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Vertex<String> v3 = new Vertex<>("C");
        graph.addEdge(new Edge<>(v1, v2));
        graph.addEdge(new Edge<>(v1, v2));
        graph.addEdge(new Edge<>(v1, v3));
        List<Vertex<String>> neighbors = graph.getNeighbors(v1);
        List<Vertex<String>> expected = new ArrayList<>();
        expected.add(v2);
        expected.add(v2);
        expected.add(v3);
        assertEquals(expected, neighbors);
    }

    @Test
    public void testEquals() {
        AdjacencyListGraph<String> graph1 = new AdjacencyListGraph<>();
        AdjacencyListGraph<String> graph2 = new AdjacencyListGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Vertex<String> v3 = new Vertex<>("C");
        graph1.addEdge(new Edge<>(v1, v2));
        graph1.addEdge(new Edge<>(v1, v2));
        graph1.addEdge(new Edge<>(v1, v3));
        graph2.addEdge(new Edge<>(v1, v3));
        graph2.addEdge(new Edge<>(v1, v3));
        graph2.addEdge(new Edge<>(v1, v2));
        assertFalse(graph1.equals(graph2));

        AdjacencyListGraph<String> graph3 = new AdjacencyListGraph<>();
        AdjacencyListGraph<String> graph4 = new AdjacencyListGraph<>();
        graph3.addVertex(v1);
        graph3.addVertex(v2);
        graph3.addEdge(new Edge<>(v1, v2));
        graph4.addVertex(v2);
        graph4.addVertex(v1);
        graph4.addEdge(new Edge<>(v1, v2));
        assertTrue(graph3.equals(graph4));

    }

    @Test
    public void testHashCode() {
        AdjacencyListGraph<String> graph1 = new AdjacencyListGraph<>();
        AdjacencyListGraph<String> graph2 = new AdjacencyListGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        graph1.addVertex(v1);
        graph1.addVertex(v2);
        graph2.addVertex(v2);
        graph2.addVertex(v1);
        assertTrue(graph1.hashCode() == graph2.hashCode());
    }

    @Test
    public void testReadFromFile() throws IOException {
        File tempFile;
        tempFile = File.createTempFile("testGraph", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("A:[B, C] \n");
            writer.write("B:[] \n");
            writer.write("C:[] \n");
        }
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
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