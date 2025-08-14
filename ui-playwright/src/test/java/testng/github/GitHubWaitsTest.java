package testng.github;

import com.google.inject.Inject;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.github.GitHubPullRequestsPage;

import java.io.ByteArrayInputStream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.PathConstants.PULL_REQUESTS_PATH;

public class GitHubWaitsTest extends GitHubBaseTest {

    @Inject
    public GitHubWaitsTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @AfterMethod
    public void addAllureAttachment() {
        Allure.addAttachment("Screenshot confirming the page state",
                new ByteArrayInputStream(page.screenshot()));
    }

    @Test
    public void testPlaywrightWaitsPower() {
        GitHubPullRequestsPage pullRequestsPage = new GitHubPullRequestsPage(page)
                .navigatePullRequestsPage(gitHubUrl + PULL_REQUESTS_PATH);
        assertThat(pullRequestsPage.getPullRequestsPageLocator()).isVisible();
        pullRequestsPage.clickOnClosedPullRequests()
                .clickOnFirstClosedPullRequest();
        assertThat(pullRequestsPage.getMergedPullRequestsLocator()).isVisible();
    }
}