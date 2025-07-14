package factory;

import com.codeborne.selenide.Configuration;

public class WebDriverFactory {

    private static final String DEFAULT_BROWSER = "chrome";

    public static void initBrowser() {
        String browser = System.getProperty("browser", DEFAULT_BROWSER).toLowerCase();

        Configuration.browser = switch (browser) {
            case "firefox" -> "firefox";
            case "edge" -> "edge";
            case "safari" -> "safari";
            default -> DEFAULT_BROWSER;
        };
    }
}