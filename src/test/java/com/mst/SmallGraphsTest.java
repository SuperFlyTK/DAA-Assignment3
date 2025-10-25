package com.mst;

import com.mst.algorithms.PrimMST;
import com.mst.algorithms.KruskalMST;
import com.mst.core.Graph;
import com.mst.core.MSTResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Specific tests for small graphs (4-6 vertices) as required in criteria
 */
public class SmallGraphsTest {

    @Test
    void test4VertexGraph() {
        Graph graph = TestGraphFactory.create4VertexGraph();
        testSmallGraphCorrectness(graph);
    }

    @Test
    void test5VertexGraph() {
        Graph graph = TestGraphFactory.create5VertexGraph();
        testSmallGraphCorrectness(graph);
    }

    @Test
    void test6VertexGraph() {
        Graph graph = TestGraphFactory.create6VertexGraph();
        testSmallGraphCorrectness(graph);
    }

    private void testSmallGraphCorrectness(Graph graph) {
        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        MSTResult primResult = prim.findMST(graph);
        MSTResult kruskalResult = kruskal.findMST(graph);

        // Verify algorithms produce same cost
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());

        // Verify MST has V-1 edges
        assertEquals(graph.getVertexCount() - 1, primResult.getMstEdges().size());
        assertEquals(graph.getVertexCount() - 1, kruskalResult.getMstEdges().size());

        // Verify performance metrics are valid
        assertTrue(primResult.getExecutionTime() >= 0);
        assertTrue(kruskalResult.getExecutionTime() >= 0);
        assertTrue(primResult.getOperationsCount() >= 0);
        assertTrue(kruskalResult.getOperationsCount() >= 0);
    }

    @Test
    void testSmallGraphPerformance() {
        Graph graph = TestGraphFactory.create6VertexGraph();

        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        // Run multiple times to test consistency
        MSTResult firstRun = prim.findMST(graph);
        MSTResult secondRun = prim.findMST(graph);

        // Results should be consistent
        assertEquals(firstRun.getTotalCost(), secondRun.getTotalCost());
        assertEquals(firstRun.getMstEdges().size(), secondRun.getMstEdges().size());
    }
}