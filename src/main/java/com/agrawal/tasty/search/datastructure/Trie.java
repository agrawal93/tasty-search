package com.agrawal.tasty.search.datastructure;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Trie - Trie is an efficient information retrieval data structure.
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class Trie extends DataStructure {

    /**
     *
     * Trie Node structure, as a dependency for creating a Trie ( Refer
     * Trie.java )
     *
     * @author Hardik Agrawal [ hardik93@ymail.com ]
     */
    private static class TrieNode {

        private final Map<Character, TrieNode> nodes;
        private final Set<Integer> reviewIds;
        private boolean leaf;

        public TrieNode() {
            this.nodes = new ConcurrentHashMap<>();
            this.reviewIds = new HashSet<>();
            this.leaf = true;
        }

        public boolean isLeaf() {
            return this.leaf;
        }

        public void setLeaf() {
            this.leaf = true;
        }

        public void unsetLeaf() {
            this.leaf = false;
        }

        public boolean hasNode(Character c) {
            return this.nodes.containsKey(c);
        }

        public synchronized Set<Integer> getOffset() {
            return Collections.unmodifiableSet(this.reviewIds);
        }

        public synchronized void addOffset(int reviewId) {
            this.reviewIds.add(reviewId);
        }

        public Set<Character> characterSet() {
            return this.nodes.keySet();
        }

        public TrieNode getNode(Character c) {
            if (!this.nodes.containsKey(c)) {
                synchronized(this.nodes) {
                    this.nodes.put(c, new TrieNode());
                    this.leaf = false;
                }
            }
            return this.nodes.get(c);
        }

        public synchronized void clear() {
            this.nodes.clear();
        }

    }

    private final TrieNode root;

    public Trie(boolean partialSearch, boolean caseSensitive) {
        super(partialSearch, caseSensitive);
        this.root = new TrieNode();
    }

    @Override
    public void addToken(int reviewId, String token) {
        if(!caseSensitive) token = token.toLowerCase();
        addToken(reviewId, token.toCharArray());
    }

    private void addToken(int reviewId, char token[]) {
        TrieNode current = root;
        for (char c : token) current = current.getNode(c);
        current.addOffset(reviewId);
    }

    private TrieNode search(String token) {
        if(!caseSensitive) token = token.toLowerCase();
        TrieNode current = root;
        for (int i = 0; i < token.length(); i++) {
            if (current.isLeaf()) {
                return null;
            }

            char c = token.charAt(i);
            if (!current.hasNode(c)) {
                return null;
            }
            current = current.getNode(c);
        }
        return current;
    }
    
    @Override
    public Set<Integer> searchToken(String token) {
        TrieNode result = search(token);
        return result == null ? null : result.getOffset();
    }

    @Override
    public void display() {
        display(0, root);
    }

    private void display(int i, TrieNode current) {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < i; j++) {
            builder.append("-");
        }

        Set<Character> charSet = current.characterSet();
        if (charSet == null || charSet.isEmpty()) {
            System.out.println(" [LEAF]");
        }
        for (char c : charSet) {
            TrieNode child = current.getNode(c);
            System.out.println(builder + " " + c + (child.isLeaf() ? " [LEAF]" : " [PARENT]"));
            if (!child.isLeaf()) {
                display(i + 1, child);
            }
        }
    }

}
