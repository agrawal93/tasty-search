package com.agrawal.tasty.search.datastructure;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class HashMap extends DataStructure {

    private final Map<String, Set<Integer>> structure = new ConcurrentHashMap<>();

    public HashMap(boolean partialSearch, boolean caseSensitive) {
        super(partialSearch, caseSensitive);
    }

    @Override
    public void addToken(int reviewId, String token) {
        if (!caseSensitive) {
            token = token.toLowerCase();
        }
        if (!structure.containsKey(token)) {
            structure.put(token, new HashSet<>());
        }
        structure.get(token).add(reviewId);
    }

    @Override
    public Set<Integer> searchToken(String token) {
        if (!caseSensitive) {
            token = token.toLowerCase();
        }
        return Collections.unmodifiableSet(structure.get(token));
    }

    @Override
    public void display() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
