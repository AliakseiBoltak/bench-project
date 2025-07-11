import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import org.example.config.ConfigLoader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.logging.Level;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.example.constants.Constants.ENV;

public abstract class BaseUiTest {

    protected String browser;
    protected String baseUrl;
    protected static final int WAIT_FOR_ELEMENT_MILLISECONDS_TIMEOUT = 15000;

    @BeforeClass
    public void setUp() {
        ConfigLoader loader = new ConfigLoader(ENV);
        browser = loader.getBrowser();
        baseUrl = loader.getBaseUrl();

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .enableLogs(LogType.BROWSER, Level.ALL)
                .includeSelenideSteps(true));
    }

    @BeforeMethod
    public void openBrowser() {
        Configuration.browser = browser;
        Configuration.timeout = WAIT_FOR_ELEMENT_MILLISECONDS_TIMEOUT;
        Allure.step("Open browser: " + browser);
        open(baseUrl);
        getWebDriver().manage().window().maximize();
    }

    @AfterMethod
    public void closeBrowser() {
        Allure.step("Close browser: " + browser);
        getWebDriver().quit();
    }
}
