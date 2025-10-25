package com.mst.algorithms;

import com.mst.core.Graph;
import com.mst.core.Edge;
import com.mst.core.MSTResult;

import java.util.*;

public class PrimMST {

    public MSTResult findMST(Graph graph) {
        long startTime = System.nanoTime();
        int operations = 0;

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        if (graph.getVertexCount() == 0) {
            return new MSTResult(mstEdges, totalCost, operations, 0);
        }

        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>();
        Map<String, List<Edge>> adjList = graph.getAdjacencyList();

        // Start with first vertex
        String startVertex = graph.getVertices().get(0);
        visited.add(startVertex);
        operations++;

        // Add all edges from start vertex
        edgeQueue.addAll(adjList.get(startVertex));
        operations += adjList.get(startVertex).size();

        while (!edgeQueue.isEmpty() && visited.size() < graph.getVertexCount()) {
            Edge currentEdge = edgeQueue.poll();
            operations++;

            String nextVertex = findUnvisitedVertex(currentEdge, visited);

            if (nextVertex != null) {
                visited.add(nextVertex);
                mstEdges.add(currentEdge);
                totalCost += currentEdge.getWeight();
                operations += 3;

                // Add edges from the newly visited vertex
                for (Edge edge : adjList.get(nextVertex)) {
                    operations++;
                    if (!visited.contains(edge.getTo())) {
                        edgeQueue.add(edge);
                        operations++;
                    }
                }
            }
        }

        long executionTime = System.nanoTime() - startTime;
        return new MSTResult(mstEdges, totalCost, operations, executionTime);
    }

    private String findUnvisitedVertex(Edge edge, Set<String> visited) {
        if (visited.contains(edge.getFrom()) && !visited.contains(edge.getTo())) {
            return edge.getTo();
        }
        if (visited.contains(edge.getTo()) && !visited.contains(edge.getFrom())) {
            return edge.getFrom();
        }
        return null;
    }
}