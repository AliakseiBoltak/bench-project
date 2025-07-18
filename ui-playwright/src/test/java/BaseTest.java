import com.google.inject.Inject;
import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import listeners.AllurePlaywrightListener;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;

@Guice(modules = {CoreModule.class})
public class BaseTest {

    protected final ConfigLoader configLoader;
    protected final String baseUrl;
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @Inject
    public BaseTest(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.baseUrl = configLoader.getBaseUrl();
    }

    @BeforeMethod
    public void setUp() {
        Allure.step("Open browser");
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
        AllurePlaywrightListener.setPage(page);
    }

    @AfterMethod
    public void tearDown() {
        Allure.step("Close browser");
        browser.close();
        playwright.close();
        AllurePlaywrightListener.removePage();
    }

    protected void startTracing() {
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
    }

    protected void stopTracing(String tracePath) {
        context.tracing().stop(new Tracing.StopOptions().setPath(java.nio.file.Paths.get(tracePath)));
    }

}
