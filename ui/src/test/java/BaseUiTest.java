import com.codeborne.selenide.Configuration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BaseUiTest {

    protected static final String DEFAULT_BROWSER = "chrome";
    protected static final String BASE_URL = "https://github.com/";
    protected static final int WAIT_FOR_ELEMENT_MILLISECONDS_TIMEOUT = 20000; // 20 seconds

    @BeforeMethod
    public void openBrowser() {
        Configuration.browser = DEFAULT_BROWSER;
        Configuration.timeout = WAIT_FOR_ELEMENT_MILLISECONDS_TIMEOUT;
        open(BASE_URL);
        getWebDriver().manage().window().maximize();
    }

    @AfterMethod
    public void closeBrowser() {
        getWebDriver().quit();
    }
}
