package guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import pages.AndroidSettingsPage;
import pages.IOSSettingsPage;
import pages.SettingsPage;

public class PageObjectProvider {

    private final Injector injector;

    @Inject
    public PageObjectProvider(Injector injector) {
        this.injector = injector;
    }

    public SettingsPage getSettingsPage(String platform) {
        if ("android".equalsIgnoreCase(platform)) {
            return injector.getInstance(AndroidSettingsPage.class);
        } else if ("ios".equalsIgnoreCase(platform)) {
            return injector.getInstance(IOSSettingsPage.class);
        } else {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }
}
