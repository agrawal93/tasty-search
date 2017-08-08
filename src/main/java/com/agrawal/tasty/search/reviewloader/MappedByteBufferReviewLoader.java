package com.agrawal.tasty.search.reviewloader;

import com.agrawal.tasty.search.config.Configuration;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class MappedByteBufferReviewLoader implements ReviewLoader {

    @Override
    public void loadReviews(String fileName, int limit) {
        ExecutorService executor = Executors.newFixedThreadPool(Configuration.getPropertyInteger("load.threads"));
        try(RandomAccessFile file = new RandomAccessFile(fileName, "r")) {
            FileChannel channel = file.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            
            int reviewCount = 0;
            StringBuilder review = new StringBuilder();
            
            final Random random = new Random();
            
            StringBuilder line = new StringBuilder();
            for(int i=0, n=buffer.limit(); i<n; i++) {
                char read = (char) buffer.get();
                line.append(read);
                if(read == '\n') {
                    if(reviewCount < limit && review.length() > 0 && line.toString().trim().startsWith("product/productId:")) {
                        if(random.nextBoolean()) {
                            executor.submit(new LoadReviewTask(++reviewCount, review.toString()));
                            if(reviewCount >= limit) break;
                        }
                        review = new StringBuilder(line);
                    } else {
                        review.append(line);
                    }
                    line = new StringBuilder();
                }
            }
            executor.shutdown();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
 
    public static void main(String args[]) {
        Configuration.loadConfig();
        MappedByteBufferReviewLoader rl = new MappedByteBufferReviewLoader();
        rl.loadReviews("./foods.txt", 10);
    }
    
}
