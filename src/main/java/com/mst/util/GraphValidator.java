package com.mst.util;

import com.mst.core.Edge;
import com.mst.core.Graph;
import java.util.*;

public class GraphValidator {

    public static boolean isAcyclic(List<Edge> edges) {
        if (edges.isEmpty()) return true;

        Map<String, List<String>> adjList = new HashMap<>();
        for (Edge edge : edges) {
            adjList.computeIfAbsent(edge.getFrom(), k -> new ArrayList<>()).add(edge.getTo());
            adjList.computeIfAbsent(edge.getTo(), k -> new ArrayList<>()).add(edge.getFrom());
        }

        Set<String> visited = new HashSet<>();
        for (String vertex : adjList.keySet()) {
            if (!visited.contains(vertex)) {
                if (hasCycle(vertex, null, adjList, visited, new HashSet<>())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean hasCycle(String vertex, String parent,
                                    Map<String, List<String>> adjList,
                                    Set<String> visited, Set<String> recursionStack) {
        visited.add(vertex);
        recursionStack.add(vertex);

        for (String neighbor : adjList.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (hasCycle(neighbor, vertex, adjList, visited, recursionStack)) {
                    return true;
                }
            } else if (!neighbor.equals(parent) && recursionStack.contains(neighbor)) {
                return true;
            }
        }

        recursionStack.remove(vertex);
        return false;
    }

    public static boolean connectsAllVertices(List<Edge> edges, List<String> vertices) {
        if (vertices.isEmpty()) return true;
        if (edges.isEmpty()) return vertices.size() == 1;

        Map<String, List<String>> adjList = new HashMap<>();
        for (Edge edge : edges) {
            adjList.computeIfAbsent(edge.getFrom(), k -> new ArrayList<>()).add(edge.getTo());
            adjList.computeIfAbsent(edge.getTo(), k -> new ArrayList<>()).add(edge.getFrom());
        }

        Set<String> visited = new HashSet<>();
        dfs(vertices.get(0), adjList, visited);

        return visited.size() == vertices.size();
    }

    private static void dfs(String vertex, Map<String, List<String>> adjList, Set<String> visited) {
        visited.add(vertex);
        for (String neighbor : adjList.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, adjList, visited);
            }
        }
    }

    public static int countConnectedComponents(Graph graph) {
        Map<String, List<String>> adjList = graph.getAdjacencyListForValidation();
        Set<String> visited = new HashSet<>();
        int components = 0;

        for (String vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                components++;
                dfs(vertex, adjList, visited);
            }
        }
        return components;
    }
}