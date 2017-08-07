package com.agrawal.tasty.search.service;

import com.agrawal.tasty.search.model.Review;
import com.agrawal.tasty.search.model.ReviewTrie;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * Loads a review as a trie.
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class LoadReviewTask implements Runnable {

    private static AtomicInteger counter = new AtomicInteger(0);

    private final String fileName;
    private final Review review;

    public LoadReviewTask(String fileName, Review review) {
        this.fileName = fileName;
        this.review = review;
    }

    @Override
    public void run() {
        ReviewTrie.addReview(this.review.getId(), this.review.getTokens());
        int count = counter.incrementAndGet();
        if (count % 10000 == 0) {
            System.out.println("Reviews Loaded So Far: " + count + ", " + System.currentTimeMillis() + " ms.");
        }
        
        
//        System.out.println("Loading Review [" + review.getId() + "]");
/*        long t1 = System.currentTimeMillis();
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(fileName, "r");
            randomAccessFile.seek(review.getReviewStartOffset());

            String lines[] = new String[8];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = randomAccessFile.readLine();
//                if(i == 0) System.out.println(this.review.getId() + ": " + lines[i]);
                if (lines[i] == null) {
                    return;
                }
                lines[i] = lines[i].trim();
            }

            review.setReview(lines);

            int count = counter.incrementAndGet();
            
            long t2 = System.currentTimeMillis();
            
            fileLoad.addAndGet(t2-t1);
            
            t1 = System.currentTimeMillis();
            ReviewTrie.addReview(this.review.getId(), this.review.getTokens());
            t2 = System.currentTimeMillis();
            
            trieLoad.addAndGet(t2-t1);
            
            if (count % 1000 == 0) {
                System.out.println("Reviews Loaded So Far: " + count + ", " + (fileLoad.get()/(double) count) + " ms, " + (trieLoad.get()/(double) count) + " ms.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error Occurred: " + ex.getMessage());
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException ex) {
                }
            }
        }*/
    }

}
