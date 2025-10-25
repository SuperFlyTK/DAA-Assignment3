package com.mst.algorithms;

import com.mst.core.Graph;
import com.mst.core.Edge;
import com.mst.core.MSTResult;

import java.util.*;

public class KruskalMST {

    public MSTResult findMST(Graph graph) {
        long startTime = System.nanoTime();
        int operations = 0;

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        // Sort all edges by weight
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        Collections.sort(sortedEdges);
        operations += (int) (sortedEdges.size() * Math.log(sortedEdges.size()));

        UnionFind uf = new UnionFind(graph.getVertices());

        for (Edge edge : sortedEdges) {
            operations++;
            if (mstEdges.size() == graph.getVertexCount() - 1) break;

            String root1 = uf.find(edge.getFrom());
            String root2 = uf.find(edge.getTo());
            operations += 2;

            if (!root1.equals(root2)) {
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                uf.union(edge.getFrom(), edge.getTo());
                operations += 3;
            }
        }

        long executionTime = System.nanoTime() - startTime;
        return new MSTResult(mstEdges, totalCost, operations, executionTime);
    }

    private static class UnionFind {
        private final Map<String, String> parent;
        private final Map<String, Integer> rank;

        public UnionFind(List<String> vertices) {
            parent = new HashMap<>();
            rank = new HashMap<>();

            for (String vertex : vertices) {
                parent.put(vertex, vertex);
                rank.put(vertex, 0);
            }
        }

        public String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) return;

            if (rank.get(rootX) < rank.get(rootY)) {
                parent.put(rootX, rootY);
            } else if (rank.get(rootX) > rank.get(rootY)) {
                parent.put(rootY, rootX);
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rank.get(rootX) + 1);
            }
        }
    }
}