package com.mst.algorithms;

import com.mst.core.Graph;
import com.mst.core.Vertex;
import com.mst.core.Edge;
import com.mst.core.MSTResult;

import java.util.*;

/**
 * Enhanced Prim's algorithm implementation for custom Graph class
 * Works with Vertex and Edge objects instead of string-based representation
 */
public class EnhancedPrimMST {

    public MSTResult findMST(Graph graph) {
        long startTime = System.nanoTime();
        int operations = 0;

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        if (graph.getVertexCount() == 0) {
            return new MSTResult(mstEdges, totalCost, operations, 0);
        }

        Set<Vertex> visited = new HashSet<>();
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>();

        // Start with first vertex
        Vertex startVertex = graph.getVerticesList().get(0);
        visited.add(startVertex);
        operations++;

        // Add all edges from start vertex to priority queue
        edgeQueue.addAll(startVertex.getAdjacentEdges());
        operations += startVertex.getDegree();

        while (!edgeQueue.isEmpty() && visited.size() < graph.getVertexCount()) {
            Edge currentEdge = edgeQueue.poll();
            operations++;

            Vertex unvisitedVertex = getUnvisitedVertex(currentEdge, visited);

            if (unvisitedVertex != null) {
                visited.add(unvisitedVertex);
                mstEdges.add(currentEdge);
                totalCost += currentEdge.getWeight();
                operations += 3;

                // Add edges from the newly visited vertex
                for (Edge edge : unvisitedVertex.getAdjacentEdges()) {
                    operations++;
                    Vertex otherVertex = edge.getOtherVertex(unvisitedVertex);
                    if (!visited.contains(otherVertex)) {
                        edgeQueue.add(edge);
                        operations++;
                    }
                }
            }
        }

        long executionTime = System.nanoTime() - startTime;
        return new MSTResult(mstEdges, totalCost, operations, executionTime);
    }

    private Vertex getUnvisitedVertex(Edge edge, Set<Vertex> visited) {
        boolean fromVisited = visited.contains(edge.getFrom());
        boolean toVisited = visited.contains(edge.getTo());

        if (fromVisited && !toVisited) {
            return edge.getTo();
        }
        if (toVisited && !fromVisited) {
            return edge.getFrom();
        }
        return null;
    }

    /**
     * Find MST starting from a specific district
     */
    public MSTResult findMSTFromDistrict(Graph graph, String districtId) {
        Vertex startVertex = graph.getVertex(districtId);
        if (startVertex == null) {
            throw new IllegalArgumentException("District not found: " + districtId);
        }

        long startTime = System.nanoTime();
        int operations = 0;

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;
        Set<Vertex> visited = new HashSet<>();
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>();

        visited.add(startVertex);
        operations++;
        edgeQueue.addAll(startVertex.getAdjacentEdges());
        operations += startVertex.getDegree();

        while (!edgeQueue.isEmpty() && visited.size() < graph.getVertexCount()) {
            Edge currentEdge = edgeQueue.poll();
            operations++;

            Vertex unvisitedVertex = getUnvisitedVertex(currentEdge, visited);

            if (unvisitedVertex != null) {
                visited.add(unvisitedVertex);
                mstEdges.add(currentEdge);
                totalCost += currentEdge.getWeight();
                operations += 3;

                for (Edge edge : unvisitedVertex.getAdjacentEdges()) {
                    operations++;
                    Vertex otherVertex = edge.getOtherVertex(unvisitedVertex);
                    if (!visited.contains(otherVertex)) {
                        edgeQueue.add(edge);
                        operations++;
                    }
                }
            }
        }

        long executionTime = System.nanoTime() - startTime;
        return new MSTResult(mstEdges, totalCost, operations, executionTime);
    }
}