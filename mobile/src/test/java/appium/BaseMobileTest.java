package appium;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.MobileConfigLoader;
import io.qameta.allure.selenide.AllureSelenide;
import com.google.inject.Inject;
import factory.DriverProvider;
import guice.PageModule;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.guice.CoreModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Guice;

@Guice(modules = {CoreModule.class, PageModule.class})
public abstract class BaseMobileTest {

    private static final Logger LOGGER = LogManager.getLogger(BaseMobileTest.class);

    protected final MobileConfigLoader configLoader;
    protected final DriverProvider driverProvider;
    protected static final int DEFAULT_TIMEOUT = 8000; // in milliseconds
    protected static final int DEFAULT_POLLING_INTERVAL = 200; // in milliseconds

    @Inject
    public BaseMobileTest(MobileConfigLoader configLoader, DriverProvider driverProvider) {
        this.configLoader = configLoader;
        this.driverProvider = driverProvider;
    }

    @BeforeSuite
    public void globalSetup() {
        Configuration.timeout = DEFAULT_TIMEOUT;
        Configuration.pollingInterval = DEFAULT_POLLING_INTERVAL;
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
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