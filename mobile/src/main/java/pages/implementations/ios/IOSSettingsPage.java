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
        // temporary return true for iOS
        return true;
    }

}
