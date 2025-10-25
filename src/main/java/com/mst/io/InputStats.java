package com.mst.io;

public class InputStats {
    private int vertices;
    private int edges;

    public InputStats() {}

    public InputStats(int vertices, int edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public int getVertices() { return vertices; }
    public void setVertices(int vertices) { this.vertices = vertices; }

    public int getEdges() { return edges; }
    public void setEdges(int edges) { this.edges = edges; }
}