package pages;

import com.google.inject.Inject;
import factory.DriverProvider;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.appmanagement.ApplicationState;

public class AndroidSettingsPage implements SettingsPage {

    private final DriverProvider driverProvider;

    @Inject
    public AndroidSettingsPage(DriverProvider driverProvider) {
        this.driverProvider = driverProvider;
    }

    @Override
    public boolean isAppInForeground(String appIdentifier) {
        InteractsWithApps driver = (InteractsWithApps) driverProvider.getDriver();
        return driver.queryAppState(appIdentifier) == ApplicationState.RUNNING_IN_FOREGROUND;
    }
}
