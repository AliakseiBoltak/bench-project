import com.google.inject.Inject;
import com.microsoft.playwright.*;
import factory.BrowserFactory;
import io.qameta.allure.Allure;
import listeners.AllurePlaywrightListener;
import org.example.config.ConfigLoader;
import org.example.guice.CoreModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import pages.GitHubLoginPage;
import pages.GitHubMainPage;

import java.nio.file.Files;
import java.nio.file.Paths;

import static constants.PathConstants.LOGIN_PATH;

@Guice(modules = {CoreModule.class})
public class GitHubBaseTest {

    protected static final String STORAGE_STATE = "githubStorageState.json";
    protected final ConfigLoader configLoader;
    protected final String gitHubUrl;
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @Inject
    public GitHubBaseTest(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.gitHubUrl = configLoader.getGithubUrl();
    }

    protected boolean useStoredSession() {
        return true;
    }

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        browser = BrowserFactory.initBrowser(playwright);
        if (useStoredSession()) {
            if (Files.exists(Paths.get(STORAGE_STATE))) {
                context = browser.newContext(new Browser.NewContextOptions()
                        .setStorageStatePath(Paths.get(STORAGE_STATE)));
            } else {
                context = browser.newContext();
                page = context.newPage();
                gitHubUiLoginAndSaveStorage(configLoader.getGitHubUsername(), configLoader.getGitHubPassword());
                context = browser.newContext(new Browser.NewContextOptions()
                        .setStorageStatePath(Paths.get(STORAGE_STATE)));
            }
            page = context.newPage();
        } else {
            context = browser.newContext();
            page = context.newPage();
        }
        AllurePlaywrightListener.setPage(page);
        page.onConsoleMessage(msg -> {
            Allure.addAttachment("Console log", msg.text());
        });
    }

    @AfterMethod
    public void tearDown() {
        browser.close();
        playwright.close();
        AllurePlaywrightListener.removePage();
    }

    protected void gitHubUiLoginAndSaveStorage(String username, String password) {
        if (!Files.exists(Paths.get(STORAGE_STATE))) {
            context = browser.newContext();
            page = context.newPage();
            new GitHubLoginPage(page)
                    .navigateToLogin(gitHubUrl + LOGIN_PATH)
                    .enterUsername(username)
                    .enterPassword(password)
                    .clickSignIn();
            new GitHubMainPage(page).waitForHomePageToBeLoaded();
            context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get(STORAGE_STATE)));
            context.close();
        }
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