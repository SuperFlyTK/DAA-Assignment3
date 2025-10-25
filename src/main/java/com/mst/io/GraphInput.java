package com.mst.io;

import java.util.List;

public class GraphInput {
    private int id;
    private List<String> nodes;
    private List<EdgeInput> edges;

    public GraphInput() {}

    public GraphInput(int id, List<String> nodes, List<EdgeInput> edges) {
        this.id = id;
        this.nodes = nodes;
        this.edges = edges;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<String> getNodes() { return nodes; }
    public void setNodes(List<String> nodes) { this.nodes = nodes; }

    public List<EdgeInput> getEdges() { return edges; }
    public void setEdges(List<EdgeInput> edges) { this.edges = edges; }
}