package appium.common;

import appium.BaseMobileTest;
import com.google.inject.Inject;
import factory.DriverProvider;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import config.MobileConfigLoader;
import org.testng.annotations.Test;
import pages.interfaces.SettingsPage;
import actions.DeviceActions;

import static org.testng.Assert.assertTrue;

@Log4j2
class VerifySettingsAppIT extends BaseMobileTest {

    private final SettingsPage settingsPage;
    private final DeviceActions deviceActions;

    @Inject
    public VerifySettingsAppIT(MobileConfigLoader configLoader, DriverProvider driverProvider,
                               SettingsPage settingsPage, DeviceActions deviceActions) {
        super(configLoader, driverProvider);
        this.settingsPage = settingsPage;
        this.deviceActions = deviceActions;
    }

    @Test(description = "Checks the Settings app launches and is the foreground app")
    void checkSettingsAppLaunchedTest() {
        String settingsAppIdentifier = configLoader.getAppiumAppPackage();
        Allure.step("Checking foreground state of app: " + settingsAppIdentifier);

        boolean inForeground = deviceActions.isAppInForeground(settingsAppIdentifier);
        log.info("App {} in foreground: {}", settingsAppIdentifier, inForeground);

        assertTrue(inForeground, "Settings app was not launched as the foreground app");

        Allure.step("Checking if Search Settings element is visible");

        boolean isSearchSettingsVisible = settingsPage.isSearchSettingsInputVisible();
        assertTrue(isSearchSettingsVisible, "Search Settings element is not visible on the screen");
    }

    @Test(description = "Checks user can swipe to any existing setting in settings menu")
    void checkUserCanSwipeInSettingsMenu() {
        Allure.step("Swiping in settings menu");

        boolean isElementVisibleAfterSwipe  = settingsPage.canSwipeToSettingFromMenuUntilItIsVisible();
        assertTrue(isElementVisibleAfterSwipe, "Element was not visible after swiping in settings menu");
    }

}
