package org.example.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.example.config.ConfigConstants;
import org.example.config.ConfigLoader;

public class CoreModule extends AbstractModule {

    private final String env;

    public CoreModule() {
        this.env = ConfigConstants.ENV;
    }

    @Provides
    @Singleton
    public ConfigLoader provideConfigLoader() {
        return new ConfigLoader();
    }
}