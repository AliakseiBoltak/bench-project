# Page Object Model

Used independently in `ui` (Selenide) and `ui-playwright` (Playwright) — there is no shared `BasePage` abstraction between them (different driver APIs), so each module reimplements the pattern locally. Follow the existing idiom per module rather than introducing a cross-module base class.

## `ui` (Selenide)

Page objects are stateless POJOs using static Selenide locators — no `Page`/`WebDriver` field to carry:

```java
public class LoginPage {
    public LoginPage enterEmail(String email) {
        $(By.id("login_field")).setValue(email);
        return this;
    }

    public HomePage clickOnLoginButton() {
        $(By.name("commit")).click();
        return new HomePage();
    }

    public boolean isLoginErrorVisible() {
        return $(By.xpath("...")).isDisplayed();
    }
}
```

- Fluent/chaining setters return `this` (same page) for same-page interactions, or a `new <NextPage>()` when the action navigates.
- Boolean query methods (`isXVisible`, `checkIfYLoaded`) return primitives directly from a Selenide condition check — no extra assertion inside the page object; assertions belong in the test.
- Locators are inlined at the call site (`By.id(...)`, `By.xpath(...)`) rather than extracted to constants in this module — follow this unless a locator is reused across multiple methods.

## `ui-playwright` (Playwright)

Page objects hold an explicit `Page` field passed via constructor (Playwright has no static driver handle):

```java
public class GitHubLoginPage {
    private final Page page;

    private static final String USERNAME_FIELD = "Username or email address";
    private static final String SIGN_IN_BUTTON = "Sign in";

    public GitHubLoginPage(Page page) {
        this.page = page;
    }

    public GitHubLoginPage enterUsername(String username) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName(USERNAME_FIELD)).fill(username);
        return this;
    }

    public GitHubMainPage clickSignIn() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(SIGN_IN_BUTTON).setExact(true)).click();
        return new GitHubMainPage(page);
    }
}
```

- Prefer `page.getByRole(...)` locators (accessible-role + name) over CSS/XPath selectors — matches Playwright's recommended locator strategy and is used consistently across `pages/github/*`.
- Extract repeated role-name strings to `private static final String` constants at the top of the page class (`USERNAME_FIELD`, `SIGN_IN_BUTTON`) rather than inlining literals in every method.
- Same fluent-chaining rule as `ui`: same-page action → `return this`; navigating action → `return new <NextPage>(page)`, threading the `page` field through.
- Composite state checks (e.g. `isLoginPageLoaded`) combine multiple `isVisible()` locator checks into one boolean — keep this logic in the page object, not the test.

## General

- One page object class per screen/page, named `<Thing>Page` (`LoginPage`, `HomePage`, `GitHubLoginPage`, `GitHubMainPage`, `GitHubPullRequestsPage`, `FileUploadPage`).
- Page objects live under `src/main/java/pages/` (or `pages/github/` for a sub-flow), never under `src/test/java` — they're reusable production code for the test suite, not test code itself.
- Page objects don't perform assertions and don't reference `ConfigLoader`/Guice — they take primitives/`Page` in the constructor and expose behavior methods only. URL/config values are resolved in the test or mission layer and passed in as method arguments (e.g. `navigateToLogin(gitHubUrl + LOGIN_PATH)`).
