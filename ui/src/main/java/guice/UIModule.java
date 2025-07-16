package guice;

import com.google.inject.AbstractModule;
import dao.UserDataDao;
import dao.UserDataJsonDao;

public class UIModule extends AbstractModule {

    @Override
    protected void configure() {
        // Bind a service to its implementation
        bind(UserDataDao.class).to(UserDataJsonDao.class);
    }

}