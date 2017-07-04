import java.util.*;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;

public class DeluxeBFS {
    
    private final Digraph G;
    // constructor takes in the digraph G
    public DeluxeBFS(Digraph G) {
        this.G = G;
    }
    
    // create an iterable item with the single vertex, so that the reachable_vertices(Iterable) can be used to improve readability
    // time taken: O(vertices reachable from vertex v)
    public ST<Integer, Integer> reachable_vertices(int v) {
        List<Integer> list = new LinkedList<Integer>();
        list.add(v);
        return reachable_vertices(list);
    }
    
    // create a symbol table for a vertex v, that has keys = vertices and values = length of path from vertex v to the key vertex
    // time taken: O(vertices reachable from vertices in subset)
    public ST<Integer, Integer> reachable_vertices(Iterable<Integer> subset) {
        Queue<Integer> queue = new Queue<Integer>();
        ST<Integer, Integer> st = new ST<Integer, Integer>();
        // iterates through all vertices in the subset and enqueues them and adds to the symbol table
        for (int i : subset) {
            queue.enqueue(i);
            // length of path from vertex i to itself is 0
            st.put(i, 0);
        }
        
        while (!queue.isEmpty()) {
            int vertex = queue.dequeue();
            int distance = st.get(vertex);
            // we enqueue all uncovered vertices; if a vertex has been covered but we discover a shorter path, we update the value for that key in the symbol table
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
