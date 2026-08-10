package factory;

import com.google.inject.Singleton;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.example.config.ConfigLoader;

import java.net.MalformedURLException;
import java.net.URI;

@Singleton
public class DriverProvider {

    private final ThreadLocal<AppiumDriver> driverThreadLocal = new ThreadLocal<>();

    public void initDriver(ConfigLoader configLoader) {
        String platform = configLoader.getPlatformName().toLowerCase();
        try {
            if ("android".equals(platform)) {
                UiAutomator2Options options = new UiAutomator2Options()
                        .setAutomationName(configLoader.getAppiumAutomationName())
                        .setDeviceName(configLoader.getAppiumDeviceName())
                        .setPlatformVersion(configLoader.getAppiumPlatformVersion())
                        .setAppPackage(configLoader.getAppiumAppPackage())
                        .setAppActivity(configLoader.getAppiumAppActivity());

                driverThreadLocal.set(new AndroidDriver(URI.create(configLoader.getAppiumServerUrl()).toURL(), options));

            } else if ("ios".equals(platform)) {
                XCUITestOptions options = new XCUITestOptions()
                        .setAutomationName("XCUITest")
                        .setDeviceName(configLoader.getAppiumDeviceName())
                        .setPlatformVersion(configLoader.getAppiumPlatformVersion())
                        .setBundleId(configLoader.getAppiumAppPackage());

                driverThreadLocal.set(new IOSDriver(URI.create(configLoader.getAppiumServerUrl()).toURL(), options));
            } else {
                throw new IllegalArgumentException("Unsupported platform specified in config: " + platform);
            }
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Appium server URL: " + configLoader.getAppiumServerUrl(), e);
        }
    }

    public AppiumDriver getDriver() {
        AppiumDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("Driver is not initialized for the current thread!");
        }
        return driver;
    }

    public void quitDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }
}
