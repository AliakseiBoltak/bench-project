package pages;

import com.google.inject.Inject;
import factory.DriverProvider;
import io.appium.java_client.android.AndroidDriver;

public class AndroidSettingsPage implements SettingsPage
{
    private final DriverProvider driverProvider;

    @Inject
    public AndroidSettingsPage(DriverProvider driverProvider) {
        this.driverProvider = driverProvider;
    }

    @Override
    public String getCurrentAppIdentifier() {
        return ((AndroidDriver) driverProvider.getDriver()).getCurrentPackage();
    }
}
