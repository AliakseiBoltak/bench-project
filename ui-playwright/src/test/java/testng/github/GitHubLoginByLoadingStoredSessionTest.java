package testng.github;

import com.google.inject.Inject;
import org.example.config.ConfigLoader;
import org.testng.annotations.Test;
import pages.github.GitHubMainPage;
import pages.github.GitHubPullRequestsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.PathConstants.PULL_REQUESTS_PATH;

public class GitHubLoginByLoadingStoredSessionTest extends GitHubBaseTest {

    @Inject
    public GitHubLoginByLoadingStoredSessionTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @Test
    public void testUserIsLoggedInAndGithubMainPageOpenedByLoadingStoredSession() {
        GitHubMainPage mainPage = new GitHubMainPage(page).navigateToHomePage(gitHubUrl);
        assertThat(mainPage.getHomePageTitleLocator()).isVisible();
    }

    @Test
    public void testUserIsLoggedInAndGithubPullRequestsPageOpenedByLoadingStoredSession() {
        GitHubPullRequestsPage pullRequestsPage = new GitHubPullRequestsPage(page)
                .navigatePullRequestsPage(gitHubUrl + PULL_REQUESTS_PATH);
        assertThat(pullRequestsPage.getPullRequestsPageLocator()).isVisible();
    }
}