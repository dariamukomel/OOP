package ru.nsu.lavitskaya.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Edge class.
 *
 * <p>This class tests various functionalities such as creating edges, comparing
 * edges for equality, generating hash codes, and obtaining string representations of edges.</p>
 */
class EdgeTest {
    @Test
    public void testEdgeCreation() {
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Edge<String> edge = new Edge<>(v1, v2);

        assertEquals(v1, edge.getFrom());
        assertEquals(v2, edge.getTo());
    }

    @Test
    public void testEqualsSameEdge() {
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Edge<String> edge1 = new Edge<>(v1, v2);
        Edge<String> edge2 = new Edge<>(v1, v2);

        assertEquals(edge1, edge2);
    }

    @Test
    public void testEqualsDifferentEdges() {
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Vertex<String> v3 = new Vertex<>("C");
        Edge<String> edge1 = new Edge<>(v1, v2);
        Edge<String> edge2 = new Edge<>(v1, v3);

        assertNotEquals(edge1, edge2);
    }

    @Test
    public void testEqualsNull() {
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Edge<String> edge = new Edge<>(v1, v2);

        assertNotEquals(edge, null);
    }

    @Test
    public void testEqualsDifferentClass() {
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Edge<String> edge = new Edge<>(v1, v2);

        assertNotEquals(edge, "not an edge");
    }

    @Test
    public void testHashCode() {
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Edge<String> edge1 = new Edge<>(v1, v2);
        Edge<String> edge2 = new Edge<>(v1, v2);

        assertEquals(edge1.hashCode(), edge2.hashCode());
    }

    @Test
    public void testToString() {
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Edge<String> edge = new Edge<>(v1, v2);

        assertEquals("A->B", edge.toString());
    }
}