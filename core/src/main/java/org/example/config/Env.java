package org.example.config;

import lombok.ToString;

/**
 * Singleton environment class.
 * Wraps the environment name, resolves from ConfigConstants.ENV.
 */
@ToString
public final class Env {

    private static final Env INSTANCE = new Env(ConfigConstants.ENV);
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