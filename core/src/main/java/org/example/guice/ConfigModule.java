package org.example.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.example.config.ConfigLoader;

import static org.example.constants.Constants.ENV;

public class ConfigModule extends AbstractModule {

    private final String env;

    public ConfigModule(String env) {
        this.env = env;
    }

    public ConfigModule() {
        this.env = ENV;
    }

    @Provides
    @Singleton
    public ConfigLoader provideConfigLoader() {
        return new ConfigLoader(env);
    }

}