package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class GitHubPullRequestsPage {

    private final Page page;
    private static final String PULL_REQUESTS_TITLE_LOCATOR =
            "//span[@class='AppHeader-context-item-label ' and text()='Pull Requests']";

    public GitHubPullRequestsPage(Page page) {
        this.page = page;
    }

    public boolean checkIfPullRequestsPageLoaded() {
        return page.locator(PULL_REQUESTS_TITLE_LOCATOR)
                .isVisible(new Locator.IsVisibleOptions());
    }

    public GitHubPullRequestsPage navigatePullRequestsPage(String url) {
        page.navigate(url);
        return this;
    }

}