package com.agrawal.tasty.search.reviewloader;

import com.agrawal.tasty.search.config.Configuration;
import com.agrawal.tasty.search.model.Review;
import com.agrawal.tasty.search.model.ReviewIndex;
import com.agrawal.tasty.search.model.TokenStructure;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class LoadReviewTask implements Runnable {
    
    private final int reviewId;
    private final String review;
    
    private static final AtomicInteger count = new AtomicInteger(0);
    
    public LoadReviewTask(int reviewId, String review) {
        this.reviewId = reviewId;
        this.review = review;
    }
    
    @Override
    public void run() {
        try {
            Review review = new Review(this.reviewId, this.review);
            ReviewIndex.addReview(review);
            TokenStructure.getInstance().addReview(reviewId, review.getTokens());
            int c = count.incrementAndGet();
            if(c == Configuration.getPropertyInteger("sample.data.limit")-1) {
                System.out.println("All Reviews Loaded as structure.");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
