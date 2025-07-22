package org.example.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.example.config.ConfigLoader;
import org.example.dao.UserDataDao;
import org.example.dao.UserDataJsonDao;
import org.example.loader.DataLoader;
import org.example.loader.JSONDataLoader;
import org.example.service.UserDataJsonService;
import org.example.service.UserDataService;

public class CoreModule extends AbstractModule {

    @Provides
    @Singleton
    public ConfigLoader provideConfigLoader() {
        return new ConfigLoader();
    }

    @Override
    protected void configure() {
        // Bind a service to its implementation
        bind(DataLoader.class).to(JSONDataLoader.class);
        bind(UserDataDao.class).to(UserDataJsonDao.class);
        bind(UserDataService.class).to(UserDataJsonService.class);
    }

}