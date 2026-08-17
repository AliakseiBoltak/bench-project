package factory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.codeborne.selenide.WebDriverRunner;
import constants.Platform;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

@Singleton
public class DriverProvider {

    private static final Logger LOGGER = LogManager.getLogger(DriverProvider.class);
    private final ConfigLoader configLoader;
    private final ThreadLocal<AppiumDriver> driverThreadLocal = new ThreadLocal<>();

    @Inject
    public DriverProvider(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    public void initDriver() {
        if (driverThreadLocal.get() != null) {
            LOGGER.warn("Driver is already initialized for the current thread. Skipping initialization.");
            return;
        }

        URL serverUrl = appiumServerUrl();
        Platform platform = Platform.from(configLoader.getPlatformName());
        LOGGER.info("Initializing {} driver against Appium server {}", platform, serverUrl);

        AppiumDriver driver = switch (platform) {
            case ANDROID -> new AndroidDriver(serverUrl, androidOptions());
            case IOS -> new IOSDriver(serverUrl, iosOptions());
        };

        driverThreadLocal.set(driver);
        // Register the driver on Selenide's own ThreadLocal so that the static
        // Selenide.$()/$$() API (used by BasePage and page objects) resolves it
        // without going through Selenide's Configuration.browser bootstrap.
        WebDriverRunner.setWebDriver(driver);
        LOGGER.info("{} driver initialized, session id: {}", platform, driver.getSessionId());
    }

    public AppiumDriver getDriver() {
        AppiumDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("Driver is not initialized for the current thread!");
        }
        return driver;
    }

    public void quitDriver() {
        AppiumDriver driver = driverThreadLocal.get();
        if (driver != null) {
            LOGGER.info("Quitting driver, session id: {}", driver.getSessionId());
            // closeWebDriver() quits the driver and clears Selenide's ThreadLocal reference.
            WebDriverRunner.closeWebDriver();
            driverThreadLocal.remove();
        }
    }

    private URL appiumServerUrl() {
        String serverUrl = configLoader.getAppiumServerUrl();
        try {
            return URI.create(serverUrl).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Appium server URL: " + serverUrl, e);
        }
    }

    private UiAutomator2Options androidOptions() {
        return new UiAutomator2Options()
                .setAutomationName(configLoader.getAppiumAutomationName())
                .setDeviceName(configLoader.getAppiumDeviceName())
                .setPlatformVersion(configLoader.getAppiumPlatformVersion())
                .setAppPackage(configLoader.getAppiumAppPackage())
                .setAppActivity(configLoader.getAppiumAppActivity());
    }

    private XCUITestOptions iosOptions() {
        return new XCUITestOptions()
                .setAutomationName(configLoader.getAppiumAutomationName())
                .setDeviceName(configLoader.getAppiumDeviceName())
                .setPlatformVersion(configLoader.getAppiumPlatformVersion())
                .setBundleId(configLoader.getAppiumAppPackage());
    }
}
