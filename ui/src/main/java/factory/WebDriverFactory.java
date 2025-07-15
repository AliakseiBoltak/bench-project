package factory;

import com.codeborne.selenide.Configuration;

public class WebDriverFactory {

    private static final String DEFAULT_BROWSER = "chrome";
    private static final String BROWSER = System.getProperty("browser", DEFAULT_BROWSER).toLowerCase();

    public static void initBrowser() {
        Configuration.browser = switch (BROWSER) {
            case "firefox" -> "firefox";
            case "edge" -> "edge";
            case "safari" -> "safari";
            default -> DEFAULT_BROWSER;
        };
    }
}