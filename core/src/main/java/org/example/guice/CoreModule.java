package org.example.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.example.config.ConfigLoader;
import org.example.config.Env;

import static org.example.constants.Constants.ENV;

public class CoreModule extends AbstractModule {

    private final String env;

    public CoreModule(String env) {
        this.env = env;
    }

    public CoreModule() {
        this.env = ENV;
    }

    @Provides
    @Singleton
    public ConfigLoader provideConfigLoader() {
        return new ConfigLoader(env);
    }

    @Provides
    @Singleton
    Env provideEnv() {
        return Env.getInstance();
    }

}