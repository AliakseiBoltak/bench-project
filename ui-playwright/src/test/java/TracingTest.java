import com.google.inject.Inject;
import github.GitHubBaseTest;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.github.GitHubMainPage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static constants.PathConstants.TRACE_SESSION_PATH;

public class TracingTest extends GitHubBaseTest {

    private static final String TRACE_SESSION_FILE_PATH = TRACE_SESSION_PATH + "github-login-trace.zip";

    @Inject
    public TracingTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Override
    protected boolean useStoredGitHubSession() {
        return false;
    }


    @Test
    public void testHomePageNavigationWithTracing() {
        startTracing();
        new GitHubMainPage(page).navigateToHomePage(gitHubUrl);
        stopTracing(TRACE_SESSION_FILE_PATH);
        Assert.assertTrue(new File(TRACE_SESSION_FILE_PATH).exists() &&
                        new File(TRACE_SESSION_FILE_PATH).length() > 0,
                "Trace file does not exist or is empty at:: " + TRACE_SESSION_FILE_PATH);
    }

    @AfterMethod
    public void addTracedSessionToAllure() {
        Allure.step("Traced GitHub login session",
                () -> Allure.addAttachment("Trace Session", "application/zip",
                        Files.newInputStream(Paths.get(TRACE_SESSION_FILE_PATH)), "zip")
        );
    }
}