package factory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public class BrowserFactory {

    private static final String BROWSER = System.getProperty("browser",
            BrowserTypeEnum.CHROMIUM.name()).toLowerCase();
    private static final String HEADLESS = System.getProperty("headless", "true").toLowerCase();

    public static Browser initBrowser(Playwright playwright) {
        BrowserOptions options = getBrowserOptions();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(options.headless);
        return switch (options.browserType) {
            case CHROMIUM -> playwright.chromium().launch(launchOptions);
            case FIREFOX -> playwright.firefox().launch(launchOptions);
            case WEBKIT -> playwright.webkit().launch(launchOptions);
        };
    }

    private static BrowserOptions getBrowserOptions() {
        BrowserTypeEnum browserType;
        boolean headless;
        switch (BROWSER.trim().toLowerCase()) {
            case "firefox" -> browserType = BrowserTypeEnum.FIREFOX;
            case "webkit" -> browserType = BrowserTypeEnum.WEBKIT;
            default -> browserType = BrowserTypeEnum.CHROMIUM;
        }
        headless = Boolean.parseBoolean(HEADLESS.trim().toLowerCase());
        return new BrowserOptions(browserType, headless);
    }

    private static class BrowserOptions {
        public BrowserTypeEnum browserType;
        public boolean headless;

        public BrowserOptions(BrowserTypeEnum browserType, boolean headless) {
            this.browserType = browserType;
            this.headless = headless;
        }
    }

    public enum BrowserTypeEnum {
        CHROMIUM,
        FIREFOX,
        WEBKIT
    }
}