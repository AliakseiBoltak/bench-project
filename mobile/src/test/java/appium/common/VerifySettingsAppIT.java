package appium.common;

import appium.BaseMobileTest;
import com.google.inject.Inject;
import factory.DriverProvider;
import io.qameta.allure.Allure;
import config.MobileConfigLoader;
import org.testng.annotations.Test;
import steps.SettingsSteps;
import actions.DeviceActions;

import static org.testng.Assert.assertTrue;

class VerifySettingsAppIT extends BaseMobileTest {

    private final SettingsSteps settingsSteps;
    private final DeviceActions deviceActions;

    @Inject
    public VerifySettingsAppIT(MobileConfigLoader configLoader, DriverProvider driverProvider,
                               SettingsSteps settingsSteps, DeviceActions deviceActions) {
        super(configLoader, driverProvider);
        this.settingsSteps = settingsSteps;
        this.deviceActions = deviceActions;
    }

    @Test(description = "Checks the Settings app launches and is the foreground app")
    void checkSettingsAppLaunchedTest() {
        String settingsAppIdentifier = configLoader.getAppiumAppPackage();
        Allure.step("Checking foreground state of app: " + settingsAppIdentifier);

        boolean inForeground = deviceActions.isAppInForeground(settingsAppIdentifier);
        assertTrue(inForeground, "Settings app was not launched as the foreground app");

        settingsSteps.checkSearchSettingsInputIsVisible();
    }

    @Test(description = "Checks user can swipe to any existing setting in settings menu")
    void checkUserCanSwipeInSettingsMenu() {
        settingsSteps.checkCanSwipeToSettingFromMenu();
    }

}
