package pages.github;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class GitHubMainPage {

    private final Page page;
    private static final String HOME_PAGE_TITLE_LOCATOR = "//h2[text()='Home']";

    public GitHubMainPage(Page page) {
        this.page = page;
    }

    public Locator getHomePageTitleLocator() {
        return page.locator(HOME_PAGE_TITLE_LOCATOR);
    }

    public void waitForHomePageToBeLoaded() {
        page.locator(HOME_PAGE_TITLE_LOCATOR)
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public GitHubMainPage navigateToHomePage(String url) {
        page.navigate(url);
        return this;
    }

}