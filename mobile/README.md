# Mobile Module

This module contains mobile UI automation tests for Android (and partially iOS) utilizing Appium `java-client`, Selenide Appium, TestNG, Guice for dependency injection, and Allure for reporting.

## 📁 Project Structure

```text
mobile/
├── src/main/java/
│   ├── actions/          # OS and device-level hardware interactions
│   ├── constants/        # Enums and constants (e.g., Platform)
│   ├── factory/          # Appium driver management and capabilities
│   ├── guice/            # Guice configuration and dynamic page provisioning
│   └── pages/            # Page Object Model layer
│       ├── implementations/
│       │   ├── android/  # Android-specific page objects and locators
│       │   └── ios/      # iOS-specific page objects and locators
│       └── interfaces/   # Cross-platform page contracts
└── src/test/java/
    └── appium/           # TestNG test classes and base setup
```

## 🏗️ Core Components and Responsibilities

To maintain a scalable and separated architecture, functionality must be placed in the appropriate layer.

### 1. `BaseMobileTest` (Test Lifecycle)
**Location:** `src/test/java/appium/BaseMobileTest.java`
*   **Responsibility:** Manages the global test lifecycle, driver startup/teardown, Allure listeners, and Selenide configurations.
*   **Do include:** `@BeforeSuite`, `@BeforeMethod`, `@AfterMethod`, TestNG configurations.
*   **Do NOT include:** Appium driver interaction logic, OS checks, page element assertions, or business logic.

### 2. `DeviceActions` (Device & OS Layer)
**Location:** `src/main/java/actions/DeviceActions.java`
*   **Responsibility:** Encapsulates system-level commands that bypass standard UI elements.
*   **Do include:** Raw coordinate gestures (swipes, taps), app state querying (foreground/background), device rotation, keyboard toggling, network toggling.
*   **Do NOT include:** Verifications of specific on-screen UI elements (like `SettingsPage` elements).

### 3. `BasePage` (UI Orchestration Layer)
**Location:** `src/main/java/pages/BasePage.java`
*   **Responsibility:** Provides standard wrapper methods around Selenide UI elements (waits, clicks, typing).
*   **Do include:** Methods taking a `SelenideElement` and performing UI commands (`isElementVisible`, `clickElement`, `swipeUpUntilVisible`).
*   **Do NOT include:** Physical pointer logic (delegated to `DeviceActions`) or app lifecycle transitions.

### 4. `interfaces` & `implementations` (Page Objects)
**Location:** `src/main/java/pages/interfaces/` & `src/main/java/pages/implementations/`
*   **Responsibility:** Define screen-specific locators and business workflows. Tests should strictly rely on interfaces (e.g., `SettingsPage`), while the Guice dynamically injects the platform-specific actualization (e.g., `AndroidSettingsPage`) based on test configuration.
*   **Do include:** `SelenideElement` locators (using `@AndroidFindBy`, `AppiumSelectors`, or `By`), and high-level behavioral domain methods (`swipeToSystemSettings()`).
*   **Do NOT include:** Any `WebDriver` driver initialization or device manipulation logic not tied to evaluating page elements.

## 🚀 Running Tests

Locally running the mobile module requires a running Appium server and an active Android Emulator or connected physical device. 

```sh
mvn -f mobile/pom.xml clean verify
```

To generate and view the report after execution:
```sh
mvn allure:report
mvn allure:serve
```
