package com.mst.core;

import java.util.*;

public class Vertex {
    private final String id;
    private final Set<Edge> adjacentEdges;

    public Vertex(String id) {
        this.id = id;
        this.adjacentEdges = new HashSet<>();
    }

    public String getId() { return id; }

    public void addAdjacentEdge(Edge edge) {
        adjacentEdges.add(edge);
    }

    public Set<Edge> getAdjacentEdges() {
        return Collections.unmodifiableSet(adjacentEdges);
    }

    public int getDegree() {
        return adjacentEdges.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(id, vertex.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Vertex{id='%s', degree=%d}", id, getDegree());
    }
}