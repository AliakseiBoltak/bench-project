package org.example.guice;

import com.google.inject.AbstractModule;
import org.example.dao.UserDataDao;
import org.example.dao.UserDataJsonDao;

public class TestModule extends AbstractModule {

    @Override
    public void configure() {
        try {
            bind(UserDataDao.class).to(UserDataJsonDao.class);
        } catch (Exception e) {
            addError(e.getMessage());
        }
    }
}
