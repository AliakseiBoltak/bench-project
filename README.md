# Test Automation Bench Project

A comprehensive Java framework for test automation.

## 🎓 About This Project

This project serves as an **educational sketch and reference example** of how robust test automation can be implemented across multiple completely different software domains. It is built as a **Maven multi-module project** to demonstrate architectural best practices: isolating domain-specific dependencies while sharing common utilities.

At the heart of the framework is the `core` module, which contains shared logic (like configuration loading, data generation, and dependency injection). This core is inherited and utilized by independent domain-specific testing modules.

### Domains Covered:
*   **API Testing:** RESTful service validation with RestAssured library.
*   **Database (DB) Testing:** Direct JDBC integrations and SQL state validation.
*   **Mobile Testing:** Appium-based automation for Android and iOS devices.
*   **UI Testing (Dual Approach):** The web automation layer is deliberately split into two distinct paradigms to showcase different industry standards:
    *   **Classic Approach:** Utilizing `Selenide` (WebDriver-based) in the `ui` module.
    *   **Modern Approach:** Utilizing `Playwright` + `Cucumber` (BDD) in the `ui-playwright` module.

## 🛠️ Technology Stack

*   **Language:** Java 17
*   **Build Tool:** Maven (Multi-module POM packaging)
*   **Test Runner:** TestNG 7.11.0
*   **Dependency Injection:** Google Guice 7.0.0 for Dependency Injection
*   **BDD Framework:** Cucumber 7.27.0 (with Guice support)
*   **Reporting:** Allure 2.24.0 (with TestNG and Cucumber JVM integrations)
*   **JSON Processing:** Google Gson 2.14.0 (for serialization/deserialization)
*   **Execution Plugins:** `maven-surefire-plugin` (for unit/fast tests) and `maven-failsafe-plugin` (for integration tests)

## 📁 Multi-Module Architecture

The framework consists of the following modules:

1.  **`core`**: The foundational shared library. Must be built and installed before running any tests.
2.  **`api`**: REST API test automation.
3.  **`db`**: Database integration tests.
4.  **`mobile`**: Android/iOS Appium tests.
5.  **`ui`**: Browser UI tests via Selenide.
6.  **`ui-playwright`**: Browser UI tests via Playwright and Cucumber BDD.

## 🚀 How to Build and Run Tests

**Before running any tests, you must first build and install the core module artifact.**

1. Import as a Maven project in your IDE.
2. Navigate to the `core` module directory:

    ```sh
    cd core
    ```

3. Build and install the core artifact to your local Maven repository by running this command:

    ```sh
    mvn clean install
    ```

After this, you can navigate to the desired module (for example, `api`, `ui-playwright`, etc.) and execute the tests as described below.

### 1. Run Tests

    mvn clean test

---

### 2. Restore Allure History from Previous Runs

To enable test trend statistics (history) in Allure reports, restore the history from your previous report using:

    mvn antrun:run@restore-allure-history

---

### 3. Generate Allure Report

    mvn allure:report

---

### 4. Save Allure History for Future Runs

After generating the report, save the current run's Allure history so trends will persist between runs:

    mvn antrun:run@copy-allure-history

---

### 5. Open Allure Report

    mvn allure:serve

---

> **Note:**  
> The Allure history steps (restore/save) are only required if you want to see trends/statistics across runs.  
> If you don’t need trends, you can skip steps 2 and 4.

---

## Notes

- **Allure Trends:**  
  To keep test trends and history visible in Allure reports across runs, always restore history before running tests and save history after generating the report.

- **Database Tests:**  
  Before running tests in the `db` module, execute the script `init_test_db.sql` to create the database and tables.

- **UI Playwright Tests:**  
  Before running tests in the `ui-playwright` module, you must install Playwright browsers by running:

      mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

  To run Playwright tests that require GitHub authentication (such as tests that use a stored session for GitHub),  
  you must update your `env.conf` file with valid GitHub credentials.

  After running your tests, you may have a Playwright trace file (for example, `github-login-trace.zip`).  
  To view and analyze this traced session in your browser, run the following command:

      mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace traces/github-login-trace.zip"

  This will open the Playwright Trace Viewer, allowing you to inspect every step of your test, including screenshots, network, console, and more.

- **Adjust Browser/Headless Mode:**  
  To change the default browser or headless mode for UI tests, pass JVM parameters when running tests. For example, this will run the UI tests in headed mode using Firefox instead of the default headless Chromium.:

      mvn clean test -Dheadless=false -Dbrowser=firefox

