package com.example.metro.graph;

import java.util.*;

public class MetroGraph {

    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();
    private final Map<String, String> keyToName = new HashMap<>();

    public void addEdge(String from, String to, double distance) {
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, distance));
        adjacencyList.computeIfAbsent(to, k -> new ArrayList<>()).add(new Edge(from, distance));
    }

    public void registerStationName(String key, String name) {
        keyToName.put(key, name);
    }

    public Map<String, String> getKeyToName() {
        return keyToName;
    }

    public List<String> findShortestPath(String source, String destination) {
        Map<String, Double> distance = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(n -> n.distance));

        for (String station : adjacencyList.keySet()) {
            distance.put(station, Double.MAX_VALUE);
        }
        distance.put(source, 0.0);
        pq.add(new Node(source, 0.0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.name.equals(destination)) break;

            for (Edge edge : adjacencyList.getOrDefault(current.name, Collections.emptyList())) {
                double newDist = distance.get(current.name) + edge.weight;
                if (newDist < distance.get(edge.to)) {
                    distance.put(edge.to, newDist);
                    previous.put(edge.to, current.name);
                    pq.add(new Node(edge.to, newDist));
                }
            }
        }

        List<String> path = new ArrayList<>();
        for (String at = destination; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        if (path.isEmpty() || !path.get(0).equals(source)) {
            return Collections.emptyList();
        }

        return path;
    }

    private static class Edge {
        String to;
        double weight;

        Edge(String to, double weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    private static class Node {
        String name;
        double distance;

        Node(String name, double distance) {
            this.name = name;
            this.distance = distance;
        }
    }
}