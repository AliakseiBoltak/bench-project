package org.example.constants;

public class Constants {

    private Constants() {
    }

    public static final String ENV = System.getProperty("env") != null && !System.getProperty("env").isEmpty()
            ? System.getProperty("env") : "default";
}
