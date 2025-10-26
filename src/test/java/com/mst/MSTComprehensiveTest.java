package com.mst;

import com.mst.algorithms.PrimMST;
import com.mst.algorithms.KruskalMST;
import com.mst.core.Graph;
import com.mst.core.MSTResult;
import com.mst.util.GraphValidator; // ✅ Правильный импорт
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MSTComprehensiveTest {

    @Test
    void testAlgorithmsProduceSameMSTCost() {
        Graph graph = TestGraphFactory.createSampleGraph();

        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        MSTResult primResult = prim.findMST(graph);
        MSTResult kruskalResult = kruskal.findMST(graph);

        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(),
                "Both algorithms should produce the same MST cost");
    }

    @Test
    void testMSTHasCorrectEdgeCount() {
        Graph graph = TestGraphFactory.createMediumGraph();

        PrimMST prim = new PrimMST();
        MSTResult result = prim.findMST(graph);

        assertEquals(graph.getVertexCount() - 1, result.getMstEdges().size(),
                "MST should have exactly V-1 edges");
    }

    @Test
    void testMSTIsAcyclic() {
        Graph graph = TestGraphFactory.createLargeGraph();

        PrimMST prim = new PrimMST();
        MSTResult result = prim.findMST(graph);

        assertTrue(GraphValidator.isAcyclic(result.getMstEdges()),
                "MST should be acyclic");
    }

    @Test
    void testMSTConnectsAllVertices() {
        Graph graph = TestGraphFactory.createConnectedGraph();

        PrimMST prim = new PrimMST();
        MSTResult result = prim.findMST(graph);

        assertTrue(GraphValidator.connectsAllVertices(result.getMstEdges(), graph.getVertices()),
                "MST should connect all vertices");
    }

    @Test
    void testPerformanceMetricsAreValid() {
        Graph graph = TestGraphFactory.createSampleGraph();

        PrimMST prim = new PrimMST();
        MSTResult result = prim.findMST(graph);

        assertTrue(result.getExecutionTime() >= 0, "Execution time should be non-negative");
        assertTrue(result.getOperationsCount() >= 0, "Operation count should be non-negative");
    }

    @Test
    void testSingleVertexGraph() {
        Graph singleNodeGraph = TestGraphFactory.createSingleNodeGraph();

        PrimMST prim = new PrimMST();
        MSTResult result = prim.findMST(singleNodeGraph);

        assertEquals(0, result.getMstEdges().size());
        assertEquals(0, result.getTotalCost());
    }

    @Test
    void testDisconnectedGraphHandling() {
        Graph disconnectedGraph = TestGraphFactory.createDisconnectedGraph();

        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        MSTResult primResult = prim.findMST(disconnectedGraph);
        MSTResult kruskalResult = kruskal.findMST(disconnectedGraph);

        int components = GraphValidator.countConnectedComponents(disconnectedGraph);
        int expectedEdges = disconnectedGraph.getVertexCount() - components;

        assertEquals(expectedEdges, primResult.getMstEdges().size());
        assertEquals(expectedEdges, kruskalResult.getMstEdges().size());
    }

    @Test
    void testResultsAreReproducible() {
        Graph graph = TestGraphFactory.createSampleGraph();
        PrimMST prim = new PrimMST();

        MSTResult firstRun = prim.findMST(graph);
        MSTResult secondRun = prim.findMST(graph);

        assertEquals(firstRun.getTotalCost(), secondRun.getTotalCost(),
                "Algorithm results should be reproducible");
    }
}