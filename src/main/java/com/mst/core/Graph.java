package com.mst.core;

import java.util.*;

public class Graph {
    private final int id;
    private final List<String> vertices;
    private final List<Edge> edges;

    public Graph(int id, List<String> vertices, List<Edge> edges) {
        this.id = id;
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>(edges);
    }

    public int getId() { return id; }
    public List<String> getVertices() { return Collections.unmodifiableList(vertices); }
    public List<Edge> getEdges() { return Collections.unmodifiableList(edges); }
    public int getVertexCount() { return vertices.size(); }
    public int getEdgeCount() { return edges.size(); }

    public Map<String, List<Edge>> getAdjacencyList() {
        Map<String, List<Edge>> adjList = new HashMap<>();

        for (String vertex : vertices) {
            adjList.put(vertex, new ArrayList<>());
        }

        for (Edge edge : edges) {
            adjList.get(edge.getFrom()).add(edge);
            adjList.get(edge.getTo()).add(
                    new Edge(edge.getTo(), edge.getFrom(), edge.getWeight())
            );
        }

        return adjList;
    }

    @Override
    public String toString() {
        return String.format("Graph{id=%d, vertices=%d, edges=%d}", id, vertices.size(), edges.size());
    }
}