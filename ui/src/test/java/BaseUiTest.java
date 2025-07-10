import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.logging.Level;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BaseUiTest {

    protected static final String DEFAULT_BROWSER = "chrome";
    protected static final String BASE_URL = "https://github.com/";
    protected static final int WAIT_FOR_ELEMENT_MILLISECONDS_TIMEOUT = 15000; // 15 seconds

    @BeforeClass
    public void setUpAllureSelenide() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .enableLogs(LogType.BROWSER, Level.ALL)
                .includeSelenideSteps(true));
    }

    @BeforeMethod
    public void openBrowser() {
        Configuration.browser = DEFAULT_BROWSER;
        Configuration.timeout = WAIT_FOR_ELEMENT_MILLISECONDS_TIMEOUT;
        Allure.step("Open BASE URL: " + BASE_URL + " in browser: " + DEFAULT_BROWSER);
        open(BASE_URL);
        getWebDriver().manage().window().maximize();
    }

    @AfterMethod
    public void closeBrowser() {
        Allure.step("Close browser: " + DEFAULT_BROWSER);
        getWebDriver().quit();
    }
}
