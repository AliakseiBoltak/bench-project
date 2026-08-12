import com.google.inject.Inject;
import factory.DriverProvider;
import factory.PageObjectProvider;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.testng.annotations.Test;
import pages.SettingsPage;

import static org.testng.Assert.assertTrue;

class OpenSettingsAppTest extends BaseMobileTest {

    private static final Logger LOGGER = LogManager.getLogger(OpenSettingsAppTest.class);

    private final SettingsPage settingsPage;

    @Inject
    public OpenSettingsAppTest(ConfigLoader configLoader, DriverProvider driverProvider,
                               PageObjectProvider pageObjectProvider) {
        super(configLoader, driverProvider);
        this.settingsPage = pageObjectProvider.getSettingsPage(configLoader.getPlatformName());
    }

    @Test(description = "Checks the Settings app launches and is the foreground app")
    void checkSettingsAppLaunchedTest() {
        String settingsAppIdentifier = configLoader.getAppiumAppPackage();
        LOGGER.info("Checking foreground state of app: {}", settingsAppIdentifier);
        Allure.step("Checking foreground state of app: " + settingsAppIdentifier);

        boolean inForeground = settingsPage.isAppInForeground(settingsAppIdentifier);
        LOGGER.info("App {} in foreground: {}", settingsAppIdentifier, inForeground);

        assertTrue(inForeground, "Settings app was not launched as the foreground app");

        LOGGER.info("Checking if Search Settings element is visible");
        Allure.step("Checking if Search Settings element is visible");

        boolean isSearchSettingsVisible = settingsPage.isSearchSettingsVisible();
        assertTrue(isSearchSettingsVisible, "Search Settings element is not visible on the screen");
    }
}
