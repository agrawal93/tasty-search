package com.agrawal.tasty.search.service;

import com.agrawal.tasty.search.model.Review;
import com.agrawal.tasty.search.model.ReviewQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Dispatcher - Polls for new reviews in a queue and passes them to the ExecutorService.
 * 
 * @author I322819
 */
public class DispatcherService implements Runnable {

    private final String file;
    private final ExecutorService executorService;
    
    public DispatcherService(String file, int maxThreads) {
        this.file = file;
        this.executorService = Executors.newFixedThreadPool(maxThreads);
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Dispatcher Service - STARTED.");
            while(true) {
                Review review = ReviewQueue.dequeue();
                executorService.submit(new LoadReviewTask(file, review));
                Thread.sleep(10);
            }
        } catch(InterruptedException ex) {
            System.out.println("Dispatcher Service - STOPPED.");
        } finally {
            if(executorService != null) {
                executorService.shutdown();
            }
        }
    }
    
}
