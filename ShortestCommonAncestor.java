import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

public class ShortestCommonAncestor {

    private final Digraph G, copy;
    private final DeluxeBFS dbfs;
    private static int root;
    // input a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null)
            throw new NullPointerException();
        if (!isRooted(G))
            throw new IllegalArgumentException();
        this.G = G;
        dbfs = new DeluxeBFS(G);
        // defensive copy of the digraph G
        copy = new Digraph(G);
    }
    
    // sanity check (?); word net digraph is always rooted
    public boolean isRooted(Digraph G) {
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle())
            return false;
	// count degree zero, degree greater vertices
        int count_zero = 0;
        int count_greater = 0; 
        for (int i = 0; i < G.V(); i++) 
            if (G.outdegree(i) == 0) {
                count_zero++;
                root = i;
            }
            else count_greater++;
        
        if (count_zero == 1 && count_greater == G.V()-1) 
            return true;
        else return false;  
    }
    
    // finds the min distance; calls private min_ancestor function to find the closest ancestor and then computes and returns the distance
    private int min_length(ST<Integer, Integer> st_v, ST<Integer, Integer> st_w) {
        int ancestor = min_ancestor(st_v, st_w);
        return (st_v.get(ancestor) + st_w.get(ancestor));
    }
    
    private int min_ancestor(ST<Integer, Integer> st_v, ST<Integer, Integer> st_w) {
        int min_ancestor = root;
        // to get an upperbound on the maximum path length or distance
        int min_distance = G.E();
        for (int i : st_v.keys()) 
            if (st_w.contains(i)) {
                int distance = st_v.get(i) + st_w.get(i);
                if (distance < min_distance) {
                    min_distance = distance;
                    min_ancestor = i;
                }
            }
        return min_ancestor;
    }
    
    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IllegalArgumentException();
        
        ST<Integer, Integer> st_v = dbfs.reachable_vertices(v);
        ST<Integer, Integer> st_w = dbfs.reachable_vertices(w);

        int min_distance = min_length(st_v, st_w);
        return min_distance;
    }
        
    // a shortest common ancestor of vertices v and w 
    public int ancestor(int v, int w) {
        if (v >= G.V() || w >= G.V())
            throw new IllegalArgumentException();
     
        ST<Integer, Integer> st_v = dbfs.reachable_vertices(v);
        ST<Integer, Integer> st_w = dbfs.reachable_vertices(w);
        
        int min = min_ancestor(st_v, st_w);
        return min;
    }
    
    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null)
            throw new IllegalArgumentException();
        
        ST<Integer, Integer> st_v = dbfs.reachable_vertices(subsetA);
        ST<Integer, Integer> st_w = dbfs.reachable_vertices(subsetB);
        
        int min_distance = min_length(st_v, st_w);
        return min_distance;
    }
        
    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null)
            throw new IllegalArgumentException();

        ST<Integer, Integer> st_v = dbfs.reachable_vertices(subsetA);
        ST<Integer, Integer> st_w = dbfs.reachable_vertices(subsetB);
     
        int min = min_ancestor(st_v, st_w);
        return min;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        System.out.println("asas");
        String s = "digraph1.txt";
        In in = new In(s);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
        
    }
}
