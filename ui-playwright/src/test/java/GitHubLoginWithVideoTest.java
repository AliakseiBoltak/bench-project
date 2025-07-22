import com.google.inject.Inject;
import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.GitHubMainPage;

import java.nio.file.Paths;

public class GitHubLoginWithVideoTest extends GitHubBaseTest {

    private static final Logger LOGGER = LogManager.getLogger(GitHubLoginWithVideoTest.class);

    @Inject
    public GitHubLoginWithVideoTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Override
    protected boolean useStoredSession() {
        return false;
    }

    @BeforeMethod
    public void setVideo() {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))
                .setRecordVideoSize(1280, 720);
        context = browser.newContext(contextOptions);
        page = context.newPage();
    }

    @AfterMethod
    public void logVideo() {
        if (page.video() != null) {
            String videoPath = page.video().path().toString();
            LOGGER.info("Video recorded at: " + videoPath);
        }
    }

    @Test
    public void testLoginWithVideoRecordingAndUseStoredSessionIsFalse() {
        GitHubMainPage mainPage = new GitHubMainPage(page).navigateToHomePage(gitHubUrl);
        Assert.assertFalse(mainPage.checkIfHomePageLoaded(),
                "User should be logged out when useStoredSession is false.");
    }

}