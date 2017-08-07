package com.agrawal.tasty.search.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
        
        try(BufferedReader reader = new BufferedReader(new FileReader(reviewFile))) {
            String line;
            while((line = reader.readLine()) != null) {
                if(line.contains("review/summary") || line.contains("review/text")) {
                    String lineTokens[] = line.substring(line.indexOf(":")+1).split("\\W+");
                    tokens.addAll(Arrays.asList(lineTokens));
                }
            }
        }
        
        File file = new File("./query.txt");
        if(!file.exists()) file.createNewFile();
        
        System.out.println("Output File: " + file.getAbsolutePath());
        
        Random random = new Random();
        List<String> tokenList = new ArrayList<>(tokens);
        try (FileWriter writer = new FileWriter(file, true)) {
            int queryLimit = 5000;
            while(queryLimit-- > 0) {
                Collections.shuffle(tokenList);
                List<String> queryTokens = tokenList.subList(0, 1+random.nextInt(9));
                StringBuilder builder = new StringBuilder("http://localhost:8080/tasty-search/search?query=");
                for(String queryToken : queryTokens) {
                    builder.append(queryToken).append("%20");
                }
                writer.write(builder.toString() + "\n");
            }
        }
        
    }
    
}
