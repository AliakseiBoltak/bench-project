package factory;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrowserFactory {

    private static final String DEFAULT_BROWSER = "chrome";
    private static final String BROWSER = System.getProperty("browser", DEFAULT_BROWSER).toLowerCase();
    private static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "true"));

    public static void initBrowser() {
        Configuration.browser = switch (BROWSER) {
            case "firefox" -> "firefox";
            case "edge" -> "edge";
            case "safari" -> "safari";
            default -> DEFAULT_BROWSER;
        };
        Configuration.headless = HEADLESS;
        if (Configuration.browser.equals(DEFAULT_BROWSER)) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            if (Configuration.headless) {
                options.addArguments("--headless=new");
            }
        }
    }
}