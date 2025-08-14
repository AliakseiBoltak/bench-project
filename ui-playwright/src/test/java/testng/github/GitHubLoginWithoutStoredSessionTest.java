package testng.github;

import com.google.inject.Inject;
import io.qameta.allure.Allure;
import org.example.config.ConfigLoader;
import org.testng.annotations.Test;
import pages.github.GitHubPullRequestsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.PathConstants.PULL_REQUESTS_PATH;

public class GitHubLoginWithoutStoredSessionTest extends GitHubBaseTest {

    @Inject
    public GitHubLoginWithoutStoredSessionTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Override
    protected boolean useStoredGitHubSession() {
        return false;
    }

    @Test()
    public void testUserIsNotLoggedInWhenUseStoredSessionIsFalse() {
        Allure.step("Trying to open pull requests page: " + gitHubUrl + PULL_REQUESTS_PATH);
        GitHubPullRequestsPage pullRequestsPage = new GitHubPullRequestsPage(page)
                .navigatePullRequestsPage(gitHubUrl + PULL_REQUESTS_PATH);
        assertThat(pullRequestsPage.getPullRequestsPageLocator()).not().isVisible();
    }
}