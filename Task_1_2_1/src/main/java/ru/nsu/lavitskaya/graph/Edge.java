package ru.nsu.lavitskaya.graph;

public class Edge<T> {
    private final Vertex<T> from;
    private final Vertex<T> to;

    public Edge(Vertex<T> from, Vertex<T> to) {
        this.from = from;
        this.to = to;
    }

    public Vertex<T> getFrom() {
        return from;
    }

    public Vertex<T> getTo() {
        return to;
    }

    @Override
    public String toString() {
        return from + "->" + to;
    }
}
