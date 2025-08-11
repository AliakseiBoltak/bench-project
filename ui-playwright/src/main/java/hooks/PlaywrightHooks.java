package hooks;

import com.google.inject.Inject;
import com.microsoft.playwright.Playwright;
import context.TestContext;
import factory.BrowserFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class PlaywrightHooks {

    private final TestContext testContext;

    @Inject
    public PlaywrightHooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void setUp() {
        testContext.setPlaywright(Playwright.create());
        testContext.setBrowser(BrowserFactory.initBrowser(testContext.getPlaywright()));
        testContext.setBrowserContext(testContext.getBrowser().newContext());
        testContext.setPage(testContext.getBrowserContext().newPage());
        System.out.println("======================");
        System.out.println("TestContext in Hooks: " + testContext.hashCode());
        System.out.println("Playwright in Hooks: " + testContext.getPlaywright());
        System.out.println("Browser in Hooks: " + testContext.getBrowser());
        System.out.println("BrowserContext in Hooks: " + testContext.getBrowserContext());
        System.out.println("Page in Hooks: " + testContext.getPage());
        System.out.println("======================");
    }

    @After
    public void tearDown() {
        testContext.getBrowserContext().close();
        testContext.getBrowser().close();
        testContext.getPlaywright().close();
    }
}
