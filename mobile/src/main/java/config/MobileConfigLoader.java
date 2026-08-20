package config;

import org.example.config.ConfigLoader;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class MobileConfigLoader extends ConfigLoader {

    private final Config platformConfig;
    private static final String DEFAULT_PLATFORM = "platform-default";

    public MobileConfigLoader() {
        super();

        String platform = System.getProperty("platform", DEFAULT_PLATFORM);

        Config fullConfig = ConfigFactory.parseResources("env.conf");

        if (fullConfig.hasPath(platform)) {
            // Fallback to default if profile missing
            this.platformConfig = fullConfig.getConfig(platform)
                    .withFallback(fullConfig.getConfig(DEFAULT_PLATFORM))
                    .resolve();
        } else {
            this.platformConfig = fullConfig.getConfig(DEFAULT_PLATFORM).resolve();
        }
    }

    public String getAppiumServerUrl() {
        return platformConfig.getString("appium.serverUrl");
    }

    public String getAppiumPlatformVersion() {
        return platformConfig.getString("appium.platformVersion");
    }

    public String getAppiumDeviceName() {
        return platformConfig.getString("appium.deviceName");
    }

    public String getAppiumAutomationName() {
        return platformConfig.getString("appium.automationName");
    }

    public String getPlatformName() {
        return platformConfig.getString("appium.platformName");
    }

    public String getAppiumAppPackage() {
        return platformConfig.getString("appium.appPackage");
    }

    public String getAppiumAppActivity() {
        return platformConfig.getString("appium.appActivity");
    }

    public String getAppiumApp() {
        return platformConfig.hasPath("appium.app") ? platformConfig.getString("appium.app") : null;
    }

}