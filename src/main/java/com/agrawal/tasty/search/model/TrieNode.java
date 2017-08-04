package com.agrawal.tasty.search.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Trie Node structure, as a dependency for creating a Trie ( Refer Trie.java )
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
class TrieNode {

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
        return Collections.unmodifiableSet(reviewIds);
    }

    public synchronized void addOffset(int reviewId) {
        this.reviewIds.add(reviewId);
    }

    public Set<Character> characterSet() {
        return this.nodes.keySet();
    }
    
    public TrieNode getNode(Character c) {
        if (!this.nodes.containsKey(c)) {
            this.nodes.put(c, new TrieNode());
            this.leaf = false;
        }
        return this.nodes.get(c);
    }

    public void clear() {
        this.nodes.clear();
    }

}
