package com.mst.generator;

import com.mst.io.GraphInput;
import com.mst.io.EdgeInput;
import com.mst.io.InputData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

public class GraphDatasetGenerator {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Random random = new Random(42);

    public static void main(String[] args) throws Exception {
        System.out.println("🚀 Generating MST test datasets...");

        generateSmallDataset();
        generateMediumDataset();
        generateLargeDataset();
        generateExtraLargeDataset();

        System.out.println("✅ All datasets generated successfully!");
    }

    private static void generateSmallDataset() throws Exception {
        List<GraphInput> graphs = new ArrayList<>();

        int[] smallSizes = {8, 15, 22, 25, 28};
        for (int i = 0; i < 5; i++) {
            graphs.add(createGraph(i + 1, smallSizes[i], 0.4, 0.7));
        }

        saveDataset(graphs, "small_dataset.json");
        System.out.println("✓ Generated 5 small graphs (<30 vertices)");
    }

    private static void generateMediumDataset() throws Exception {
        List<GraphInput> graphs = new ArrayList<>();

        int[] mediumSizes = {50, 80, 120, 150, 180, 210, 240, 270, 290, 295};
        for (int i = 0; i < 10; i++) {
            graphs.add(createGraph(i + 1, mediumSizes[i], 0.25, 0.5));
        }

        saveDataset(graphs, "medium_dataset.json");
        System.out.println("✓ Generated 10 medium graphs (<300 vertices)");
    }

    private static void generateLargeDataset() throws Exception {
        List<GraphInput> graphs = new ArrayList<>();

        int[] largeSizes = {300, 400, 500, 600, 700, 800, 850, 900, 950, 980};
        for (int i = 0; i < 10; i++) {
            graphs.add(createGraph(i + 1, largeSizes[i], 0.15, 0.35));
        }

        saveDataset(graphs, "large_dataset.json");
        System.out.println("✓ Generated 10 large graphs (<1000 vertices)");
    }

    private static void generateExtraLargeDataset() throws Exception {
        List<GraphInput> graphs = new ArrayList<>();

        int[] extraSizes = {1000, 1500, 2000, 2500, 2800};
        for (int i = 0; i < 5; i++) {
            graphs.add(createGraph(i + 1, extraSizes[i], 0.1, 0.25));
        }

        saveDataset(graphs, "extra_large_dataset.json");
        System.out.println("✓ Generated 5 extra large graphs (<3000 vertices)");
    }

    private static GraphInput createGraph(int id, int vertexCount, double minDensity, double maxDensity) {
        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < vertexCount; i++) {
            nodes.add("N" + i);
        }

        double density = minDensity + (random.nextDouble() * (maxDensity - minDensity));
        List<EdgeInput> edges = generateEdges(nodes, density);

        return new GraphInput(id, nodes, edges);
    }

    private static List<EdgeInput> generateEdges(List<String> nodes, double density) {
        List<EdgeInput> edges = new ArrayList<>();
        Set<String> usedEdges = new HashSet<>();
        int vertexCount = nodes.size();

        // First ensure connectivity
        List<Integer> connected = new ArrayList<>();
        connected.add(0);
        List<Integer> unconnected = new ArrayList<>();
        for (int i = 1; i < vertexCount; i++) {
            unconnected.add(i);
        }

        while (!unconnected.isEmpty()) {
            int fromIdx = connected.get(random.nextInt(connected.size()));
            int toIdx = unconnected.remove(random.nextInt(unconnected.size()));

            String from = nodes.get(fromIdx);
            String to = nodes.get(toIdx);
            int weight = 1 + random.nextInt(100);

            edges.add(new EdgeInput(from, to, weight));
            usedEdges.add(getEdgeKey(fromIdx, toIdx));

            connected.add(toIdx);
        }

        // Add additional edges to reach desired density
        int maxPossibleEdges = vertexCount * (vertexCount - 1) / 2;
        int targetEdges = (int) (maxPossibleEdges * density);
        targetEdges = Math.min(targetEdges, maxPossibleEdges);

        while (edges.size() < targetEdges) {
            int fromIdx = random.nextInt(vertexCount);
            int toIdx = random.nextInt(vertexCount);

            if (fromIdx != toIdx) {
                String edgeKey = getEdgeKey(fromIdx, toIdx);
                if (!usedEdges.contains(edgeKey)) {
                    String from = nodes.get(fromIdx);
                    String to = nodes.get(toIdx);
                    int weight = 1 + random.nextInt(100);

                    edges.add(new EdgeInput(from, to, weight));
                    usedEdges.add(edgeKey);
                }
            }
        }

        return edges;
    }

    private static String getEdgeKey(int from, int to) {
        return Math.min(from, to) + "-" + Math.max(from, to);
    }

    private static void saveDataset(List<GraphInput> graphs, String filename) throws Exception {
        InputData data = new InputData(graphs);
        File outputDir = new File("datasets");
        if (!outputDir.exists()) outputDir.mkdirs();

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(outputDir, filename), data);
    }
}