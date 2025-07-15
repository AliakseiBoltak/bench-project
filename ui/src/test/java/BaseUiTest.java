import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.inject.Guice;
import com.google.inject.Injector;
import factory.WebDriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.logging.Level;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public abstract class BaseUiTest {

    protected String baseUrl;
    private static final int WAIT_FOR_ELEMENT_MILLISECONDS_TIMEOUT = 15000;

    @BeforeSuite
    public void setUp() {
        Injector injector = Guice.createInjector(new CoreModule());
        ConfigLoader configLoader = injector.getInstance(ConfigLoader.class);
        baseUrl = configLoader.getBaseUrl();
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .enableLogs(LogType.BROWSER, Level.ALL)
                .includeSelenideSteps(true));
    }

    @BeforeMethod
    public void openBrowser() {
        WebDriverFactory.initBrowser();
        Configuration.timeout = WAIT_FOR_ELEMENT_MILLISECONDS_TIMEOUT;
        Allure.step("Open browser: " + Configuration.browser);
        open(baseUrl);
        getWebDriver().manage().window().maximize();
    }

    @AfterMethod
    public void closeBrowser() {
        Allure.step("Close browser: " + Configuration.browser);
        getWebDriver().quit();
    }
}