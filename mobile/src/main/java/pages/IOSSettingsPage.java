package pages;

import com.google.inject.Inject;
import factory.DriverProvider;

public class IOSSettingsPage implements SettingsPage {

    private final DriverProvider driverProvider;

    @Inject
    public IOSSettingsPage(DriverProvider driverProvider) {
        this.driverProvider = driverProvider;
    }

    @Override
    public String getCurrentAppIdentifier() {
        Object response = driverProvider.getDriver().executeScript("mobile: activeAppInfo");
        return response != null ? response.toString() : "";
    }
}
