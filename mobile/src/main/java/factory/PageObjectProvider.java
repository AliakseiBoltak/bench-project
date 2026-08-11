package factory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import constants.Platform;
import pages.AndroidSettingsPage;
import pages.IOSSettingsPage;
import pages.SettingsPage;

public class PageObjectProvider {

    private final Injector injector;

    @Inject
    public PageObjectProvider(Injector injector) {
        this.injector = injector;
    }

    public SettingsPage getSettingsPage(String platformName) {
        return switch (Platform.from(platformName)) {
            case ANDROID -> injector.getInstance(AndroidSettingsPage.class);
            case IOS -> injector.getInstance(IOSSettingsPage.class);
        };
    }
}

