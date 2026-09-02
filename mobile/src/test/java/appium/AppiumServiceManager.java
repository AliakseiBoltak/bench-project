package appium;

import config.MobileConfigLoader;
import exceptions.MobileFrameworkException;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Helper responsible for starting and stopping an embedded Appium service for tests.
 *
 * The service is bound to the host/port declared in {@code appium.serverUrl} (env.conf)
 * so that it matches the address {@code factory.DriverProvider} uses to create sessions.
 *
 * Usage:
 * - AppiumServiceManager.startService(configLoader);
 * - AppiumServiceManager.stopService();
 */
@UtilityClass
public final class AppiumServiceManager {

    private static final Logger LOGGER = LogManager.getLogger(AppiumServiceManager.class);
    private static AppiumDriverLocalService service;

    public static synchronized void startService(MobileConfigLoader configLoader) {
        if (service != null && service.isRunning()) {
            LOGGER.info("Appium service already running at: {}", service.getUrl());
            return;
        }

        try {
            URI serverUri = new URI(configLoader.getAppiumServerUrl());
            String host = serverUri.getHost();
            int port = serverUri.getPort();

            AppiumServiceBuilder builder = new AppiumServiceBuilder()
                    .withIPAddress(host)
                    .usingPort(port);

            String appiumJs = System.getenv("APPIUM_JS");
            String nodePath = System.getenv("NODE_PATH");
            if (appiumJs != null && !appiumJs.isEmpty()) {
                builder.withAppiumJS(new File(appiumJs));
            }
            if (nodePath != null && !nodePath.isEmpty()) {
                builder.usingDriverExecutable(new File(nodePath));
            }

            service = AppiumDriverLocalService.buildService(builder);
            service.start();

            LOGGER.info("Started Appium service at: {}", service.getUrl());
        } catch (URISyntaxException e) {
            throw new MobileFrameworkException("Invalid appium.serverUrl in configuration: "
                    + configLoader.getAppiumServerUrl(), configLoader.getPlatformName(), e);
        } catch (Exception e) {
            LOGGER.error("Failed to start Appium service", e);
            throw new RuntimeException("Could not start Appium service", e);
        }
    }

    public static synchronized void stopService() {
        if (service == null) {
            LOGGER.info("Appium service was not started by this manager.");
            return;
        }

        try {
            if (service.isRunning()) {
                LOGGER.info("Stopping Appium service at: {}", service.getUrl());
                service.stop();
            }
        } catch (Exception e) {
            LOGGER.warn("Error while stopping Appium service", e);
        } finally {
            service = null;
        }
    }

    public static synchronized boolean isServiceRunning() {
        return service != null && service.isRunning();
    }
}
