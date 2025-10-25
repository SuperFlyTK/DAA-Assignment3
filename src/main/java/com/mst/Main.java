package com.mst;

import com.mst.io.*;
import com.mst.core.Graph;
import com.mst.core.MSTResult;
import com.mst.algorithms.PrimMST;
import com.mst.algorithms.KruskalMST;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Main {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void main(String[] args) {
        System.out.println("🚀 MST Algorithms - City Transportation Network Optimization");
        System.out.println("============================================================\n");

        String[] inputFiles = {
                "datasets/ass_3_input.json",
                "datasets/small_dataset.json",
                "datasets/medium_dataset.json",
                "datasets/large_dataset.json",
                "datasets/extra_large_dataset.json"
        };

        List<PerformanceRecord> allRecords = new ArrayList<>();

        for (String inputFile : inputFiles) {
            try {
                System.out.println("\n" + "=".repeat(60));
                System.out.println("Processing: " + inputFile);
                System.out.println("=".repeat(60));

                List<PerformanceRecord> records = processFile(inputFile);
                allRecords.addAll(records);

            } catch (Exception e) {
                System.out.println("❌ Error processing " + inputFile + ": " + e.getMessage());
            }
        }

        try {
            generatePerformanceReport(allRecords);
            System.out.println("\n✅ All datasets processed successfully!");
            System.out.println("📊 Performance report: analysis/performance_comparison.csv");
        } catch (Exception e) {
            System.err.println("❌ Error generating report: " + e.getMessage());
        }
    }

    public static List<PerformanceRecord> processFile(String inputFile) throws Exception {
        InputData inputData = JsonUtils.readInput(inputFile);
        List<Graph> graphs = JsonUtils.convertToGraphs(inputData);

        System.out.println("📁 Loaded " + graphs.size() + " graphs");

        List<GraphResult> results = new ArrayList<>();
        List<PerformanceRecord> records = new ArrayList<>();
        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        for (Graph graph : graphs) {
            int vertices = graph.getVertexCount();
            int edges = graph.getEdgeCount();
            int maxPossibleEdges = vertices * (vertices - 1) / 2;
            double density = maxPossibleEdges > 0 ? (edges * 100.0) / maxPossibleEdges : 0;

            System.out.printf("\n--- Graph %d: %d vertices, %d edges (Density: %.1f%%) ---%n",
                    graph.getId(), vertices, edges, density);

            MSTResult primResult = prim.findMST(graph);
            MSTResult kruskalResult = kruskal.findMST(graph);

            if (primResult.getTotalCost() != kruskalResult.getTotalCost()) {
                System.err.println("❌ COST MISMATCH! Prim: " + primResult.getTotalCost() +
                        ", Kruskal: " + kruskalResult.getTotalCost());
            } else {
                System.out.println("✅ Algorithms agree on MST cost: " + primResult.getTotalCost());
            }

            System.out.printf("Prim:    cost=%-6d time=%-8.3fms ops=%-8d%n",
                    primResult.getTotalCost(),
                    primResult.getExecutionTime() / 1_000_000.0,
                    primResult.getOperationsCount());

            System.out.printf("Kruskal: cost=%-6d time=%-8.3fms ops=%-8d%n",
                    kruskalResult.getTotalCost(),
                    kruskalResult.getExecutionTime() / 1_000_000.0,
                    kruskalResult.getOperationsCount());

            double timeRatio = kruskalResult.getExecutionTime() > 0 ?
                    (double) primResult.getExecutionTime() / kruskalResult.getExecutionTime() : 0;
            System.out.printf("Time Ratio (Prim/Kruskal): %.3f%n", timeRatio);

            InputStats stats = new InputStats(vertices, edges);
            MSTOutput primOutput = convertToMSTOutput(primResult);
            MSTOutput kruskalOutput = convertToMSTOutput(kruskalResult);

            results.add(new GraphResult(graph.getId(), stats, primOutput, kruskalOutput));

            records.add(new PerformanceRecord(
                    inputFile.replace("datasets/", "").replace(".json", ""),
                    graph.getId(),
                    vertices,
                    edges,
                    density,
                    primResult.getTotalCost(),
                    kruskalResult.getTotalCost(),
                    primResult.getExecutionTime(),
                    kruskalResult.getExecutionTime(),
                    primResult.getOperationsCount(),
                    kruskalResult.getOperationsCount()
            ));
        }

        String outputFile = inputFile.replace("datasets/", "output/").replace(".json", "_output.json");
        File outputDir = new File("output");
        if (!outputDir.exists()) outputDir.mkdirs();

        OutputData outputData = new OutputData(results);
        JsonUtils.writeOutput(outputFile, outputData);

        System.out.println("💾 Results written to: " + outputFile);
        return records;
    }

    private static MSTOutput convertToMSTOutput(MSTResult result) {
        List<EdgeOutput> edgeOutputs = new ArrayList<>();
        for (com.mst.core.Edge edge : result.getMstEdges()) {
            edgeOutputs.add(new EdgeOutput(edge.getFrom(), edge.getTo(), edge.getWeight()));
        }
        return new MSTOutput(edgeOutputs, result.getTotalCost(),
                result.getOperationsCount(), result.getExecutionTime() / 1_000_000.0);
    }

    public static void generatePerformanceReport(List<PerformanceRecord> records) throws Exception {
        File analysisDir = new File("analysis");
        if (!analysisDir.exists()) analysisDir.mkdirs();

        FileWriter writer = new FileWriter("analysis/performance_comparison.csv");
        writer.write("Dataset,GraphID,Vertices,Edges,Density%,Prim_Cost,Kruskal_Cost,Prim_Time_ms,Kruskal_Time_ms,Prim_Operations,Kruskal_Operations,Time_Ratio,Operations_Ratio\n");

        for (PerformanceRecord record : records) {
            double timeRatio = record.kruskalTime > 0 ? (double) record.primTime / record.kruskalTime : 0;
            double opsRatio = record.kruskalOperations > 0 ? (double) record.primOperations / record.kruskalOperations : 0;

            writer.write(String.format("%s,%d,%d,%d,%.1f,%d,%d,%.3f,%.3f,%d,%d,%.3f,%.3f\n",
                    record.dataset, record.graphId, record.vertices, record.edges, record.density,
                    record.primCost, record.kruskalCost,
                    record.primTime / 1_000_000.0, record.kruskalTime / 1_000_000.0,
                    record.primOperations, record.kruskalOperations, timeRatio, opsRatio));
        }

        writer.close();
        System.out.println("📈 Performance report saved to: analysis/performance_comparison.csv");

        printSummaryStatistics(records);
    }

    public static void printSummaryStatistics(List<PerformanceRecord> records) {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("📊 PERFORMANCE SUMMARY BY DATASET");
        System.out.println("=".repeat(100));

        Map<String, List<PerformanceRecord>> byDataset = new HashMap<>();
        for (PerformanceRecord record : records) {
            byDataset.computeIfAbsent(record.dataset, k -> new ArrayList<>()).add(record);
        }

        for (Map.Entry<String, List<PerformanceRecord>> entry : byDataset.entrySet()) {
            String dataset = entry.getKey();
            List<PerformanceRecord> datasetRecords = entry.getValue();

            System.out.printf("\n%s (%d graphs):%n", dataset.toUpperCase(), datasetRecords.size());
            System.out.println("-".repeat(60));

            double avgPrimTime = datasetRecords.stream().mapToLong(r -> r.primTime).average().orElse(0) / 1_000_000.0;
            double avgKruskalTime = datasetRecords.stream().mapToLong(r -> r.kruskalTime).average().orElse(0) / 1_000_000.0;
            double avgPrimOps = datasetRecords.stream().mapToInt(r -> r.primOperations).average().orElse(0);
            double avgKruskalOps = datasetRecords.stream().mapToInt(r -> r.kruskalOperations).average().orElse(0);

            System.out.printf("Prim's Algorithm:     Avg Time: %.3f ms, Avg Operations: %.0f%n",
                    avgPrimTime, avgPrimOps);
            System.out.printf("Kruskal's Algorithm:  Avg Time: %.3f ms, Avg Operations: %.0f%n",
                    avgKruskalTime, avgKruskalOps);
            System.out.printf("Performance Ratio:    Time: %.3f (Prim/Kruskal), Operations: %.3f (Prim/Kruskal)%n",
                    avgPrimTime / avgKruskalTime, avgPrimOps / avgKruskalOps);
        }
    }

    static class PerformanceRecord {
        String dataset;
        int graphId;
        int vertices;
        int edges;
        double density;
        int primCost;
        int kruskalCost;
        long primTime;
        long kruskalTime;
        int primOperations;
        int kruskalOperations;

        public PerformanceRecord(String dataset, int graphId, int vertices, int edges, double density,
                                 int primCost, int kruskalCost, long primTime, long kruskalTime,
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