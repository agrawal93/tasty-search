package com.agrawal.tasty.search.datastructure;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author I322819
 */
public class HashMap extends DataStructure {

    private final Map<String, Map<Integer, Object>> structure = new ConcurrentHashMap<>();
    
    public HashMap(boolean partialSearch, boolean caseSensitive) {
        super(partialSearch, caseSensitive);
    }
    
    @Override
    public void addToken(int reviewId, String token) {
        if(!caseSensitive) token = token.toLowerCase();
        if(!structure.containsKey(token)) {
            structure.put(token, new ConcurrentHashMap<>());
        }
        structure.get(token).put(reviewId, null);
    }

    @Override
    public Set<Integer> searchToken(String token) {
        if(!caseSensitive) token = token.toLowerCase();
        return Collections.unmodifiableSet(structure.get(token).keySet());
    }

    @Override
    public void display() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
