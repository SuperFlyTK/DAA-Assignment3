package com.mst.analysis;

import com.mst.algorithms.PrimMST;
import com.mst.algorithms.KruskalMST;
import com.mst.core.Graph;
import com.mst.core.MSTResult;
import com.mst.io.InputData;
import com.mst.io.JsonUtils;

import java.io.FileWriter;
import java.util.*;

public class PerformanceAnalyzer {

    public static void main(String[] args) throws Exception {
        System.out.println("🔍 Starting MST Performance Analysis...");

        List<PerformanceRecord> allRecords = new ArrayList<>();
        String[] datasets = {
                "datasets/small_dataset.json",
                "datasets/medium_dataset.json",
                "datasets/large_dataset.json",
                "datasets/extra_large_dataset.json"
        };

        for (String dataset : datasets) {
            System.out.println("\n📊 Analyzing: " + dataset);
            List<PerformanceRecord> records = analyzeDataset(dataset);
            allRecords.addAll(records);
        }

        generatePerformanceReport(allRecords);
        generateSummaryStatistics(allRecords);

        System.out.println("\n✅ Performance analysis completed!");
        System.out.println("📁 Reports saved to: analysis/");
    }

    private static List<PerformanceRecord> analyzeDataset(String datasetFile) throws Exception {
        InputData inputData = JsonUtils.readInput(datasetFile);
        List<com.mst.core.Graph> graphs = JsonUtils.convertToGraphs(inputData);
        List<PerformanceRecord> records = new ArrayList<>();

        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        for (com.mst.core.Graph graph : graphs) {
            MSTResult primResult = prim.findMST(graph);
            MSTResult kruskalResult = kruskal.findMST(graph);


            if (primResult.getTotalCost() != kruskalResult.getTotalCost()) {
                System.err.println("⚠️  Cost mismatch in graph " + graph.getId());
            }

            double density = calculateDensity(graph);
            String datasetName = datasetFile.replace("datasets/", "").replace("_dataset.json", "");


            double primTimeMs = primResult.getExecutionTime() / 1_000_000.0;
            double kruskalTimeMs = kruskalResult.getExecutionTime() / 1_000_000.0;

            records.add(new PerformanceRecord(
                    datasetName,
                    graph.getId(),
                    graph.getVertexCount(),
                    graph.getEdgeCount(),
                    density,
                    primResult.getTotalCost(),
                    kruskalResult.getTotalCost(),
                    primTimeMs,
                    kruskalTimeMs,
                    primResult.getOperationsCount(),
                    kruskalResult.getOperationsCount()
            ));
        }

        return records;
    }

    private static double calculateDensity(com.mst.core.Graph graph) {
        int vertexCount = graph.getVertexCount();
        if (vertexCount <= 1) return 0.0;

        int maxPossibleEdges = vertexCount * (vertexCount - 1) / 2;
        return (double) graph.getEdgeCount() / maxPossibleEdges * 100;
    }

    private static void generatePerformanceReport(List<PerformanceRecord> records) throws Exception {
        java.io.File analysisDir = new java.io.File("analysis");
        if (!analysisDir.exists()) {
            analysisDir.mkdirs();
        }

        FileWriter writer = new FileWriter("analysis/performance_comparison.csv");

        writer.write("Dataset,GraphID,Vertices,Edges,Density%,Prim_Cost,Kruskal_Cost,Prim_Time_ms,Kruskal_Time_ms,Prim_Operations,Kruskal_Operations\n");

        for (PerformanceRecord record : records) {
            writer.write(String.format("%s,%d,%d,%d,%.2f,%d,%d,%.3f,%.3f,%d,%d\n",
                    record.dataset, record.graphId, record.vertices, record.edges, record.density,
                    record.primCost, record.kruskalCost, record.primTime, record.kruskalTime,
                    record.primOperations, record.kruskalOperations));
        }

        writer.close();
        System.out.println("✓ Generated performance_comparison.csv");
    }

    private static void generateSummaryStatistics(List<PerformanceRecord> records) throws Exception {
        Map<String, List<PerformanceRecord>> byDataset = new HashMap<>();

        for (PerformanceRecord record : records) {
            byDataset.computeIfAbsent(record.dataset, k -> new ArrayList<>()).add(record);
        }

        FileWriter writer = new FileWriter("analysis/summary_statistics.txt");
        writer.write("MST ALGORITHMS PERFORMANCE SUMMARY\n");
        writer.write("===================================\n\n");

        for (Map.Entry<String, List<PerformanceRecord>> entry : byDataset.entrySet()) {
            String dataset = entry.getKey();
            List<PerformanceRecord> datasetRecords = entry.getValue();

            writer.write(String.format("%s DATASET (%d graphs):\n", dataset.toUpperCase(), datasetRecords.size()));
            writer.write("------------------------------------------------\n");


            double avgPrimTime = datasetRecords.stream().mapToDouble(r -> r.primTime).average().orElse(0);
            double avgKruskalTime = datasetRecords.stream().mapToDouble(r -> r.kruskalTime).average().orElse(0);
            double avgPrimOps = datasetRecords.stream().mapToInt(r -> r.primOperations).average().orElse(0);
            double avgKruskalOps = datasetRecords.stream().mapToInt(r -> r.kruskalOperations).average().orElse(0);

            writer.write(String.format("Prim's Algorithm:     Avg Time: %.3f ms, Avg Operations: %.0f\n",
                    avgPrimTime, avgPrimOps));
            writer.write(String.format("Kruskal's Algorithm:  Avg Time: %.3f ms, Avg Operations: %.0f\n",
                    avgKruskalTime, avgKruskalOps));

            double timeRatio = avgKruskalTime > 0 ? avgPrimTime / avgKruskalTime : 0;
            double opsRatio = avgKruskalOps > 0 ? avgPrimOps / avgKruskalOps : 0;

            writer.write(String.format("Performance Ratio:    Time: %.3f (Prim/Kruskal), Operations: %.3f (Prim/Kruskal)\n\n",
                    timeRatio, opsRatio));
        }

        writer.close();
        System.out.println("✓ Generated summary_statistics.txt");
    }

    static class PerformanceRecord {
        final String dataset;
        final int graphId;
        final int vertices;
        final int edges;
        final double density;
        final int primCost;
        final int kruskalCost;
        final double primTime;
        final double kruskalTime;
        final int primOperations;
        final int kruskalOperations;

        PerformanceRecord(String dataset, int graphId, int vertices, int edges, double density,
                          int primCost, int kruskalCost, double primTime, double kruskalTime,
                          int primOperations, int kruskalOperations) {
            this.dataset = dataset;
            this.graphId = graphId;
            this.vertices = vertices;
            this.edges = edges;
            this.density = density;
            this.primCost = primCost;
            this.kruskalCost = kruskalCost;
            this.primTime = primTime;
            this.kruskalTime = kruskalTime;
            this.primOperations = primOperations;
            this.kruskalOperations = kruskalOperations;
        }
    }
}