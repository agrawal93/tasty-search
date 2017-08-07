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
import java.util.Random;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
@SpringBootApplication(scanBasePackages = {"com.agrawal.tasty.search.controller", "com.agrawal.tasty.search.service"})
public class MainApplication {

    private static final int THREADS = 100;
    private static final int SAMPLED_DATA_LIMIT = 100001;

    public static void main(String args[]) {
        try {
            // 0. Process inputs/arguments
            File reviewFile = new File("./foods.txt");

            // 1. Start the dispatcher thread
            Thread dispatcherThread = new Thread(new DispatcherService(reviewFile.getAbsolutePath(), THREADS));
            dispatcherThread.start();

            // 2. Initialize before loading the API
            init(new FileInputStream(reviewFile));

            SpringApplication.run(MainApplication.class, args);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error Occurred: " + ex.getMessage());
            System.exit(-1);
        }
    }

    private static void init(InputStream inputStream) throws Exception {
        // 1. Index the file for reviews
        indexAndSampleReviews(inputStream, SAMPLED_DATA_LIMIT);

        // 2. Sample Reviews Randomly
//        List<Review> reviews = IndexedReviews.sampledReviews();
//
//        // 3. Process Reviews and generate Trie
//        for (Review review : reviews) {
//            ReviewQueue.enqueue(review);
//        }
    }

    private static void indexAndSampleReviews(InputStream inputStream, int K) throws Exception {
        if (inputStream == null) {
            throw new Exception("Invalid Input Stream.");
        }

//        final AtomicLong byteCounter = new AtomicLong(0);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            Review currentReview = null;
            int reviewCount = 1;
            boolean consider = false;
            Random random = new Random();
            String lines[] = null;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.startsWith("product/productId")) {
                    if(currentReview != null && lines != null) {
                        currentReview.setReview(lines);
                        IndexedReviews.addReview(currentReview);
                        ReviewQueue.enqueue(currentReview);
                        if(--K <= 0) break;
                    }
                    consider = random.nextBoolean();
                    if(consider) {
                        currentReview = new Review(reviewCount++, 0);
                        lines = new String[8];
                        lines[0] = line;
                    } else {
                        currentReview = null;
                        lines = null;
                    }
                    continue;
                }
                
                if(consider) {
                    if(line.startsWith("review/userId")) lines[1] = line;
                    if(line.startsWith("review/profileName")) lines[2] = line;
                    if(line.startsWith("review/helpfulness")) lines[3] = line;
                    if(line.startsWith("review/score")) lines[4] = line;
                    if(line.startsWith("review/time")) lines[5] = line;
                    if(line.startsWith("review/summary")) lines[6] = line;
                    if(line.startsWith("review/text")) lines[7] = line;
                }
            }
//            AtomicInteger lineCounter = new AtomicInteger(0);
//            reader.lines().forEach(line -> {
//                if (line.contains("product/productId:")) {
//                    lineNo.set(1);
//                    IndexedReviews.addReview(new Review(lineCounter.incrementAndGet(), byteCounter.get()));
//                }
////                byteCounter.addAndGet(line.length() + 1);
//            });
        } catch (IOException ex) {
            throw new Exception(ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
            }
        }
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp");
        resolver.setSuffix(".jsp");
        return resolver;
    }

}
