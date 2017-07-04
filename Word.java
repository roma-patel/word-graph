import java.util.*;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

public class Word {

    private final ShortestCommonAncestor s;
    private ST<String, List<Integer>> st1 = new ST<String, List<Integer>>();
    private ST<Integer, String> st2 = new ST<Integer, String>();
    private Digraph G;
 
    // input synset and hypernym files from word net
    public Word(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException();
        
        syn(synsets);
        hyp(hypernyms);
        s = new ShortestCommonAncestor(G);
    }
    
    // create two ordered STs for synset input file, one with keys: individual nouns (string), values: synset ID of the noun (integer). the second with keys: synset ID (integer), values: synset (string) 
    private void syn(String synsets) {
        String[] line, noun;
        In in_synsets = new In(synsets);

        while(in_synsets.hasNextLine()) {
            String s = in_synsets.readLine();
            line = s.split(",");
            noun = line[1].split(" ");
            
	    // store all synsets that one noun belongs to
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

    // returns WordNet nouns in alphabetical order
    public Iterable<String> nouns() {
     return(st1.keys());
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new NullPointerException();
        
        return(st1.get(word) != null);
    }

   // returns shortest common ancestor (synset) of noun1 and noun2
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null || !isNoun(noun1) || !isNoun(noun2))
            throw new IllegalArgumentException();
  
        int answer = s.ancestor(st1.get(noun1), st1.get(noun2)); 
        return st2.get(answer);
    }

    // returns path distance between noun1 and noun2 
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null || !isNoun(noun1) || !isNoun(noun2))
            throw new IllegalArgumentException();

        return s.length(st1.get(noun1), st1.get(noun2));
    }

   // do unit testing of this class
    public static void main(String[] args) {
       
    }
}
