package com.agrawal.tasty.search.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class ReviewQueue {

    private static final BlockingQueue<Review> queue = new LinkedBlockingQueue<>();

    public static void enqueue(Review review) throws InterruptedException {
        queue.put(review);
    }

    public static Review dequeue() throws InterruptedException {
        return queue.take();
    }

    public static void clear() {
        queue.clear();
    }

}
