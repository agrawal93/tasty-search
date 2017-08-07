package com.agrawal.tasty.search.model;

import com.agrawal.tasty.search.datastructure.DataStructure;
import com.agrawal.tasty.search.datastructure.DataStructureFactory;
import java.util.Set;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class ReviewTrie {

    private static final DataStructure root = DataStructureFactory.getInstance(DataStructureFactory.DataStructureType.TRIE, false, false);

    public static void addReview(int reviewId, String... tokens) {
        root.addReview(reviewId, tokens);
    }

    public static Set<Integer> searchReviews(String token) {
        return root.searchReviews(token);
    }

    public static void display() {
        root.display();
    }

}
