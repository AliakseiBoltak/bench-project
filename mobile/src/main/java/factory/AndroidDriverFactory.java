package factory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.example.config.ConfigLoader;

import java.net.MalformedURLException;
import java.net.URI;

public class AndroidDriverFactory {

    private AndroidDriverFactory() {
    }

    public static AndroidDriver createDriver(ConfigLoader configLoader) {
        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName(configLoader.getAppiumAutomationName())
                .setDeviceName(configLoader.getAppiumDeviceName())
                .setPlatformVersion(configLoader.getAppiumPlatformVersion())
                .setAppPackage(configLoader.getAppiumAppPackage())
                .setAppActivity(configLoader.getAppiumAppActivity());

        try {
            return new AndroidDriver(URI.create(configLoader.getAppiumServerUrl()).toURL(), options);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Appium server URL: " + configLoader.getAppiumServerUrl(), e);
        }
    }
}
