# UI Playwright Module

This module contains browser UI automation tests utilizing Playwright, Cucumber (BDD), TestNG, Guice for dependency injection, and Allure for reporting.

## 📁 Project Structure

```text
ui-playwright/
├── src/main/java/
│   ├── constants/        # Path/URL and locator-name constants
│   ├── context/          # Shared Cucumber scenario state (CucumberTestContext)
│   ├── factory/          # Playwright/Browser initialization (BrowserFactory)
│   ├── hooks/            # Cucumber lifecycle hooks (CucumberPlaywrightHooks)
│   ├── listeners/        # Allure/Playwright listeners (AllurePlaywrightListener)
│   ├── pages/            # Page Object Model layer
│   │   └── github/        # GitHub-specific page objects
│   └── stepdefs/         # Cucumber step definitions (FileUploadStepDefs)
└── src/test/java/
    ├── cucumber/         # AllCucumberTests runner
    ├── locatorhealing/   # Locator-healing focused tests
    └── testng/           # Plain TestNG Playwright tests
        └── github/        # GitHubBaseTest and GitHub-flow tests
```

## 🏗️ Core Components and Responsibilities

### 1. `GitHubBaseTest` (TestNG Test Lifecycle)
**Location:** `src/test/java/testng/github/GitHubBaseTest.java`
*   **Responsibility:** Manages Playwright lifecycle (`Playwright`, `Browser`, `BrowserContext`, `Page`) per test method, resolves `gitHubUrl` from `ConfigLoader`, and optionally reuses a stored GitHub session (`sessionState.json`) to skip the login flow.
*   **Do include:** `@Guice(modules = {CoreModule.class})`, `@BeforeMethod`/`@AfterMethod` setup/teardown, `BrowserFactory` usage, Allure listener wiring.
*   **Do NOT include:** Page-specific locators or assertions — those belong in `pages/` and the concrete test classes.

### 2. `CucumberPlaywrightHooks` & `CucumberTestContext` (BDD Lifecycle)
**Location:** `src/main/java/hooks/` & `src/main/java/context/`
*   **Responsibility:** `CucumberPlaywrightHooks` manages Playwright/browser setup and teardown around scenarios (`@Before`/`@After`); `CucumberTestContext` carries shared state (e.g., the current `Page`) between step definition classes for a scenario.
*   **Do include:** Scenario-scoped resource management, context injection.
*   **Do NOT include:** Step-specific business logic or assertions.

### 3. `factory.BrowserFactory` (Browser Initialization)
**Location:** `src/main/java/factory/BrowserFactory.java`
*   **Responsibility:** Centralizes `Browser` creation from a `Playwright` instance, honoring `-Dbrowser` (chromium|firefox|webkit) and `-Dheadless` overrides.
*   **Do NOT include:** Page-object or test-specific logic.

### 4. `pages` (Page Object Model)
**Location:** `src/main/java/pages/`
*   **Responsibility:** One class per screen (e.g., `GitHubLoginPage`, `GitHubMainPage`, `GitHubPullRequestsPage`, `FileUploadPage`), holding an explicit `private final Page page` passed via constructor and threaded into the next page on navigation.
*   **Do include:** `page.getByRole(...)` preferred over CSS/XPath, repeated role-name strings extracted into `private static final String` constants, same-page actions return `this`, navigating actions return a `new <NextPage>(page)`.
*   **Do NOT include:** Assertions, `ConfigLoader`/Guice usage, or driver/browser lifecycle management.

### 5. `stepdefs` (Cucumber Step Definitions)
**Location:** `src/main/java/stepdefs/`
*   **Responsibility:** Translate Gherkin steps into calls against page objects and `CucumberTestContext`.
*   **Do include:** Thin glue code delegating to page objects; `Allure.step("...")` narration where useful.
*   **Do NOT include:** Direct Playwright locator logic that belongs on a page object.

### 6. Test Runners & Classes
**Location:** `src/test/java/testng/`, `src/test/java/cucumber/`, `src/test/java/locatorhealing/`
*   **Responsibility:** `testng` and `testng.github` hold plain TestNG Playwright tests (tracing, offline errors, response interception, video capture, GitHub login flows); `cucumber.AllCucumberTests` is the Cucumber runner; `locatorhealing` holds tests exercising the MCP-based locator-healing setup.
*   **Do include:** `@Test(description = "...")`, `org.testng.Assert` assertions with failure messages, `Allure.step("...")` narration.
*   **Do NOT include:** A reverse-domain package prefix — packages here are short and unprefixed (`testng`, `testng.github`, `cucumber`, `locatorhealing`).

## 🔧 Locator Healing (MCP)

Playwright locator healing is not natively pre-installed; it requires integrating an external MCP server (see `docker-compose.mcp.yml`) and wiring the `locatorhealing` tests/config to point at it.

## 🚀 Running Tests

```sh
cd core && mvn clean install               # required first / after any core change
mvn -f ui-playwright/pom.xml clean test
```

Common overrides:

```sh
-DsuiteXmlFile=my-suite   # non-default TestNG suite
-Dheadless=false          # default: true
-Dbrowser=firefox         # chromium|firefox|webkit
-Denv=dev                 # env.conf profile: default|dev|test|uat
```

Playwright extras:

```sh
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace traces/github-login-trace.zip"
```

To generate and view the Allure report after execution:
```sh
mvn allure:report
mvn allure:serve
```

## 📝 Notes

- `sessionState.json` stores a reusable GitHub SSO session; `GitHubLoginByLoadingStoredSessionTest` skips the login flow when it's present.
- Videos land in `videos/`, traces in `traces/`.
- `github.username` / `github.password` must be set in `env.conf`.

