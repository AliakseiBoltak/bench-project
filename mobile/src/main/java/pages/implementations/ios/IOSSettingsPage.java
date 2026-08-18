package pages.implementations.ios;

import actions.DeviceActions;
import com.google.inject.Inject;
import factory.DriverProvider;
import pages.BasePage;
import pages.interfaces.SettingsPage;

public class IOSSettingsPage extends BasePage implements SettingsPage {

    @Inject
    public IOSSettingsPage(DriverProvider driverProvider, DeviceActions deviceActions) {
        super(driverProvider, deviceActions);
    }

    @Override
    public boolean isSearchSettingsVisible() {
        // Adjust when iOS locators are available.
        return true;
    }

    @Override
    public void swipeToSystemSettings() {
        // Adjust when iOS locators are available..
    }

    @Override
    public boolean isSystemSettingsVisible() {
        // Adjust when iOS locators are available.
        return true;
    }

}
