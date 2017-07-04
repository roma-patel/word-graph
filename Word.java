import java.util.*;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
// data structures used: two ordered symbol tables (using ST.java in algs4) that perform contains() and put() in logarithmic time, we use an iterable integer list to store synset IDs of nouns that appear in multiple synsets
public class Word {

    private final ShortestCommonAncestor s;
    private ST<String, List<Integer>> st1 = new ST<String, List<Integer>>();
    private ST<Integer, String> st2 = new ST<Integer, String>();
    private Digraph G;
 
   // the constructor takes in the names of the two input files and then calls individual private methods to deal with each input file accordinglys
    public Word(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException();
        
        syn(synsets);
        hyp(hypernyms);
        s = new ShortestCommonAncestor(G);
    }
    
    // create two ordered symbol tables to deal with the synset input file, the first one has keys: individual nouns (string), values: synset ID of the noun (integer). the second has keys: synset ID (integer), values: synset (string) 
    // time taken: linearithmic in size of input file
    private void syn(String synsets) {
        String[] line, noun;
        In in_synsets = new In(synsets);

        while(in_synsets.hasNextLine()) {
            String s = in_synsets.readLine();
            line = s.split(",");
            noun = line[1].split(" ");
            
            // integer list stores all the synsets that the noun belongs to
            List<Integer> list;
            for (int i = 0; i < noun.length; i++) {
                if (!st1.contains(noun[i]))
                    list = new LinkedList<Integer>();
                else 
                    list = st1.get(noun[i]);
                list.add(Integer.parseInt(line[0]));
                st1.put(noun[i], list);
            }
            st2.put(Integer.parseInt(line[0]), line[1]);
        }
    }
    // creates a digraph for the synset-hypernym relations
    // time taken: linearithmic in input file size
    private void hyp(String hypernyms) {
        // the number of synsets i.e. vertices that should be in the digraph = st2.size()
        G = new Digraph(st2.size());
        String[] line;

        In in = new In(hypernyms);
        while(in.hasNextLine()) {
            line = in.readLine().split(",");
            int synsetID = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) 
                G.addEdge(synsetID, Integer.parseInt(line[i]));
        }
    }

    // returns all WordNet nouns in alphabetical order
    // time taken: O(number of nouns that exist)
    public Iterable<String> nouns() {
     return(st1.keys());
    }

    // is the word a WordNet noun?
    // time taken: O(log of number of nouns) 
    public boolean isNoun(String word) {
        if (word == null)
            throw new NullPointerException();
        
        return(st1.get(word) != null);
    }

   // returns a synset (second field of synsets.txt) that is a shortest common ancestor of noun1 and noun2
   // time taken: time taken by ancestor function: O(vertices reachable from the synsets)
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null || !isNoun(noun1) || !isNoun(noun2))
            throw new IllegalArgumentException();
  
        int answer = s.ancestor(st1.get(noun1), st1.get(noun2)); 
        return st2.get(answer);
    }

    // returns the distance between noun1 and noun2 (defined below)
    // time taken: time taken by the length function: O(number of vertices reachble from the synsets)
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null || !isNoun(noun1) || !isNoun(noun2))
            throw new IllegalArgumentException();

        return s.length(st1.get(noun1), st1.get(noun2));
    }

   // do unit testing of this class
    public static void main(String[] args) {
       
    }
}
