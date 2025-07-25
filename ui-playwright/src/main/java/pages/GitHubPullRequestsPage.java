package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class GitHubPullRequestsPage {

    private final Page page;
    private static final String PULL_REQUESTS_TITLE_LOCATOR =
            "//span[@class='AppHeader-context-item-label ' and text()='Pull Requests']";
    private static final String CLOSED_PULL_REQUESTS_LOCATOR =
            "//a[@data-ga-click='Pull Requests, Table state, Closed']";

    private static final String CLOSED_PULL_REQUESTS_LIST_LOCATOR =
            "a[data-hovercard-type='pull_request']";

    private static final String MERGED_PULL_REQUESTS_SPAN_LOCATOR =
            "//span[contains(@class,'State--merged') and @title='Status: Merged']";

    public GitHubPullRequestsPage(Page page) {
        this.page = page;
    }

    public boolean checkIfPullRequestsPageLoaded() {
        return page.locator(PULL_REQUESTS_TITLE_LOCATOR).isVisible();
    }

    public GitHubPullRequestsPage navigatePullRequestsPage(String url) {
        page.navigate(url);
        return this;
    }

    public GitHubPullRequestsPage clickOnClosedPullRequests() {
        page.locator(CLOSED_PULL_REQUESTS_LOCATOR)
                .filter(new Locator.FilterOptions().setVisible(true))
                .first()
                .click();
        return this;
    }

    public GitHubPullRequestsPage clickOnFirstClosedPullRequest() {
        Locator closedPrLinks = page.locator(CLOSED_PULL_REQUESTS_LIST_LOCATOR);
        closedPrLinks.first().click();
        return this;
    }

    public boolean checkIfMergePullRequestTextDisplayed() {
        Locator mergedBadge = page.locator(MERGED_PULL_REQUESTS_SPAN_LOCATOR)
                .filter(new Locator.FilterOptions().setVisible(true))
                .first();
        mergedBadge.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return mergedBadge.isVisible();
    }

}