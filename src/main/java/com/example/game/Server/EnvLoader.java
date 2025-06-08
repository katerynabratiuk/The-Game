package com.example.game.Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    // temporary solution for loading .env file
    public static Map<String, String> loadEnv(String filepath) {
        Map<String, String> env = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    env.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return env;
    }
}