# word-graph

Input a list of nouns (of those included in WordNet's synset list -- which is presumably most nouns) and output the two least semantically similar nouns. 

This creates a directed graph with synset-hypernym links and traverses them to find the shortest common ancestor of pairs of words to determine semantic relatedness.

WordNet resources: http://wordnet.princeton.edu/