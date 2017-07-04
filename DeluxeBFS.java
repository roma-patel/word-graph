import java.util.*;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;

public class DeluxeBFS {
    
    private final Digraph G;
    // input directed graph of synset-hypernym links
    public DeluxeBFS(Digraph G) {
        this.G = G;
    }
    
    // create an iterable item with the single vertex, so that the reachable_vertices(Iterable) can be used 
    public ST<Integer, Integer> reachable_vertices(int v) {
        List<Integer> list = new LinkedList<Integer>();
        list.add(v);
        return reachable_vertices(list);
    }
    
    // create a symbol table for a vertex v with keys = vertices and values = length of path from vertex v to the key vertex
    public ST<Integer, Integer> reachable_vertices(Iterable<Integer> subset) {
        Queue<Integer> queue = new Queue<Integer>();
        ST<Integer, Integer> st = new ST<Integer, Integer>();
        for (int i : subset) {
            queue.enqueue(i);
            // length of path from vertex i to itself is 0
            st.put(i, 0);
        }
        
        while (!queue.isEmpty()) {
            int vertex = queue.dequeue();
            int distance = st.get(vertex);
            // enqueue all uncovered vertices; update values for covered vertices if more optimal path found
            for (int i : G.adj(vertex)) 
                if (!st.contains(i) || distance+1 < st.get(i)) {
                    queue.enqueue(i);
                    st.put(i, distance+1);
                }
        }
        return st;
    }
    
    public static void main(String[] args) {
    }
}
