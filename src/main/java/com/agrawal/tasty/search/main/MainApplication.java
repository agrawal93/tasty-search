package com.agrawal.tasty.search.main;

import com.agrawal.tasty.search.model.IndexedReviews;
import com.agrawal.tasty.search.model.Review;
import com.agrawal.tasty.search.model.ReviewQueue;
import com.agrawal.tasty.search.service.DispatcherService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.io.input.CountingInputStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
@SpringBootApplication( scanBasePackages = { "com.agrawal.tasty.search.controller", "com.agrawal.tasty.search.service" } )
public class MainApplication {
    
    private static final int THREADS = 50;
    private static final int SAMPLED_DATA_LIMIT = 100000;
    
    public static void main(String args[]) {
        try {
            // 0. Process inputs/arguments
            File reviewFile = new File("./foods.txt");
			System.out.println(reviewFile.getAbsolutePath());
			
            // 1. Start the dispatcher thread
            Thread dispatcherThread = new Thread(new DispatcherService(reviewFile.getAbsolutePath(), THREADS));
            dispatcherThread.start();

            // 2. Initialize before loading the API
            init(new FileInputStream(reviewFile));

            SpringApplication.run(MainApplication.class, args);
        } catch(Exception ex) {
            System.err.println("Error Occurred: " + ex.getMessage());
            System.exit(-1);
        }
    }
    
    private static void init(InputStream inputStream) throws Exception {
        // 1. Index the file for reviews
        indexFile(new CountingInputStream(inputStream));
        
        // 2. Sample Reviews Randomly
        List<Review> reviews = IndexedReviews.sampledReviews(SAMPLED_DATA_LIMIT);
        
        // 3. Process Reviews and generate Trie
        for(Review review : reviews) ReviewQueue.enqueue(review);
    }
    
    private static void indexFile(CountingInputStream inputStream) throws Exception {
        if(inputStream == null) {
            throw new Exception("Invalid Input Stream.");
        }
        
        final AtomicLong byteCounter = new AtomicLong(0);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            AtomicInteger lineCounter = new AtomicInteger(0);
            reader.lines().forEach(line -> {
                if(line.contains("product/productId:")) {
                    IndexedReviews.addReview(new Review(lineCounter.incrementAndGet(), byteCounter.get()));
                }
                byteCounter.addAndGet(line.length() + 1);
            });
        } catch(IOException ex) {
            throw new Exception(ex);
        } finally {
            try { inputStream.close(); }
            catch(IOException ex) {  }
        }
    }
    
}
