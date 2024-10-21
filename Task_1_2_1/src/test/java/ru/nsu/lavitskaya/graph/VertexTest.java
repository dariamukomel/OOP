package ru.nsu.lavitskaya.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Vertex class.
 *
 * <p>This class tests the functionality of the Vertex class, including creation,
 * equality comparisons, hash code generation, and string representation.</p>
 */
class VertexTest {
    @Test
    public void testVertexCreation() {
        Vertex<String> vertex = new Vertex<>("A");
        assertEquals("A", vertex.getValue());
    }

    @Test
    public void testEqualsSameVertex() {
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("A");

        assertEquals(v1, v2);
    }

    @Test
    public void testEqualsDifferentVertex() {
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");

        assertNotEquals(v1, v2);
    }

    @Test
    public void testEqualsNull() {
        Vertex<String> vertex = new Vertex<>("A");
        assertNotEquals(vertex, null);
    }

    @Test
    public void testEqualsDifferentClass() {
        Vertex<String> vertex = new Vertex<>("A");
        assertNotEquals(vertex, "not a vertex");
    }

    @Test
    public void testHashCode() {
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("A");

        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testToString() {
        Vertex<String> vertex = new Vertex<>("A");
        assertEquals("A", vertex.toString());
    }
}