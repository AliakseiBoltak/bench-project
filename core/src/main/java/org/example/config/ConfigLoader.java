package org.example.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigLoader {

    private final Config config;

    public ConfigLoader(String profile) {
        Config envConfig = ConfigFactory.parseResources("env.conf");
        // Fallback to default if profile missing
        if (envConfig.hasPath(profile)) {
            config = envConfig.getConfig(profile).withFallback(envConfig.getConfig("default")).resolve();
        } else {
            config = envConfig.getConfig("default").resolve();
        }
    }

    public String getBaseUrl() {
        return config.getString("baseUrl");
    }

    public Config getRawConfig() {
        return config;
    }
}
