package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Injector;
import constants.Platform;
import org.example.config.ConfigLoader;
import pages.implementations.android.AndroidSettingsPage;
import pages.implementations.ios.IOSSettingsPage;
import pages.interfaces.SettingsPage;

public class PageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SettingsPage.class).toProvider(SettingsPageProvider.class);
    }

    private static class SettingsPageProvider implements Provider<SettingsPage> {
        @Inject
        private Injector injector;
        @Inject
        private ConfigLoader configLoader;
        @Override
        public SettingsPage get() {
            Platform platform = Platform.from(configLoader.getPlatformName());
            return switch (platform) {
                case ANDROID -> injector.getInstance(AndroidSettingsPage.class);
                case IOS -> injector.getInstance(IOSSettingsPage.class);
            };
        }
    }
}