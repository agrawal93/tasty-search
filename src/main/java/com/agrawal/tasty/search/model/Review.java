package com.agrawal.tasty.search.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class Review implements Comparable<Review> {

    private final int id;
    private final long reviewStartOffset;
    private String[] review;
    private double reviewScore;
    private int queryScore = 0;

    public Review(int id, long reviewStartOffset) {
        this.id = id;
        this.reviewStartOffset = reviewStartOffset;
    }

    public int getId() {
        return this.id;
    }

    public double getReviewScore() {
        return this.reviewScore;
    }

    public int getQueryScore() {
        return this.queryScore;
    }

    public long getReviewStartOffset() {
        return this.reviewStartOffset;
    }

    public void setReviewScore(double reviewScore) {
        this.reviewScore = reviewScore;
    }

    public void setReview(String... review) {
        this.review = review;
        this.reviewScore = Double.parseDouble(this.review[4].substring(this.review[4].indexOf(":") + 1).trim());
    }

    public void resetQueryScore() {
        this.queryScore = 0;
    }

    public void queryHit() {
        this.queryScore++;
    }

    public String getReview() {
        StringBuilder reviewBuilder = new StringBuilder();
        for (String reviewLine : this.review) {
            reviewBuilder.append(reviewLine).append(System.lineSeparator());
        }
        return reviewBuilder.toString();
    }

    public String[] getTokens() {
        Set<String> tokens = new HashSet<>();
        tokens.addAll(Arrays.asList(review[6].split("\\W+")));
        tokens.addAll(Arrays.asList(review[7].split("\\W+")));
        return tokens.toArray(new String[tokens.size()]);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Review)) {
            return false;
        }
        return this.id == ((Review) o).getId();
    }

    @Override
    public int compareTo(Review that) {
        if (that.queryScore == this.queryScore) {
            int reviewCompare = Double.compare(that.reviewScore, this.reviewScore);
            return reviewCompare == 0 ? 1 : -1;
        }
        return Integer.compare(that.queryScore, this.queryScore);
    }

    @Override
    public String toString() {
        return this.id + "";
    }

}
