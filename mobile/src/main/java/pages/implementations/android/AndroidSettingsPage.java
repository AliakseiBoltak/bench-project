package pages.implementations.android;

import com.google.inject.Inject;
import factory.DriverProvider;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import pages.BasePage;
import pages.interfaces.SettingsPage;

public class AndroidSettingsPage extends BasePage implements SettingsPage {

    private static final By SEARCH_SETTINGS_TITLE = By.id("com.android.settings:id/search_bar_title");

    @Inject
    public AndroidSettingsPage(DriverProvider driverProvider) {
        super(driverProvider);
    }

    @Override
    public boolean isAppInForeground(String appIdentifier) {
        InteractsWithApps appDriver = (InteractsWithApps) getDriver();
        return appDriver.queryAppState(appIdentifier) == ApplicationState.RUNNING_IN_FOREGROUND;
    }

    @Override
    public boolean isSearchSettingsVisible() {
        return isElementVisible(SEARCH_SETTINGS_TITLE);
    }
}
