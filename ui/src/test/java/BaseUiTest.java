import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.inject.Inject;
import factory.WebDriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Guice;

import java.util.logging.Level;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static constants.Constants.WAIT_FOR_ELEMENT_MILLISECONDS_TIMEOUT;

@Guice(modules = {CoreModule.class})
public abstract class BaseUiTest {

    protected final ConfigLoader configLoader;
    protected final String baseUrl;

    @Inject
    public BaseUiTest(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.baseUrl = configLoader.getBaseUrl();
    }

    @BeforeSuite
    public void setUp() {
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