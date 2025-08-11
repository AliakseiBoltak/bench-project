package hooks;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import context.CucumberTestContext;
import factory.BrowserFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class CucumberPlaywrightHooks {

    private final CucumberTestContext testContext;

    @Inject
    public CucumberPlaywrightHooks(CucumberTestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void beforeScenario() {
        testContext.setPlaywright(Playwright.create());
        testContext.setBrowser(BrowserFactory.initBrowser(testContext.getPlaywright()));
        testContext.setBrowserContext(testContext.getBrowser().newContext());
        testContext.setPage(testContext.getBrowserContext().newPage());
        testContext.getPage().setDefaultTimeout(testContext.getDEFAULT_WAIT_TIMEOUT_MILLISECONDS());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = testContext.getPage().screenshot(new Page.ScreenshotOptions().setFullPage(true));
            scenario.attach(screenshot, "image/png", "Failure Screenshot:");
        }
        testContext.getBrowserContext().close();
        testContext.getBrowser().close();
        testContext.getPlaywright().close();
    }
}
