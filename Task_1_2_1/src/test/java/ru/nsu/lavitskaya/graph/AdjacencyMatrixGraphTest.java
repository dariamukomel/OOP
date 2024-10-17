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
 * Unit tests for the AdjacencyMatrixGraph class.
 *
 * <p>This class contains various test cases to ensure the correct functionality
 * of the AdjacencyMatrixGraph's methods for adding/removing vertices and edges,
 * retrieving neighbors, checking equality, generating hash codes, and reading from files.</p>
 */
class AdjacencyMatrixGraphTest {
    @Test
    public void testAddAndRemoveVertex() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        String expectedOutput = "  A B \n" + "A 0 0 \n" + "B 0 0 \n";

        graph.addVertex(v1);
        graph.addVertex(v2);
        assertEquals(expectedOutput, graph.toString(), "just add vertices");

        graph.addVertex(v1);
        assertEquals(expectedOutput, graph.toString(), "add already existed vertex");

        graph.removeVertex(new Vertex<>("C"));
        assertEquals(expectedOutput, graph.toString(), "remove vertex that doesn't exist");

        graph.removeVertex(v1);
        assertEquals("  B \n" + "B 0 \n", graph.toString(), "just remove vertex");
    }

    @Test
    public void testAddAndRemoveEdges() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Vertex<String> v3 = new Vertex<>("C");

        graph.addEdge(new Edge<>(v1, v2));
        assertEquals("  A B \n" + "A 0 1 \n" + "B 0 0 \n", graph.toString(),
                "add edge when its both vertices are not in the graph");

        graph.addEdge(new Edge<>(v1, v3));
        assertEquals("  A B C \n" + "A 0 1 1 \n" + "B 0 0 0 \n" + "C 0 0 0 \n",
                graph.toString(), "add edge when its one vertices is not in the graph");

        graph.addEdge(new Edge<>(v1, v3));
        assertEquals("  A B C \n" + "A 0 1 2 \n" + "B 0 0 0 \n" + "C 0 0 0 \n",
                graph.toString(), "add multi edge");

        graph.removeEdge(new Edge<>(v1, v3));
        assertEquals("  A B C \n" + "A 0 1 1 \n" + "B 0 0 0 \n" + "C 0 0 0 \n",
                graph.toString(), "just remove edge");

        graph.removeEdge(new Edge<>(v2, v3));
        assertEquals("  A B C \n" + "A 0 1 1 \n" + "B 0 0 0 \n" + "C 0 0 0 \n",
                graph.toString(), "remove not existed edge");

    }

    @Test
    public void testGetNeighbors() {
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
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
        AdjacencyMatrixGraph<String> graph1 = new AdjacencyMatrixGraph<>();
        AdjacencyMatrixGraph<String> graph2 = new AdjacencyMatrixGraph<>();
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

        AdjacencyMatrixGraph<String> graph3 = new AdjacencyMatrixGraph<>();
        AdjacencyMatrixGraph<String> graph4 = new AdjacencyMatrixGraph<>();
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
        AdjacencyMatrixGraph<String> graph1 = new AdjacencyMatrixGraph<>();
        AdjacencyMatrixGraph<String> graph2 = new AdjacencyMatrixGraph<>();
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
            writer.write("  A B C\n");
            writer.write("A 0 1 1\n");
            writer.write("B 1 0 0\n");
            writer.write("C 1 0 0\n");
        }
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>();
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
