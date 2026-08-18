package guice;

import com.google.inject.AbstractModule;
import pages.interfaces.SettingsPage;
import pages.interfaces.NotificationsPage;

public class PageModule extends AbstractModule {

    @Override
    protected void configure() {
        bindPage(SettingsPage.class);
        bindPage(NotificationsPage.class);
    }

    private <T> void bindPage(Class<T> interfaceType) {
        bind(interfaceType).toProvider(new DynamicPageProvider<>(interfaceType));
    }
}