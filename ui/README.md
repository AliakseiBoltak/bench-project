# UI Module

This module contains browser-based UI automation tests utilizing Selenide, TestNG, Guice for dependency injection, and Allure for reporting[cite: 25, 29]. It relies on the `core` module, which must be installed prior to execution[cite: 29].

## 📁 Project Structure

    ui/
    ├── src/main/java/
    │   ├── factory/          
    │   │   └── BrowserFactory.java        # Manages WebDriver initialization and browser options[cite: 21]
    │   ├── listeners/        
    │   │   └── AllureSelenideListener.java # Custom listener for failure screenshots[cite: 22]
    │   ├── missions/         
    │   │   └── LoginMissions.java         # Reusable business workflows (e.g., login)[cite: 23]
    │   └── pages/            
    │       ├── HomePage.java              # Page Objects using Selenide locators[cite: 24]
    │       └── LoginPage.java             # Page Object for login[cite: 23]
    └── src/test/java/
        ├── BaseUiTest.java                # Abstract base test class[cite: 25]
        ├── LoginWithInvalidCredsTest.java # Concrete TestNG test class[cite: 26]
        └── resources/
            ├── env.conf                   # Environment configuration (HOCON)[cite: 28]
            ├── ui-suite.xml               # TestNG execution suite[cite: 27]
            └── users/
                └── users.json             # Test data copied from the core module[cite: 29, 30]

## 🏗️ Core Components

*   **`BaseUiTest`**: The abstract base class orchestrating the test lifecycle[cite: 25]. It sets up the `AllureSelenide` listener to capture screenshots, page sources, and browser logs[cite: 25]. It also maximizes the browser window before each test method and tears it down after execution[cite: 25].
*   **`BrowserFactory`**: Configures Selenide's underlying browser[cite: 21]. By default, it runs Google Chrome with headless mode enabled (`--headless=new`, `--no-sandbox`, `--disable-dev-shm-usage`)[cite: 21]. It can dynamically switch to Firefox, Edge, or Safari based on system properties[cite: 21].
*   **Page Objects & Missions**: The UI interaction layer is split between atomic Page Objects (like `HomePage`, which uses `$(By.xpath(...))` for element evaluation)[cite: 24] and "Missions" (like `LoginMissions`), which encapsulate multi-step user workflows[cite: 23].
*   **Listeners**: The `AllureSelenideListener` implements TestNG's `ITestListener` to automatically capture a screenshot (`Screenshots.takeScreenShotAsFile()`) and attach it to the Allure report upon test failure[cite: 22].
*   **Test Data Integration**: The `pom.xml` leverages the `maven-resources-plugin` to copy the `users.json` file from the `core` module during the `generate-test-resources` phase[cite: 29]. This file provides the credentials (username, password, and usertype) used by the `UserDataService` during tests[cite: 26, 30].

## 🚀 Execution & Configuration

The module executes tests in parallel at the class level via `ui-suite.xml`[cite: 27]. The default `baseUrl` points to `https://github.com/`, but setting the environment profile to `test` changes it to `https://test.github.com/`[cite: 28].

Run the default suite via Maven:

    mvn clean test

### Common JVM Overrides

You can control the execution environment and browser behavior using system properties:
*   **`-Dbrowser=<browser_name>`**: Switches the target browser (supported: `chrome`, `firefox`, `edge`, `safari`)[cite: 21].
*   **`-Dheadless=<true/false>`**: Toggles headless mode (default is `true`)[cite: 21].
*   **`-Denv=<profile>`**: Switches the environment block in `env.conf` (e.g., `-Denv=test`)[cite: 28].

Example of running tests in headed Firefox on the test environment:

    mvn clean test -Dbrowser=firefox -Dheadless=false -Denv=test

### Reporting

To generate and view the Allure report after execution (see root `README.md` for the full explanation of why `allure:report`, not `allure:serve`, is required before saving history):

    mvn antrun:run@restore-allure-history   # optional: restore trend history from previous runs
    mvn allure:report                       # generates target/site/allure-maven-plugin/ (required before saving history)
    mvn antrun:run@copy-allure-history       # optional: persist this run's history back to ui/allure-history/
    mvn allure:serve                        # opens the report in your browser

If you don't need trend history, skip straight to:

    mvn allure:serve
