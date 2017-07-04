import edu.princeton.cs.algs4.In;
public class Outcast {
    
    private final Word wordnet;
    // input a Word object
    public Outcast(Word wordnet) {
        this.wordnet = wordnet;
    }
    // returns least similar noun from array
    public String outcast(String[] nouns) {
        int max = 0;
        String noun = nouns[0];
        for (String s1 : nouns) {
            int distance = 0;
            for (String s2 : nouns) {
                if (s1.equals(s2)) continue;
                distance += wordnet.distance(s1, s2);
            }
            if (distance > max) {
                max = distance;
                noun = s1;
            }
        }
        return noun;
    }
    
    // testing
    public static void main(String[] args) {
        Word wordnet = new Word(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        /*for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }*/
        In in = new In(args[2]);
        String[] arr = new String[1];
        arr[0] = in.readString();
        System.out.println(outcast.outcast(arr));
    }
}
