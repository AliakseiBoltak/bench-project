package pages.implementations.ios;

import com.google.inject.Inject;
import factory.DriverProvider;
import pages.BasePage;
import pages.interfaces.SettingsPage;

public class IOSSettingsPage extends BasePage implements SettingsPage {

    @Inject
    public IOSSettingsPage(DriverProvider driverProvider) {
        super(driverProvider);
    }

    @Override
    public boolean isSearchSettingsVisible() {
        // temporarily returning true for iOS
        return true;
    }

    @Override
    public boolean isAppInForeground(String appIdentifier) {
        return super.isAppInForeground(appIdentifier);
    }
}
