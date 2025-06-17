package org.lib.environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EnvLoader {
    public static final Map<String, String> ENV_VARS = loadProperties("config.properties");

    public static Map<String, String> loadProperties(String resourceName) {
        Map<String, String> env = new HashMap<>();
        Properties props = new Properties();

        try (InputStream input = EnvLoader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                System.err.println("Could not find " + resourceName + " on classpath.");
                return env;
            }
            props.load(input);
            for (String key : props.stringPropertyNames()) {
                env.put(key, props.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return env;
    }
}