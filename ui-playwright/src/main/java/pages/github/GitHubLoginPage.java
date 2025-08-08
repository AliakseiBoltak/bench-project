package pages.github;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class GitHubLoginPage {

    private final Page page;

    private static final String USERNAME_FIELD = "Username or email address";
    private static final String PASSWORD_FIELD = "Password";
    private static final String SIGN_IN_BUTTON = "Sign in";
    public GitHubLoginPage(Page page) {
        this.page = page;
    }

    public GitHubLoginPage navigateToLogin(String url) {
        page.navigate(url);
        return this;
    }

    public GitHubLoginPage enterUsername(String username) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(USERNAME_FIELD)).fill(username);
        return this;
    }

    public GitHubLoginPage enterPassword(String password) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(PASSWORD_FIELD)).fill(password);
        return this;
    }

    public GitHubMainPage clickSignIn() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(SIGN_IN_BUTTON).setExact(true)).click();
        return new GitHubMainPage(page);
    }

    public boolean isLoginPageLoaded() {
        boolean isUsernameVisible = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName(USERNAME_FIELD)).isVisible();
        boolean isPasswordVisible = page.getByRole(AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName(PASSWORD_FIELD)).isVisible();
        boolean isSignInVisible = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(SIGN_IN_BUTTON).setExact(true)).isVisible();
        return isUsernameVisible && isPasswordVisible && isSignInVisible;
    }

}
