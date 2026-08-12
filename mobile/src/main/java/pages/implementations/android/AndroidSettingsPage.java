package pages.implementations.android;

import com.google.inject.Inject;
import factory.DriverProvider;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import pages.BasePage;
import pages.interfaces.SettingsPage;

public class AndroidSettingsPage extends BasePage implements SettingsPage {

    private final By searchSettingsTitle = By.id("com.android.settings:id/search_bar_title");

    @Inject
    public AndroidSettingsPage(DriverProvider driverProvider) {
        super(driverProvider);
    }

    @Override
    public boolean isAppInForeground(String appIdentifier) {
        InteractsWithApps driver = (InteractsWithApps) driverProvider.getDriver();
        return driver.queryAppState(appIdentifier) == ApplicationState.RUNNING_IN_FOREGROUND;
    }

    @Override
    public boolean isSearchSettingsVisible() {
        return isElementVisible(searchSettingsTitle);
    }
}
