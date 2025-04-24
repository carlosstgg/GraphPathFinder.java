/*
 * GraphPathFinder.java
 * This program finds all paths between two nodes in a graph, calculates their costs,
 * and identifies the shortest and cheapest paths.
 * By J. Carlos Gallegos Gutiérrez
 * 04-2025
 * Métodos Númericos en la Ingeniería de Software - Universidad de la Salle Bajio
 */
import java.util.*;

public class GraphPathFinder {
    private Map<String, Map<String, Integer>> graph;
    private List<Path> allPaths;

    class Path {
        List<String> nodes;
        int totalCost;

        Path(List<String> nodes, int totalCost) {
            this.nodes = new ArrayList<>(nodes);
            this.totalCost = totalCost;
        }

        int getLength() {
            return nodes.size() - 1; // number of edges
        }

        @Override
        public String toString() {
            return "Path: " + nodes + " | Cost: " + totalCost + " | Length: " + getLength();
        }
    }

    public GraphPathFinder() {
        graph = new HashMap<>();
        allPaths = new ArrayList<>();
    }

    public static void main(String[] args) {
        GraphPathFinder pathFinder = new GraphPathFinder();
        pathFinder.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        // Input nodes
        System.out.println("Enter all node names separated by space (e.g., A B C D):");
        String[] nodes = scanner.nextLine().split(" ");
        
        for (String node : nodes) {
            graph.put(node.trim(), new HashMap<>());
        }

        // Input edges
        System.out.println("\nEnter edges in format 'Node1 Node2 Cost' (e.g., A B 5). Enter 'done' when finished:");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length != 3) {
                System.out.println("Invalid input. Please use format 'Node1 Node2 Cost'");
                continue;
            }

            String node1 = parts[0];
            String node2 = parts[1];
            int cost;
            
            try {
                cost = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid cost. Please enter a number.");
                continue;
            }

            if (!graph.containsKey(node1) || !graph.containsKey(node2)) {
                System.out.println("One or both nodes don't exist in the graph.");
                continue;
            }

            graph.get(node1).put(node2, cost);
            graph.get(node2).put(node1, cost); // For undirected graph
        }

        // Select start and end nodes
        System.out.println("\nAvailable nodes: " + graph.keySet());
        System.out.print("Enter start node: ");
        String startNode = scanner.nextLine().trim();
        
        System.out.print("Enter end node: ");
        String endNode = scanner.nextLine().trim();

        if (!graph.containsKey(startNode) || !graph.containsKey(endNode)) {
            System.out.println("Invalid start or end node.");
            return;
        }

        // Find all paths
        findAllPaths(startNode, endNode);

        // Display results
        if (allPaths.isEmpty()) {
            System.out.println("\nNo paths found from " + startNode + " to " + endNode);
            return;
        }

        System.out.println("\nAll possible paths:");
        for (Path path : allPaths) {
            System.out.println(path);
        }

        // Find shortest path (fewest nodes)
        Path shortestPath = Collections.min(allPaths, Comparator.comparingInt(Path::getLength));
        System.out.println("\nShortest path (fewest nodes): " + shortestPath);

        // Find cheapest path (lowest cost)
        Path cheapestPath = Collections.min(allPaths, Comparator.comparingInt(p -> p.totalCost));
        System.out.println("Cheapest path (lowest cost): " + cheapestPath);
    }

    private void findAllPaths(String start, String end) {
        Set<String> visited = new HashSet<>();
        List<String> currentPath = new ArrayList<>();
        allPaths.clear();

        dfsAllPaths(start, end, visited, currentPath, 0);
    }

    private void dfsAllPaths(String current, String end, Set<String> visited, List<String> currentPath, int currentCost) {
        visited.add(current);
        currentPath.add(current);

        if (current.equals(end)) {
            allPaths.add(new Path(new ArrayList<>(currentPath), currentCost));
        } else {
            for (Map.Entry<String, Integer> neighbor : graph.get(current).entrySet()) {
                String nextNode = neighbor.getKey();
                int edgeCost = neighbor.getValue();

                if (!visited.contains(nextNode)) {
                    dfsAllPaths(nextNode, end, visited, currentPath, currentCost + edgeCost);
                }
            }
        }

        // Backtrack
        visited.remove(current);
        currentPath.remove(currentPath.size() - 1);
    }
}