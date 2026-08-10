import com.google.inject.Inject;
import guice.PageObjectProvider;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import factory.DriverProvider;
import pages.SettingsPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


class OpenSettingsAppTest extends BaseMobileTest {

    @Inject
    private PageObjectProvider pageObjectProvider;

    private SettingsPage settingsPage;

    @Inject
    public OpenSettingsAppTest(ConfigLoader configLoader, DriverProvider driverProvider) {
        super(configLoader, driverProvider);
    }

    @BeforeMethod(dependsOnMethods = "setUp")
    void initPages() {
        settingsPage = pageObjectProvider.getSettingsPage(configLoader.getPlatformName());
    }

    @Test(description = "Checks the Settings app launches and is the foreground app")
    void checkSettingsAppLaunchedTest() {
        String currentAppIdentifier = settingsPage.getCurrentAppIdentifier();
        Allure.step("Current foreground app identifier: " + currentAppIdentifier);

        assertEquals(currentAppIdentifier, configLoader.getAppiumAppPackage(),
                "Settings app was not launched as the foreground app");
    }
}