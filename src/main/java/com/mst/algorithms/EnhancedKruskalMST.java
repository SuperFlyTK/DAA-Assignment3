package com.mst.algorithms;

import com.mst.core.Graph;
import com.mst.core.Vertex;
import com.mst.core.Edge;
import com.mst.core.MSTResult;

import java.util.*;

/**
 * Enhanced Kruskal's algorithm implementation for custom Graph class
 * Works with Vertex and Edge objects with proper OOP design
 */
public class EnhancedKruskalMST {

    public MSTResult findMST(Graph graph) {
        long startTime = System.nanoTime();
        int operations = 0;

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        // Sort all edges by weight
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdgesList());
        Collections.sort(sortedEdges);
        operations += sortedEdges.size(); // Simplified operation count for sorting

        UnionFind uf = new UnionFind(graph.getVerticesList());

        for (Edge edge : sortedEdges) {
            operations++;
            if (mstEdges.size() == graph.getVertexCount() - 1) break;

            Vertex from = edge.getFrom();
            Vertex to = edge.getTo();

            String root1 = uf.find(from.getId());
            String root2 = uf.find(to.getId());
            operations += 2;

            if (!root1.equals(root2)) {
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                uf.union(from.getId(), to.getId());
                operations += 3;
            }
        }

        long executionTime = System.nanoTime() - startTime;
        return new MSTResult(mstEdges, totalCost, operations, executionTime);
    }

    /**
     * Union-Find data structure for efficient connectivity checks
     */
    private static class UnionFind {
        private final Map<String, String> parent;
        private final Map<String, Integer> rank;

        public UnionFind(List<Vertex> vertices) {
            parent = new HashMap<>();
            rank = new HashMap<>();

            for (Vertex vertex : vertices) {
                parent.put(vertex.getId(), vertex.getId());
                rank.put(vertex.getId(), 0);
            }
        }

        public String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // Path compression
            }
            return parent.get(x);
        }

        public void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) return;

            // Union by rank
            if (rank.get(rootX) < rank.get(rootY)) {
                parent.put(rootX, rootY);
            } else if (rank.get(rootX) > rank.get(rootY)) {
                parent.put(rootY, rootX);
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rank.get(rootX) + 1);
            }
        }

        public int getComponentCount() {
            Set<String> roots = new HashSet<>();
            for (String vertex : parent.keySet()) {
                roots.add(find(vertex));
            }
            return roots.size();
        }
    }
}