package com.mst.io;

import java.util.List;

public class InputData {
    private List<GraphInput> graphs;

    public InputData() {}

    public InputData(List<GraphInput> graphs) {
        this.graphs = graphs;
    }

    public List<GraphInput> getGraphs() { return graphs; }
    public void setGraphs(List<GraphInput> graphs) { this.graphs = graphs; }
}