- **Mobile (Appium/Android/iOS) Tests:**  
  The `mobile` module drives a real Android device/emulator or iOS simulator via Appium and needs some one-time setup before running the tests will work:

    1. Install Node.js, then Appium and its drivers:

           npm install -g appium
           appium driver install uiautomator2
           # For iOS testing on macOS, XCUITest driver is also required:
           # appium driver install xcuitest

    2. Start the Appium server (leave it running in its own terminal):

           appium

       By default it listens on `http://127.0.0.1:4723`, matching `platform-default.appium.serverUrl` in `mobile/src/test/resources/env.conf`.

    3. Start an Android emulator (created via Android Studio's Device Manager or `avdmanager`) or connect a physical device with USB debugging enabled:

       ```sh
       emulator -avd <your_avd_name>          # list AVDs with: emulator -list-avds
       ```

       *(Note: iOS testing requires a macOS machine and an active iOS Simulator via Xcode).*

    4. Confirm it's visible to ADB before running tests:

           adb devices

       You should see a line like `emulator-5554   device` (not `offline`/`unauthorized`).

    5. Ensure your device configuration matches a platform block in `env.conf` (e.g.,`android-17`, `ios-17`), where you configure `deviceName`, `udid`, and `platformVersion`. For Android, get the platform version with `adb shell getprop ro.build.version.release`.

    6. Run the test by specifying the target platform profile (via `-Dplatform`) and optional TestNG suite XML (via `-DintegrationSuiteXmlFile`):

       **For Android (Default suite):**

           mvn -f mobile/pom.xml clean verify -Dplatform=android-17

       **For Android (Specific suite, e.g., Smoke):**

           mvn -f mobile/pom.xml clean verify -Dplatform=android-17 -DintegrationSuiteXmlFile=android-smoke-suite

       **For iOS:**

           mvn -f mobile/pom.xml clean verify -Dplatform=ios-17 -DintegrationSuiteXmlFile=ios-smoke-suite

**Why use `verify` instead of `test` for Mobile?**
The `mobile` module strictly separates fast architectural unit tests from heavy Appium UI integration tests using Maven's lifecycle phases:
* On the `test` phase, `maven-surefire-plugin` runs first to execute quick unit and architectural tests — specifically, validating that every declared page interface has its corresponding implementation for both Android and iOS.
* On the subsequent `integration-test` and `verify` phases, `maven-failsafe-plugin` takes over to run the heavy cross-platform Appium UI tests against real devices or emulators.

## ⚙️ Continuous Integration (CI/CD) & Reporting

This project is integrated with **GitHub Actions** to provide automated testing and reporting on every push and pull request to the `default` branch.

* **Automated Pipeline:** The CI pipeline automatically sets up JDK 17, installs project dependencies (including Playwright browsers), builds the `core` module, and runs the test suites (e.g., `ui-playwright`).
* **Allure Report Deployment:** After tests run, the pipeline automatically generates an Allure report. It dynamically restores previous test execution history from the `gh-pages` branch to maintain test trend statistics, and finally deploys the generated interactive report directly to **GitHub Pages** for easy viewing.

## 🤖 AI Integration & Agents

This project is equipped with AI assistance instructions and specialized autonomous agents to help maintain, run, and scale the framework.

### 1. GitHub Copilot Instructions
The repository includes predefined instructions for GitHub Copilot. These guidelines ensure Copilot understands the custom multi-module Maven structure, Guice dependency injection patterns, TestNG standards, and Page Object Model implementations (across Selenide and Playwright). This context forces the AI to match existing conventions rather than introducing divergent patterns.

### 2. AI Agents
The project features three configured autonomous AI agents to assist with code quality and architecture:
*   **`code-architect`**: A software architecture specialist responsible for design reviews, refactoring plans, and dependency analysis. It ensures modules remain loosely coupled, favors composition over inheritance, and plans breaking changes securely.
*   **`code-simplifier`**: A dedicated agent that reviews recently modified code to reduce complexity and redundancy. It streamlines logic and improves readability without altering external behavior or adding new dependencies.
*   **`staff-reviewer`**: A skeptical review agent that evaluates plans or architectural proposals *before* implementation. It pushes back on unnecessary complexity, highlights missing edge cases, and checks for security or performance concerns.

### 3. Claude Skills & Conventions
The repository contains specific configurations and operational rules for Anthropic's Claude:
*   **Skills (`skills/`)**: Contains executable workflows such as `run-tests` (smart execution of modules, handling required dependencies like `core` installation and test flags), `run-mobile-tests`, and `allure-report` (managing Allure histories and generating reports).
*   **Conventions (`conventions/`)**: Detailed architectural descriptions outlining module boundaries, Dependency Injection (Guice) rules, and test-writing standards. This ensures Claude maintains the strict stylistic and architectural consistency of the framework when helping with codebase modifications.

**Note:**  
For detailed info check README files in a particular module.