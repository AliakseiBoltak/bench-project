package appium;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.MobileConfigLoader;
import io.qameta.allure.selenide.AllureSelenide;
import com.google.inject.Inject;
import factory.DriverProvider;
import guice.PageModule;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import org.example.guice.CoreModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Guice;
import service.AppiumServiceManager;

@Log4j2
@Guice(modules = {CoreModule.class, PageModule.class})
public abstract class BaseMobileTest {

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
        log.info("Global setup: starting Appium service");
        AppiumServiceManager.startService(configLoader);
        Configuration.timeout = DEFAULT_TIMEOUT;
        Configuration.pollingInterval = DEFAULT_POLLING_INTERVAL;
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    @AfterSuite
    public void globalTeardown() {
        log.info("Global teardown: stopping Appium service");
        AppiumServiceManager.stopService();
    }

    @BeforeMethod
    public void setUp() {
        String startMessage = "Starting mobile session for platform: " + configLoader.getPlatformName()
                + " on device: " + configLoader.getAppiumDeviceName();
        Allure.step(startMessage);
        driverProvider.initDriver();
    }

    @AfterMethod
    public void tearDown() {
        Allure.step("Closing mobile session");
        driverProvider.quitDriver();
    }

}