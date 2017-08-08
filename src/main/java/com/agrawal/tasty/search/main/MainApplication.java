package com.agrawal.tasty.search.main;

import com.agrawal.tasty.search.config.Configuration;
import com.agrawal.tasty.search.reviewloader.ReviewLoader;
import com.agrawal.tasty.search.reviewloader.ReviewLoaderFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
@SpringBootApplication(scanBasePackages = {"com.agrawal.tasty.search.controller", "com.agrawal.tasty.search.service"})
public class MainApplication {

    public static void main(String args[]) {
        try {
            // 0. Load Configurations from 'application.properties'
            Configuration.loadConfig();

            // 1. Load Reviews
            ReviewLoader reviewLoader = ReviewLoaderFactory.getInstance(ReviewLoaderFactory.ReviewLoaderType.MAPPED_BYTE_BUFFER);
            reviewLoader.loadReviews(Configuration.getProperty("review.file"), Configuration.getPropertyInteger("sample.data.limit"));

            // 2. Start REST API
            SpringApplication.run(MainApplication.class, args);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error Occurred: " + ex.getMessage());
            System.exit(-1);
        }
    }

}
