package org.example.config;

import lombok.ToString;

/**
 * Singleton environment class.
 */
@ToString
public final class Env {

    private static final Env INSTANCE = new Env(System.getProperty("env") != null && !System.getProperty("env").isEmpty()
            ? System.getProperty("env") : "default");
    private final String env;

    private Env(String env) {
        this.env = env;
    }

    public static Env getInstance() {
        return INSTANCE;
    }

    public String value() {
        return env;
    }
}