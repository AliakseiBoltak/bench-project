package hooks;

import com.google.inject.Inject;
import com.microsoft.playwright.Playwright;
import context.CucumberTestContext;
import factory.BrowserFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class CucumberPlaywrightHooks {

    private final CucumberTestContext testContext;

    @Inject
    public CucumberPlaywrightHooks(CucumberTestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void setUp() {
        testContext.setPlaywright(Playwright.create());
        testContext.setBrowser(BrowserFactory.initBrowser(testContext.getPlaywright()));
        testContext.setBrowserContext(testContext.getBrowser().newContext());
        testContext.setPage(testContext.getBrowserContext().newPage());
    }

    @After
    public void tearDown() {
        testContext.getBrowserContext().close();
        testContext.getBrowser().close();
        testContext.getPlaywright().close();
    }
}
