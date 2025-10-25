package com.mst.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mst.core.Graph;
import com.mst.core.Edge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static InputData readInput(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), InputData.class);
    }

    public static List<Graph> convertToGraphs(InputData inputData) {
        List<Graph> graphs = new ArrayList<>();
        for (GraphInput graphInput : inputData.getGraphs()) {
            List<Edge> edges = new ArrayList<>();
            for (EdgeInput edgeInput : graphInput.getEdges()) {
                edges.add(new Edge(edgeInput.getFrom(), edgeInput.getTo(), edgeInput.getWeight()));
            }
            graphs.add(new Graph(graphInput.getId(), graphInput.getNodes(), edges));
        }
        return graphs;
    }

    public static void writeOutput(String filePath, OutputData outputData) throws IOException {
        objectMapper.writeValue(new File(filePath), outputData);
    }
}