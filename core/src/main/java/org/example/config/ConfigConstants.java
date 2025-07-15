package org.example.config;

public class ConfigConstants {

    private ConfigConstants() {
    }

    public static final String ENV = System.getProperty("env") != null && !System.getProperty("env").isEmpty()
            ? System.getProperty("env") : "default";
}
