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
    private String productId;
    private String userId;
    private String profileName;
    private String helpfulness;
    private String score;
    private String time;
    private String summary;
    private String text;
    
    private int queryScore;
    private double reviewScore;

    public Review(int id, String review) {
        this.id = id;
        String reviewLines[] = review.split("\\n");
        for(String line : reviewLines) {
            String value = line.substring(line.indexOf(":")+1).trim();
            if(line.startsWith("product/productId")) this.productId = value;
            if(line.startsWith("review/userId")) this.userId = value;
            if(line.startsWith("review/profileName")) this.profileName = value;
            if(line.startsWith("review/helpfulness")) this.helpfulness = value;
            if(line.startsWith("review/score")) {
                this.score = value;
                this.reviewScore = Double.parseDouble(value);
            }
            if(line.startsWith("review/time")) this.time = value;
            if(line.startsWith("review/summary")) this.summary = value;
            if(line.startsWith("review/text")) this.text = value;
        }
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

    public void resetQueryScore() {
        this.queryScore = 0;
    }

    public void queryHit() {
        this.queryScore++;
    }

    public String getReview() {
        StringBuilder builder = new StringBuilder();
        builder.append(productId).append("\n");
        builder.append(userId).append("\n");
        builder.append(profileName).append("\n");
        builder.append(helpfulness).append("\n");
        builder.append(score).append("\n");
        builder.append(time).append("\n");
        builder.append(summary).append("\n");
        builder.append(text);
        return builder.toString();
    }
    
    public Set<String> getTokens() {
        Set<String> tokens = new HashSet<>();
        tokens.addAll(Arrays.asList(this.summary.split("\\W+")));
        tokens.addAll(Arrays.asList(this.text.split("\\W+")));
        return tokens;
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
