package com.agrawal.tasty.search.service;

import com.agrawal.tasty.search.model.IndexedReviews;
import com.agrawal.tasty.search.model.Review;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class ReadReviewAsync implements Runnable {

    private final int reviewId;
    private final String fileName;
    private final long startOffset;

    public ReadReviewAsync(int reviewId, String fileName, long startOffset) {
        this.reviewId = reviewId;
        this.fileName = fileName;
        this.startOffset = startOffset;
    }

    @Override
    public void run() {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(fileName, "r");
            randomAccessFile.seek(startOffset);

            Review review = new Review(reviewId, startOffset);
            String lines[] = new String[8];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = randomAccessFile.readLine();
                if (i == 0) {
                    System.out.println(this.reviewId + ": " + lines[i]);
                }
                if (lines[i] == null) {
                    return;
                }
                lines[i] = lines[i].trim();
            }

            IndexedReviews.addReview(review);
        } catch (IOException ex) {
            System.err.println("Error Occurred: " + ex.getMessage());
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException ex) {
                }
            }
        }
    }

}
