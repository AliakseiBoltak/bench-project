package github;

import com.google.inject.Inject;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.github.GitHubPullRequestsPage;

import java.io.ByteArrayInputStream;

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

        Assert.assertTrue(pullRequestsPage.checkIfPullRequestsPageLoaded(),
                "Pull Requests page should be loaded and visible!");

        pullRequestsPage.clickOnClosedPullRequests()
                .clickOnFirstClosedPullRequest();

        Assert.assertTrue(pullRequestsPage.checkIfMergePullRequestTextDisplayed(),
                "Merged pull request text should be displayed!");

    }
}