package com.agrawal.tasty.search.model;

import java.util.Set;

/**
 *
 * Trie - Trie is an efficient information retrieval data structure.
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class Trie {

    private final TrieNode root;
    private final boolean caseSensitive;

    public Trie(boolean caseSensitive) {
        this.root = new TrieNode();
        this.caseSensitive = caseSensitive;
    }

    public void populate(int reviewId, String... strings) {
        for (String string : strings) {
            populate(reviewId, string);
        }
    }

    private void populate(int reviewId, String string) {
        String tokens[] = string.split("\\W+");
        for (String token : tokens) {
            addToken(reviewId, token.toCharArray());
        }
    }

    private void addToken(int reviewId, char token[]) {
        TrieNode current = root;
        for (char c : token) {
            if (!caseSensitive && c >= 'A' && c <= 'Z') {
                c += 32;
            }
            current = current.getNode(c);
        }
        current.addOffset(reviewId);
    }

    public Set<Integer> search(String token) {
        TrieNode current = root;
        for (int i = 0; i < token.length(); i++) {
            if (current.isLeaf()) {
                return null;
            }

            char c = token.charAt(i);
            if (!caseSensitive && c >= 'A' && c <= 'Z') {
                c += 32;
            }
            if (!current.hasNode(c)) {
                return null;
            }
            current = current.getNode(c);
        }
        return current.getOffset();
    }

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
