package org.example.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigLoader {

    private final Config config;

    public ConfigLoader() {
        String profile = Env.getInstance().value();
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

    public String getDbUrl() {
        return config.getString("db.url");
    }

    public String getDbUsername() {
        return config.getString("db.username");
    }

    public String getDbPassword() {
        return config.getString("db.password");
    }

}
