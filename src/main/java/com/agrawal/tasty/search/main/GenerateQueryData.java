package com.agrawal.tasty.search.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class GenerateQueryData {
    
    public static void main(String args[]) throws Exception {
        File reviewFile = new File("./foods.txt");
        
        Set<String> tokens = new HashSet<>();
        
        try(RandomAccessFile file = new RandomAccessFile(reviewFile, "r")) {
            FileChannel channel = file.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            
            StringBuilder line = new StringBuilder();
            for(int i=0, n=buffer.limit(); i<n; i++) {
                char read = (char) buffer.get();
                line.append(read);
                if(read == '\n') {
                    String currentLine = line.toString().trim();
                    if(currentLine.startsWith("review/summary:") || currentLine.startsWith("review/text:")) {
                        tokens.addAll(Arrays.asList(currentLine.substring(currentLine.indexOf(":") + 1).split("\\W+")));
                    }
                    line = new StringBuilder();
                }
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        
        File file = new File("./query_post.txt");
        if(!file.exists()) file.createNewFile();
        
        System.out.println("Output File: " + file.getAbsolutePath());
        
        Random random = new Random();
        List<String> tokenList = new ArrayList<>(tokens);
        try (FileWriter writer = new FileWriter(file, true)) {
            int queryLimit = 2500;
            StringBuilder builder = new StringBuilder();
            while(queryLimit-- > 0) {
                Collections.shuffle(tokenList);
                List<String> queryTokens = tokenList.subList(0, 1+random.nextInt(9));
                queryTokens.forEach((queryToken) -> {
                    builder.append(queryToken).append(" ");
                });
                builder.append("\n");
            }
            writer.write(builder.toString());
        }
        
    }
    
}
