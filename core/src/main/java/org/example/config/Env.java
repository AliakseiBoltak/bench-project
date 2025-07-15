package org.example.config;

public final class Env {

    private static final Env INSTANCE = new Env();
    private final String env;

    private Env() {
        this.env = resolveEnv();
    }

    public static Env getInstance() {
        return INSTANCE;
    }

    public String getEnv() {
        return env;
    }

    private String resolveEnv() {
        String sysProp = System.getProperty("env");
        if (sysProp != null && !sysProp.isEmpty()) {
            return sysProp;
        }
        return "default";
    }

}