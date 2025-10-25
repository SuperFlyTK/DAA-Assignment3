package com.mst.core;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private final String name;
    private final Map<String, Vertex> vertices;
    private final Set<Edge> edges;
    private final GraphType type;

    public enum GraphType {
        TRANSPORTATION_NETWORK,
        TEST_GRAPH,
        RANDOM_GENERATED
    }

    public Graph(String name, GraphType type) {
        this.name = name;
        this.type = type;
        this.vertices = new HashMap<>();
        this.edges = new HashSet<>();
    }

    public static Graph createTransportationNetwork(String cityName) {
        return new Graph(cityName, GraphType.TRANSPORTATION_NETWORK);
    }

    public Vertex addVertex(String id, String districtName) {
        Vertex vertex = new Vertex(id, districtName);
        vertices.put(id, vertex);
        return vertex;
    }

    public Vertex getVertex(String id) {
        return vertices.get(id);
    }

    public boolean containsVertex(String id) {
        return vertices.containsKey(id);
    }

    public Edge addEdge(String fromId, String toId, int weight, String roadName) {
        validateVerticesExist(fromId, toId);
        validateWeight(weight);

        Edge edge = new Edge(getVertex(fromId), getVertex(toId), weight, roadName);
        edges.add(edge);

        getVertex(fromId).addAdjacentEdge(edge);
        getVertex(toId).addAdjacentEdge(edge);

        return edge;
    }

    private void validateVerticesExist(String fromId, String toId) {
        if (!vertices.containsKey(fromId) || !vertices.containsKey(toId)) {
            throw new IllegalArgumentException("Both vertices must exist in graph: " + fromId + ", " + toId);
        }
    }

    private void validateWeight(int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Edge weight must be positive: " + weight);
        }
    }

    public boolean isConnected() {
        if (vertices.isEmpty()) return true;

        Set<Vertex> visited = new HashSet<>();
        dfs(vertices.values().iterator().next(), visited);

        return visited.size() == vertices.size();
    }

    private void dfs(Vertex current, Set<Vertex> visited) {
        visited.add(current);
        for (Edge edge : current.getAdjacentEdges()) {
            Vertex neighbor = edge.getOtherVertex(current);
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited);
            }
        }
    }

    public double getDensity() {
        int n = vertices.size();
        if (n <= 1) return 0.0;

        int maxPossibleEdges = n * (n - 1) / 2;
        return (double) edges.size() / maxPossibleEdges;
    }

    public List<Vertex> getVerticesList() {
        return new ArrayList<>(vertices.values());
    }

    public List<Edge> getEdgesList() {
        return new ArrayList<>(edges);
    }

    public String getName() { return name; }
    public GraphType getType() { return type; }
    public int getVertexCount() { return vertices.size(); }
    public int getEdgeCount() { return edges.size(); }
    public Set<String> getVertexIds() { return vertices.keySet(); }

    @Override
    public String toString() {
        return String.format("Graph{name='%s', vertices=%d, edges=%d, density=%.2f, type=%s}",
                name, vertices.size(), edges.size(), getDensity(), type);
    }

    public static class Builder {
        private final Graph graph;

        public Builder(String name, GraphType type) {
            this.graph = new Graph(name, type);
        }

        public Builder addVertex(String id, String districtName) {
            graph.addVertex(id, districtName);
            return this;
        }

        public Builder addEdge(String fromId, String toId, int weight, String roadName) {
            graph.addEdge(fromId, toId, weight, roadName);
            return this;
        }

        public Graph build() {
            return graph;
        }
    }
}