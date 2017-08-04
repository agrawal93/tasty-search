package com.agrawal.tasty.search.model;

import java.util.Set;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class ReviewTrie {

    private static final Trie root = new Trie(false);

    public static void addReview(int reviewId, String... tokens) {
        root.populate(reviewId, tokens);
    }

    public static Set<Integer> searchReviews(String token) {
        return root.search(token);
    }

    public static void display() {
        root.display();
    }

}
