package appium;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
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
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Guice;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.appmanagement.ApplicationState;

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

    @BeforeSuite
    public void globalSetup() {
        Configuration.timeout = 8000;
        Configuration.pollingInterval = 200;
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
        driverProvider.quitDriver();
    }

    protected boolean isAppInForeground(String appIdentifier) {
        InteractsWithApps appDriver = (InteractsWithApps) driverProvider.getDriver();
        return appDriver.queryAppState(appIdentifier) == ApplicationState.RUNNING_IN_FOREGROUND;
    }
}