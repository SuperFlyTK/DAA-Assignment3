package com.mst.core;

import java.util.*;
import java.util.stream.Collectors;

public class Vertex {
    private final String id;
    private final String districtName;
    private final Set<Edge> adjacentEdges;
    private final Map<String, Object> properties;

    public Vertex(String id, String districtName) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vertex ID cannot be null or empty");
        }
        if (districtName == null || districtName.trim().isEmpty()) {
            throw new IllegalArgumentException("District name cannot be null or empty");
        }

        this.id = id;
        this.districtName = districtName;
        this.adjacentEdges = new HashSet<>();
        this.properties = new HashMap<>();
    }

    public void addAdjacentEdge(Edge edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge cannot be null");
        }
        if (!edge.isIncidentTo(this)) {
            throw new IllegalArgumentException("Edge does not connect to this vertex");
        }
        adjacentEdges.add(edge);
    }

    public Set<Edge> getAdjacentEdges() {
        return Collections.unmodifiableSet(adjacentEdges);
    }

    public List<Vertex> getNeighbors() {
        return adjacentEdges.stream()
                .map(edge -> edge.getOtherVertex(this))
                .distinct()
                .collect(Collectors.toList());
    }

    public int getDegree() {
        return adjacentEdges.size();
    }

    public boolean isConnectedTo(Vertex other) {
        return adjacentEdges.stream()
                .anyMatch(edge -> edge.connects(this, other));
    }

    public void setProperty(String key, Object value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Property key cannot be null or empty");
        }
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    public String getId() { return id; }
    public String getDistrictName() { return districtName; }
    public Map<String, Object> getProperties() { return Collections.unmodifiableMap(properties); }

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
        return String.format("Vertex{id='%s', district='%s', degree=%d, properties=%s}",
                id, districtName, getDegree(), properties.keySet());
    }
}