package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class GitHubLoginPage {

    private final Page page;

    private static final String USERNAME_FIELD = "Username or email address";
    private static final String PASSWORD_FIELD = "Password";
    private static final String SIGN_IN_BUTTON = "Sign in";
    private static final String CONTINUE_WITH_GOOGLE_BUTTON = "Continue with Google";
    private static final String SIGN_IN_WITH_GOOGLE_TEXT = "Sign in with Google";
    private static final String INCORRECT_LOGIN_ALERT = "Incorrect username or password.";

    public GitHubLoginPage(Page page) {
        this.page = page;
    }

    public GitHubLoginPage navigateToLogin(String url) {
        page.navigate(url);
        return this;
    }

    public GitHubLoginPage enterUsername(String username) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(USERNAME_FIELD)).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(USERNAME_FIELD)).fill(username);
        return this;
    }

    public GitHubLoginPage enterPassword(String password) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(PASSWORD_FIELD)).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(PASSWORD_FIELD)).fill(password);
        return this;
    }

    public GitHubLoginPage clickSignIn() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(SIGN_IN_BUTTON).setExact(true)).click();
        return this;
    }

    public String getAlertText() {
        return page.getByRole(AriaRole.ALERT).textContent();
    }

    public boolean isLoginErrorVisible() {
        return page.getByRole(AriaRole.ALERT).textContent().contains(INCORRECT_LOGIN_ALERT);
    }

    public GitHubLoginPage clickContinueWithGoogle() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(CONTINUE_WITH_GOOGLE_BUTTON)).click();
        return this;
    }

    public boolean isGoogleSignInVisible() {
        page.getByText(SIGN_IN_WITH_GOOGLE_TEXT).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        return page.getByText(SIGN_IN_WITH_GOOGLE_TEXT).isVisible();
    }

}
