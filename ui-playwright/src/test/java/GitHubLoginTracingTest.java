import com.google.inject.Inject;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.GitHubMainPage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static constants.PathConstants.TRACE_SESSION_PATH;

public class GitHubLoginTracingTest extends GitHubBaseTest {

    private static final String TRACE_SESSION_FILE_PATH = TRACE_SESSION_PATH + "github-login-trace.zip";

    @Inject
    public GitHubLoginTracingTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Test
    public void testLoginWithTracing() {
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