package appium;

import com.google.inject.Inject;
import factory.DriverProvider;
import guice.PageModule;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;

@Guice(modules = {CoreModule.class, PageModule.class})
public abstract class BaseMobileTest {

    private static final Logger LOGGER = LogManager.getLogger(BaseMobileTest.class);

    protected final ConfigLoader configLoader;
    protected final DriverProvider driverProvider;

    @Inject
    public BaseMobileTest(ConfigLoader configLoader, DriverProvider driverProvider) {
        this.configLoader = configLoader;
        this.driverProvider = driverProvider;
    }

    @BeforeMethod
    public void setUp() {
        String startMessage = "Starting mobile session for platform: " + configLoader.getPlatformName()
                + " on device: " + configLoader.getAppiumDeviceName();
        LOGGER.info(startMessage);
        Allure.step(startMessage);
        driverProvider.initDriver();
    }

    @AfterMethod
    public void tearDown() {
        LOGGER.info("Closing mobile session");
        Allure.step("Closing mobile session");
        driverProvider.quitDriver();
    }
}