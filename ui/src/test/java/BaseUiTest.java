import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.inject.Inject;
import factory.WebDriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import org.example.config.ConfigLoader;
import org.example.constants.UserTypes;
import org.example.guice.CoreModule;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.logging.Level;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

@Guice(modules = {CoreModule.class})
public abstract class BaseUiTest {

    protected final ConfigLoader configLoader;
    protected final String baseUrl;

    @DataProvider
    protected Object[][] allUserTypesWithIncorrectCreds() {
        return Arrays.stream(UserTypes.values())
                .map(type -> new Object[]{type.name().toLowerCase()})
                .toArray(Object[][]::new);
    }

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
        Allure.step("Open browser: " + Configuration.browser);
        open(baseUrl);
        getWebDriver().manage().window().maximize();
    }

    @AfterMethod
    public void closeBrowser() {
        Allure.step("Close browser: " + Configuration.browser);
        getWebDriver().quit();
    }

    @AfterSuite
    public void tearDown() {
        SelenideLogger.removeListener("AllureSelenide");
    }
}