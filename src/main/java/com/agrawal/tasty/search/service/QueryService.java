package com.agrawal.tasty.search.service;

import com.agrawal.tasty.search.model.IndexedReviews;
import com.agrawal.tasty.search.model.Review;
import com.agrawal.tasty.search.model.ReviewTrie;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
@Service
public class QueryService {

    public List<String> search(String query[]) {
        return search(query, 20);
    }

    public List<String> search(String tokens[], int K) {
        IndexedReviews.resetSampledReviews();
        Set<Review> reviews = new TreeSet<>();
        for (String token : tokens) {
            Set<Integer> reviewSet = ReviewTrie.searchReviews(token);
            if (reviewSet == null || reviewSet.isEmpty()) {
                continue;
            }
            for(int reviewId : reviewSet) {
                Review currentReview = IndexedReviews.getReview(reviewId);
                if(currentReview == null) continue;
                if (reviews.contains(currentReview)) {
                    reviews.remove(currentReview);
                }
                currentReview.queryHit();
                reviews.add(currentReview);
            }
        }
        
        List<String> topReviews = new ArrayList<>(K);
        for (Review review : reviews) {
            if (K-- > 0) {
                topReviews.add(review.getReview());
            } else {
                break;
            }
        }

        return topReviews;
    }

}
