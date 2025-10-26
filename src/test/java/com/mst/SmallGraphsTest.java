    package com.mst;

    import com.mst.algorithms.PrimMST;
    import com.mst.algorithms.KruskalMST;
    import com.mst.core.Graph;
    import com.mst.core.MSTResult;
    import com.mst.util.GraphValidator;
    import org.junit.jupiter.api.Test;

    import static org.junit.jupiter.api.Assertions.*;

    public class SmallGraphsTest {

        @Test
        void test4VertexGraph() {
            Graph graph = TestGraphFactory.create4VertexGraph();

            PrimMST prim = new PrimMST();
            KruskalMST kruskal = new KruskalMST();

            MSTResult primResult = prim.findMST(graph);
            MSTResult kruskalResult = kruskal.findMST(graph);

            assertEquals(6, primResult.getTotalCost());
            assertEquals(6, kruskalResult.getTotalCost());
            assertEquals(3, primResult.getMstEdges().size());
            assertTrue(GraphValidator.isAcyclic(primResult.getMstEdges()));
        }

        @Test
        void test5VertexGraph() {
            Graph graph = TestGraphFactory.create5VertexGraph();

            PrimMST prim = new PrimMST();
            KruskalMST kruskal = new KruskalMST();

            MSTResult primResult = prim.findMST(graph);
            MSTResult kruskalResult = kruskal.findMST(graph);

            assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
            assertEquals(4, primResult.getMstEdges().size());
            assertTrue(GraphValidator.connectsAllVertices(primResult.getMstEdges(), graph.getVertices()));
        }

        @Test
        void test6VertexGraph() {
            Graph graph = TestGraphFactory.create6VertexGraph();

            PrimMST prim = new PrimMST();
            KruskalMST kruskal = new KruskalMST();

            MSTResult primResult = prim.findMST(graph);
            MSTResult kruskalResult = kruskal.findMST(graph);

            assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
            assertEquals(5, primResult.getMstEdges().size());
            assertTrue(primResult.isValidMST(graph.getVertexCount()));
        }
    }