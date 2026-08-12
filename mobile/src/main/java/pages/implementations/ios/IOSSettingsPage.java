package pages.implementations.ios;

import com.google.inject.Inject;
import factory.DriverProvider;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.appmanagement.ApplicationState;
import pages.BasePage;
import pages.interfaces.SettingsPage;

public class IOSSettingsPage extends BasePage implements SettingsPage {

    @Inject
    public IOSSettingsPage(DriverProvider driverProvider) {
        super(driverProvider);
    }

    @Override
    public boolean isAppInForeground(String appIdentifier) {
        InteractsWithApps driver = (InteractsWithApps) driverProvider.getDriver();
        return driver.queryAppState(appIdentifier) == ApplicationState.RUNNING_IN_FOREGROUND;
    }

    @Override
    public boolean isSearchSettingsVisible() {
        // temporarily returning true for iOS
        return true;
    }
}
