package testng;

import com.google.inject.Inject;
import com.microsoft.playwright.*;
import testng.github.GitHubBaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.ConfigLoader;
import org.testng.annotations.*;
import pages.github.GitHubMainPage;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.PathConstants.TEST_VIDEO_RECORDING_PATH;

public class VideoTest extends GitHubBaseTest {

    private static final Logger LOGGER = LogManager.getLogger(VideoTest.class);

    @Inject
    public VideoTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Override
    protected boolean useStoredGitHubSession() {
        return false;
    }

    @BeforeMethod
    public void setVideo() {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get(TEST_VIDEO_RECORDING_PATH))
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
        assertThat(mainPage.getHomePageTitleLocator()).not().isVisible();
    }
}