package github;

import com.google.inject.Inject;
import org.example.config.ConfigLoader;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.github.GitHubMainPage;
import pages.github.GitHubPullRequestsPage;

import static constants.PathConstants.PULL_REQUESTS_PATH;

public class GitHubLoginByLoadingStoredSessionTest extends GitHubBaseTest {

    @Inject
    public GitHubLoginByLoadingStoredSessionTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Test
    public void testUserIsLoggedInAndGithubMainPageOpenedByLoadingStoredSession() {
        GitHubMainPage mainPage = new GitHubMainPage(page).navigateToHomePage(gitHubUrl);
        Assert.assertTrue(mainPage.checkIfHomePageLoaded(),
                "Main page should be loaded successfully. " +
                        "Please check the URL: " + gitHubUrl);
    }

    @Test
    public void testUserIsLoggedInAndGithubPullRequestsPageOpenedByLoadingStoredSession() {
        GitHubPullRequestsPage pullRequestsPage = new GitHubPullRequestsPage(page)
                .navigatePullRequestsPage(gitHubUrl + PULL_REQUESTS_PATH);
        Assert.assertTrue(pullRequestsPage.checkIfPullRequestsPageLoaded(),
                "Pull Requests page should be loaded successfully. " +
                "Please check the URL: " + gitHubUrl + PULL_REQUESTS_PATH);
    }

}