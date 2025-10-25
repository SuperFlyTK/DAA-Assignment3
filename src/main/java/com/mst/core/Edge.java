package com.mst.core;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private final Vertex from;
    private final Vertex to;
    private final int weight;
    private final String roadName;
    private final boolean bidirectional;

    public Edge(Vertex from, Vertex to, int weight, String roadName) {
        this(from, to, weight, roadName, true);
    }

    public Edge(Vertex from, Vertex to, int weight, String roadName, boolean bidirectional) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Edge vertices cannot be null");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("Edge cannot connect a vertex to itself");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Edge weight must be positive: " + weight);
        }
        if (roadName == null || roadName.trim().isEmpty()) {
            throw new IllegalArgumentException("Road name cannot be null or empty");
        }

        this.from = from;
        this.to = to;
        this.weight = weight;
        this.roadName = roadName;
        this.bidirectional = bidirectional;
    }

    public Vertex getFrom() { return from; }
    public Vertex getTo() { return to; }
    public int getWeight() { return weight; }
    public String getRoadName() { return roadName; }
    public boolean isBidirectional() { return bidirectional; }

    public Vertex getOtherVertex(Vertex vertex) {
        if (vertex.equals(from)) return to;
        if (vertex.equals(to)) return from;
        throw new IllegalArgumentException("Vertex '" + vertex.getId() + "' is not connected to this edge");
    }

    public boolean connects(Vertex v1, Vertex v2) {
        if (v1 == null || v2 == null) return false;

        if (bidirectional) {
            return (from.equals(v1) && to.equals(v2)) ||
                    (from.equals(v2) && to.equals(v1));
        } else {
            return from.equals(v1) && to.equals(v2);
        }
    }

    public boolean isIncidentTo(Vertex vertex) {
        return from.equals(vertex) || to.equals(vertex);
    }

    @Override
    public int compareTo(Edge other) {
        int weightCompare = Integer.compare(this.weight, other.weight);
        if (weightCompare != 0) return weightCompare;

        int fromCompare = this.from.getId().compareTo(other.from.getId());
        if (fromCompare != 0) return fromCompare;

        return this.to.getId().compareTo(other.to.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;

        if (weight != edge.weight) return false;

        if (bidirectional && edge.bidirectional) {
            return (from.equals(edge.from) && to.equals(edge.to)) ||
                    (from.equals(edge.to) && to.equals(edge.from));
        }

        return from.equals(edge.from) && to.equals(edge.to) && bidirectional == edge.bidirectional;
    }

    @Override
    public int hashCode() {
        if (bidirectional) {
            // For bidirectional edges, order of vertices doesn't matter
            int hash1 = Objects.hash(from, to, weight);
            int hash2 = Objects.hash(to, from, weight);
            return hash1 + hash2;
        }
        return Objects.hash(from, to, weight, bidirectional);
    }

    @Override
    public String toString() {
        String direction = bidirectional ? "⟷" : "→";
        return String.format("Edge{%s %s %s: %d (%s)}",
                from.getId(), direction, to.getId(), weight, roadName);
    }

    public String toDetailedString() {
        return String.format("Edge{from=%s, to=%s, weight=%d, road='%s', bidirectional=%s}",
                from.getId(), to.getId(), weight, roadName, bidirectional);
    }
}