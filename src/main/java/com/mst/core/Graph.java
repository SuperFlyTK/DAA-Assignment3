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
        if (vertices.containsKey(id)) {
            throw new IllegalArgumentException("Vertex with id '" + id + "' already exists");
        }
        Vertex vertex = new Vertex(id, districtName);
        vertices.put(id, vertex);
        return vertex;
    }

    public Vertex getVertex(String id) {
        Vertex vertex = vertices.get(id);
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex not found: " + id);
        }
        return vertex;
    }

    public boolean containsVertex(String id) {
        return vertices.containsKey(id);
    }

    public Edge addEdge(String fromId, String toId, int weight, String roadName) {
        validateVerticesExist(fromId, toId);
        validateWeight(weight);

        Vertex from = getVertex(fromId);
        Vertex to = getVertex(toId);

        Edge edge = new Edge(from, to, weight, roadName);
        edges.add(edge);

        from.addAdjacentEdge(edge);
        to.addAdjacentEdge(edge);

        return edge;
    }

    private void validateVerticesExist(String fromId, String toId) {
        if (!vertices.containsKey(fromId)) {
            throw new IllegalArgumentException("Source vertex not found: " + fromId);
        }
        if (!vertices.containsKey(toId)) {
            throw new IllegalArgumentException("Target vertex not found: " + toId);
        }
    }

    private void validateWeight(int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Edge weight must be positive: " + weight);
        }
    }

    public boolean isConnected() {
        if (vertices.isEmpty()) return true;
        if (edges.isEmpty()) return vertices.size() <= 1;

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

    public Map<String, Object> getGraphStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("name", name);
        stats.put("type", type);
        stats.put("vertexCount", getVertexCount());
        stats.put("edgeCount", getEdgeCount());
        stats.put("density", getDensity());
        stats.put("connected", isConnected());

        // Degree statistics
        List<Integer> degrees = vertices.values().stream()
                .map(Vertex::getDegree)
                .collect(Collectors.toList());
        if (!degrees.isEmpty()) {
            stats.put("maxDegree", Collections.max(degrees));
            stats.put("minDegree", Collections.min(degrees));
            stats.put("avgDegree", degrees.stream().mapToInt(Integer::intValue).average().orElse(0));
        } else {
            stats.put("maxDegree", 0);
            stats.put("minDegree", 0);
            stats.put("avgDegree", 0.0);
        }

        return stats;
    }

    @Override
    public String toString() {
        return String.format("Graph{name='%s', vertices=%d, edges=%d, density=%.2f, type=%s, connected=%s}",
                name, vertices.size(), edges.size(), getDensity(), type, isConnected());
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