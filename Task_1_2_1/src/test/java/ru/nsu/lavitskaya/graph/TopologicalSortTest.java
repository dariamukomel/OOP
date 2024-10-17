package ru.nsu.lavitskaya.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the TopologicalSort class.
 *
 * <p>This class tests the functionality of the topological sorting algorithm applied to graphs.</p>
 */
class TopologicalSortTest {
    @Test
    public void testAdjacencyMatrixGraphTopSort() {
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Vertex<String> v3 = new Vertex<>("C");
        graph.addEdge(new Edge<>(v1, v2));
        graph.addEdge(new Edge<>(v2, v3));
        graph.addEdge(new Edge<>(v3, v1));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            TopologicalSort.topSort(graph);
        });
        assertEquals("Graph is cyclic", exception.getMessage());

        graph.removeEdge(new Edge<>(v3, v1));
        graph.addEdge(new Edge<>(v2, v3));
        List<Vertex<String>> sortedGraph = TopologicalSort.topSort(graph);
        List<Vertex<String>> expectedGraph = new ArrayList<>();
        expectedGraph.add(v1);
        expectedGraph.add(v2);
        expectedGraph.add(v3);
        assertEquals(expectedGraph, sortedGraph);

    }
}