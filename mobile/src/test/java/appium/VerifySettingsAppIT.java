package appium;

import com.google.inject.Inject;
import factory.DriverProvider;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.testng.annotations.Test;
import pages.interfaces.SettingsPage;
import actions.DeviceActions;

import static org.testng.Assert.assertTrue;

class VerifySettingsAppIT extends BaseMobileTest {

    private static final Logger LOGGER = LogManager.getLogger(VerifySettingsAppIT.class);
    private final SettingsPage settingsPage;
    private final DeviceActions deviceActions;

    @Inject
    public VerifySettingsAppIT(ConfigLoader configLoader, DriverProvider driverProvider,
                               SettingsPage settingsPage, DeviceActions deviceActions) {
        super(configLoader, driverProvider);
        this.settingsPage = settingsPage;
        this.deviceActions = deviceActions;
    }

    @Test(description = "Checks the Settings app launches and is the foreground app")
    void checkSettingsAppLaunchedTest() {
        String settingsAppIdentifier = configLoader.getAppiumAppPackage();
        LOGGER.info("Checking foreground state of app: {}", settingsAppIdentifier);
        Allure.step("Checking foreground state of app: " + settingsAppIdentifier);

        boolean inForeground = deviceActions.isAppInForeground(settingsAppIdentifier);
        LOGGER.info("App {} in foreground: {}", settingsAppIdentifier, inForeground);

        assertTrue(inForeground, "Settings app was not launched as the foreground app");

        LOGGER.info("Checking if Search Settings element is visible");
        Allure.step("Checking if Search Settings element is visible");

        boolean isSearchSettingsVisible = settingsPage.isSearchSettingsVisible();
        assertTrue(isSearchSettingsVisible, "Search Settings element is not visible on the screen");
    }

    @Test(description = "Checks System settings option is available in Settings app")
    void checkSystemSettingsAreAvailableInSettingsApp() {
        LOGGER.info("Swiping to System settings option");
        Allure.step("Swiping to System settings option");

        settingsPage.swipeToSystemSettings();

        LOGGER.info("Checking if System settings option is visible");
        Allure.step("Checking if System settings option is visible");

        boolean isSystemVisible = settingsPage.isSystemSettingsVisible();
        assertTrue(isSystemVisible, "System settings option is not visible in Settings app");
    }

}
