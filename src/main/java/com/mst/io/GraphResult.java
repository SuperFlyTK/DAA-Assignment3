package com.mst.io;

public class GraphResult {
    private int graph_id;
    private InputStats input_stats;
    private MSTOutput prim;
    private MSTOutput kruskal;

    public GraphResult(int graph_id, InputStats input_stats, MSTOutput prim, MSTOutput kruskal) {
        this.graph_id = graph_id;
        this.input_stats = input_stats;
        this.prim = prim;
        this.kruskal = kruskal;
    }

    public int getGraph_id() { return graph_id; }
    public void setGraph_id(int graph_id) { this.graph_id = graph_id; }

    public InputStats getInput_stats() { return input_stats; }
    public void setInput_stats(InputStats input_stats) { this.input_stats = input_stats; }

    public MSTOutput getPrim() { return prim; }
    public void setPrim(MSTOutput prim) { this.prim = prim; }

    public MSTOutput getKruskal() { return kruskal; }
    public void setKruskal(MSTOutput kruskal) { this.kruskal = kruskal; }
}