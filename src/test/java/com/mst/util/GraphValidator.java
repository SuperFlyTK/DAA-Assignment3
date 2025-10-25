package com.mst.util;

import com.mst.core.Graph;
import com.mst.core.Edge;

import java.util.*;

public class GraphValidator {

    public static boolean isAcyclic(List<Edge> edges) {
        Map<String, String> parent = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        for (Edge edge : edges) {
            allVertices.add(edge.getFrom());
            allVertices.add(edge.getTo());
        }

        for (String vertex : allVertices) {
            parent.put(vertex, vertex);
        }

        for (Edge edge : edges) {
            String root1 = find(parent, edge.getFrom());
            String root2 = find(parent, edge.getTo());

            if (root1.equals(root2)) {
                return false;
            }

            parent.put(root1, root2);
        }

        return true;
    }

    public static boolean connectsAllVertices(List<Edge> edges, List<String> allVertices) {
        if (edges.isEmpty() && allVertices.size() > 1) return false;

        Set<String> connected = new HashSet<>();
        for (Edge edge : edges) {
            connected.add(edge.getFrom());
            connected.add(edge.getTo());
        }

        return connected.containsAll(allVertices);
    }

    public static int countConnectedComponents(Graph graph) {
        Set<String> visited = new HashSet<>();
        int components = 0;

        Map<String, List<Edge>> adjList = graph.getAdjacencyList();

        for (String vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                components++;
                bfs(vertex, adjList, visited);
            }
        }

        return components;
    }

    private static void bfs(String start, Map<String, List<Edge>> adjList, Set<String> visited) {
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            for (Edge edge : adjList.get(current)) {
                String neighbor = edge.getTo().equals(current) ? edge.getFrom() : edge.getTo();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
    }

    private static String find(Map<String, String> parent, String vertex) {
        if (!parent.get(vertex).equals(vertex)) {
            parent.put(vertex, find(parent, parent.get(vertex)));
        }
        return parent.get(vertex);
    }
}