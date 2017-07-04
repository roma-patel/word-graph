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
    // constructor takes a rooted DAG as argument
    // time taken: time to check if the digraph is rooted: O(V+E)
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
    
    // check if the digraph is rooted i.e. if it is acyclic and has a root (vertex that is an ancestor of every other vertex)
    // time taken: time taken by DirectedCycle is O(V+E); time to check rooted and multiple roots is O(V), therefore time: O(V+E)
    public boolean isRooted(Digraph G) {
        // if cyclic G is not a DAG; if every node has outdegree >=1 and exactly one has outdegree = 0, then it has exactly one root
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle())
            return false;
        // count_zero counts vertices with outdegree = 0, count_greater counts vertices with outdegree >= 1
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
    
    // private method to find the minimum distance : calls the private min_ancestor function to find the closest ancestor and then computes and returns the distance
    private int min_length(ST<Integer, Integer> st_v, ST<Integer, Integer> st_w) {
        int ancestor = min_ancestor(st_v, st_w);
        return (st_v.get(ancestor) + st_w.get(ancestor));
    }
    
    // private method to find the shortest common ancestor
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
    
    // length of shortest ancestral path between v and w : we find reachable vertices from vertex v in st_v, reachable vertices from w in st_w, pass the two STs to min_length function and compute the minimum distance
    // time taken: time taken by reachable_vertices from v and w: O(number of vertices reachable from v and w)
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IllegalArgumentException();
        
        ST<Integer, Integer> st_v = dbfs.reachable_vertices(v);
        ST<Integer, Integer> st_w = dbfs.reachable_vertices(w);

        // created private methods to implement finding the ancestor and distance rather than doing it insdide each function to help readability
        int min_distance = min_length(st_v, st_w);
        return min_distance;
    }
        
    // a shortest common ancestor of vertices v and w : find reachable vertices from vertices v and w and then call the min_ancestor function
    // time taken: time taken by bfs(v, w) = O(vertices reachable from v and w)
    public int ancestor(int v, int w) {
        if (v >= G.V() || w >= G.V())
            throw new IllegalArgumentException();
     
        ST<Integer, Integer> st_v = dbfs.reachable_vertices(v);
        ST<Integer, Integer> st_w = dbfs.reachable_vertices(w);
        
        int min = min_ancestor(st_v, st_w);
        return min;
    }
    
    // length of shortest ancestral path of vertex subsets A and B
    // time taken: time taken by bfs(subsetA, subsetB) = O(vertices reachable from vertices in the subsets)
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null)
            throw new IllegalArgumentException();
        
        ST<Integer, Integer> st_v = dbfs.reachable_vertices(subsetA);
        ST<Integer, Integer> st_w = dbfs.reachable_vertices(subsetB);
        
        int min_distance = min_length(st_v, st_w);
        return min_distance;
    }
        
    // a shortest common ancestor of vertex subsets A and B
    // time taken: time taken by bfs(subsetA, subsetB) = O(vertices reachable from the vertices in subsets)
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
