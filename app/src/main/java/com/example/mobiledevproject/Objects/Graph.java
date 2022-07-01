package com.example.mobiledevproject.Objects;

// Java program to implement Graph
// with the help of Generics

import android.util.Log;

import java.util.*;

public class Graph<T> {

    // We use Hashmap to store the edges in the graph
    private Map<T, List<T>> map = new HashMap<>();

    // This function adds a new vertex to the graph
    public void addVertex(T s) {
        map.put(s, new LinkedList<T>());
    }

    // This function adds the edge
    // between source to destination
    public void addEdge(T source,
                        T destination,
                        boolean bidirectional) {

        if (!map.containsKey(source))
            addVertex(source);

        if (!map.containsKey(destination))
            addVertex(destination);

        map.get(source).add(destination);
        if (bidirectional) {
            map.get(destination).add(source);
        }
    }

    // This function gives the count of vertices
    public void getVertexCount() {
        System.out.println("The graph has "
                + map.keySet().size()
                + " vertex");
    }

    // This function gives the count of edges
    public void getEdgesCount(boolean bidirection) {
        int count = 0;
        for (T v : map.keySet()) {
            count += map.get(v).size();
        }
        if (bidirection) {
            count = count / 2;
        }
        System.out.println("The graph has "
                + count
                + " edges.");
    }

    // This function gives whether
    // a vertex is present or not.
    public void hasVertex(T s) {
        if (map.containsKey(s)) {
            System.out.println("The graph contains "
                    + s + " as a vertex.");
        } else {
            System.out.println("The graph does not contain "
                    + s + " as a vertex.");
        }
    }

    // This function gives whether an edge is present or not.
    public void hasEdge(T s, T d) {
        if (map.get(s).contains(d)) {
            System.out.println("The graph has an edge between "
                    + s + " and " + d + ".");
        } else {
            System.out.println("The graph has no edge between "
                    + s + " and " + d + ".");
        }
    }

    // Prints the adjancency list of each vertex.
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (T v : map.keySet()) {
            builder.append(v.toString() + ": ");
            for (T w : map.get(v)) {
                builder.append(w.toString() + " ");
            }
            builder.append("\n");
        }

        return (builder.toString());
    }

    public String getNextVertex(String currentVertex, int direction) {
        String nextVertex = null;
        if (map.containsKey(currentVertex)) {
            List<T> neighbors = map.get(currentVertex);
            if(neighbors.size() >= direction)
                nextVertex = neighbors.get(direction).toString();
        }
        return nextVertex;
    }

    public T getNextVertexGen(T currentVertex, int direction) {
        T nextVertex = null;
        if (map.containsKey(currentVertex)) {
            List<T> neighbors = map.get(currentVertex);
            if(neighbors.size() >= direction)
                nextVertex = neighbors.get(direction);
        }
        return nextVertex;
    }

    public List<String> getNeighbors(String currentVertex) {
        List<String> neighbors = new ArrayList<>();
        if (map.containsKey(currentVertex)) {
            List<T> neighborsList = map.get(currentVertex);
            for (T neighbor : neighborsList) {
                neighbors.add(neighbor.toString());
            }
        }
        return neighbors;
    }
    //get all neighbors of a vertex
    public List<T> getNeighborsGen(String currentVertex) {
        List<T> neighbors = new ArrayList<>();
        if (map.containsKey(currentVertex)) {
            neighbors = map.get(currentVertex);
        }
        return neighbors;
    }

    public String getNeighborDirection(String currentVertex, String neighbor) {
        String[] directions = {"up", "right", "down", "left"};
        String direction = "";
        if (map.containsKey(currentVertex)) {
            Log.d("Tagu", "In Graph: " + currentVertex);
            List<T> neighborsList = map.get(currentVertex);
            for (int i = 0; i < neighborsList.size(); i++) {
                if (neighborsList.get(i).toString().equals(neighbor)) {
                    direction = directions[i];
                }
            }
        }
        return direction;
    }

    //return shortest path from start to end
    public List<String> getShortestPath(String start, String end) {
        List<String> path = new ArrayList<>();
        Map<String, Integer> distance = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();
        List<String> queue = new ArrayList<>();
        queue.add(start);
        distance.put(start, 0);
        while (!queue.isEmpty()) {
            String current = queue.remove(0);
            visited.add(current);
            for (String neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && !neighbor.isEmpty()) {
                    queue.add(neighbor);
                    int alt = distance.get(current) + 1;
                    if (alt < distance.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                        distance.put(neighbor, alt);
                        previous.put(neighbor, current);
                    }
                }
            }
        }
        String vertex = end;
        while (previous.containsKey(vertex)) {
            //Log.d("Tagu", "in Graph: " + vertex);
            path.add(vertex);
            vertex = previous.get(vertex);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }

}

// Driver Code
class Main2 {

    public static void main(String args[]) {

        // Object of graph is created.
        Graph<Integer> g = new Graph<Integer>();

        // edges are added.
        // Since the graph is bidirectional,
        // so boolean bidirectional is passed as true.
        g.addEdge(0, 1, true);
        g.addEdge(0, 4, true);
        g.addEdge(1, 2, true);
        g.addEdge(1, 3, true);
        g.addEdge(1, 4, true);
        g.addEdge(2, 3, true);
        g.addEdge(3, 4, true);

        // Printing the graph
        System.out.println("Graph:\n"
                + g.toString());

        // Gives the no of vertices in the graph.
        g.getVertexCount();

        // Gives the no of edges in the graph.
        g.getEdgesCount(true);

        // Tells whether the edge is present or not.
        g.hasEdge(3, 4);

        // Tells whether vertex is present or not
        g.hasVertex(5);
    }
}