package org.example.guice;

import com.google.inject.AbstractModule;
import org.example.dao.UserDataDao;
import org.example.dao.UserDataJsonDao;

public class TestModule extends AbstractModule {

    @Override
    public void configure() {
        bind(UserDataDao.class).to(UserDataJsonDao.class);
    }
}
