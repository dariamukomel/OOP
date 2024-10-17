package ru.nsu.lavitskaya.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides a method for performing topological sorting on a directed acyclic graph.
 */
public class TopologicalSort {

    /**
     * Performs a topological sort on the given graph.
     *
     * @param graph The directed graph to be sorted.
     * @param <T>   The type of the vertices in the graph.
     * @return A list of vertices in topologically sorted order.
     * @throws IllegalStateException If the graph is cyclic.
     */
    public static <T> List<Vertex<T>> topSort(Graph<T> graph) throws IllegalStateException {
        List<Vertex<T>> sortedList = new ArrayList<>();
        Set<Vertex<T>> visited = new HashSet<>();
        Set<Vertex<T>> visiting = new HashSet<>();

        for (Vertex<T> vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                if (dfs(vertex, visited, visiting, sortedList, graph)) {
                    throw new IllegalStateException("Graph is cyclic");
                }
            }
        }

        return sortedList;
    }

    private static <T> boolean dfs(Vertex<T> vertex, Set<Vertex<T>> visited,
                                   Set<Vertex<T>> visiting, List<Vertex<T>> sortedList,
                                   Graph<T> graph) {
        visiting.add(vertex);

        List<Vertex<T>> neighbors = graph.getNeighbors(vertex);
        for (Vertex<T> neighbor : neighbors) {
            if (visiting.contains(neighbor)) {
                return true;
            }
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, visited, visiting, sortedList, graph)) {
                    return true;
                }
            }
        }

        visiting.remove(vertex);
        visited.add(vertex);
        sortedList.addFirst(vertex);
        return false;
    }

}
