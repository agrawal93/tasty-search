package com.agrawal.tasty.search.datastructure;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author I322819
 */
public class HashMap extends DataStructure {

    private final Map<String, Set<Integer>> structure = new java.util.HashMap<>();
    
    public HashMap(boolean partialSearch, boolean caseSensitive) {
        super(partialSearch, caseSensitive);
    }
    
    @Override
    public void addReview(int reviewId, String... tokens) {
        for(String token : tokens) {
            if(!caseSensitive) token = token.toLowerCase();
            if(!structure.containsKey(token)) {
                synchronized(structure) {
                    structure.put(token, new HashSet<>());
                    structure.get(token).add(reviewId);
                }
            } else {
                structure.get(token).add(reviewId);
            }
        }
    }

    @Override
    public Set<Integer> searchReviews(String token) {
        if(!caseSensitive) token = token.toLowerCase();
        return structure.get(token);
    }

    @Override
    public void display() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
