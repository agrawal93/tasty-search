package com.agrawal.tasty.search.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Hardik Agrawal [ hardik93@ymail.com ]
 */
public class Configuration {
    
    private static final Properties PROPERTIES = new Properties();
    
    public static void loadConfig() {
        try(InputStream stream = Configuration.class.getResourceAsStream("/application.properties")) {
            PROPERTIES.load(stream);
        } catch(IOException ex) {
            PROPERTIES.put("load.threads", 10);
            PROPERTIES.put("sample.data.limit", 100000);
            PROPERTIES.put("review.file", "./foods.txt");
        }
    }
    
    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
    
    public static int getPropertyInteger(String key) {
        return Integer.parseInt(PROPERTIES.getProperty(key));
    }
    
}
