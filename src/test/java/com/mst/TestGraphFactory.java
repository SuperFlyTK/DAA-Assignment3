package com.mst;

import com.mst.core.Graph;
import com.mst.core.Edge;
import java.util.*;

public class TestGraphFactory {

    public static Graph createSampleGraph() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 4),
                new Edge("A", "C", 3),
                new Edge("B", "C", 2),
                new Edge("B", "D", 5),
                new Edge("C", "D", 7),
                new Edge("C", "E", 8),
                new Edge("D", "E", 6)
        );
        return new Graph(1, vertices, edges);
    }

    public static Graph createMediumGraph() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 4),
                new Edge("A", "C", 3),
                new Edge("B", "C", 2),
                new Edge("B", "D", 5),
                new Edge("C", "D", 7),
                new Edge("C", "E", 8),
                new Edge("D", "E", 6),
                new Edge("E", "F", 9),
                new Edge("F", "G", 10),
                new Edge("G", "H", 11),
                new Edge("A", "H", 12)
        );
        return new Graph(2, vertices, edges);
    }

    public static Graph createLargeGraph() {
        List<String> vertices = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            vertices.add("N" + i);
        }

        List<Edge> edges = new ArrayList<>();
        Random random = new Random(42);

        // Create a connected graph
        for (int i = 1; i < vertices.size(); i++) {
            int weight = 1 + random.nextInt(20);
            edges.add(new Edge(vertices.get(i-1), vertices.get(i), weight));
        }

        // Add some extra edges
        for (int i = 0; i < 30; i++) {
            int from = random.nextInt(vertices.size());
            int to = random.nextInt(vertices.size());
            if (from != to) {
                int weight = 1 + random.nextInt(50);
                edges.add(new Edge(vertices.get(from), vertices.get(to), weight));
            }
        }

        return new Graph(3, vertices, edges);
    }

    public static Graph createConnectedGraph() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("B", "C", 2),
                new Edge("C", "D", 3),
                new Edge("A", "D", 4)
        );
        return new Graph(4, vertices, edges);
    }

    public static Graph createSingleNodeGraph() {
        List<String> vertices = Collections.singletonList("A");
        List<Edge> edges = Collections.emptyList();
        return new Graph(5, vertices, edges);
    }

    public static Graph createDisconnectedGraph() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("C", "D", 2)
        );
        return new Graph(6, vertices, edges);
    }

    public static Graph create4VertexGraph() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("A", "C", 4),
                new Edge("B", "C", 2),
                new Edge("B", "D", 5),
                new Edge("C", "D", 3)
        );
        return new Graph(7, vertices, edges);
    }

    public static Graph create5VertexGraph() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 2),
                new Edge("A", "C", 3),
                new Edge("B", "C", 1),
                new Edge("B", "D", 4),
                new Edge("C", "D", 5),
                new Edge("C", "E", 6),
                new Edge("D", "E", 7)
        );
        return new Graph(8, vertices, edges);
    }

    public static Graph create6VertexGraph() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D", "E", "F");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 3),
                new Edge("A", "C", 1),
                new Edge("B", "C", 4),
                new Edge("B", "D", 2),
                new Edge("C", "D", 5),
                new Edge("C", "E", 6),
                new Edge("D", "E", 7),
                new Edge("D", "F", 8),
                new Edge("E", "F", 9)
        );
        return new Graph(9, vertices, edges);
    }
}