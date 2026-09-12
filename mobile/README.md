# Mobile Module

This module contains mobile UI automation tests for Android and iOS utilizing Appium java-client, Selenide Appium, TestNG, Guice for dependency injection, and Allure for reporting.

## 📁 Project Structure

    mobile/
    ├── src/main/java/
    │   ├── actions/          # OS and device-level hardware interactions
    │   ├── config/           # Mobile configuration loader (HOCON-based)
    │   ├── constants/        # Enums and constants
    │   ├── exceptions/       # Custom runtime framework exceptions (e.g., MobileFrameworkException)
    │   ├── factory/          # Appium driver management and capabilities
    │   ├── guice/            # Guice configuration and dynamic page provisioning (PageModule, DynamicPageProvider)
    │   ├── model/            # Data transfer objects and test data models (e.g., SmsData)
    │   ├── steps/            # Intermediate business logic and assertions layer
    │   │   ├── common/       # Steps for screens whose behavior is identical on both platforms (e.g. SettingsSteps)
    │   │   ├── android/      # Steps for Android-exclusive screens (e.g. AndroidSmsNotificationsSteps)
    │   │   └── ios/          # Steps for iOS-exclusive screens (currently empty - no iOS-only screens yet)
    │   ├── utils/            # Utilities
    │   └── pages/            # Page Object Element definitions (Locators only)
    │       ├── interfaces/          # Cross-platform contracts - ONLY for screens with identical behavior (e.g. SettingsPage)
    │       └── implementations/
    │           ├── android/  # Android implementations of cross-platform interfaces, PLUS Android-exclusive
    │           │              #   pages that have no interface at all (e.g. AndroidSmsNotificationsPage)
    │           └── ios/      # iOS implementations of cross-platform interfaces (e.g. IOSSettingsPage)
    └── src/test/java/
        ├── appium/           # TestNG test classes
        │   ├── android/      # Android-specific tests
        │   ├── common/       # Cross-platform tests and base test setup
        │   └── ios/          # iOS-specific tests
        ├── unit/             # Architectural convention and unit tests
        └── resources/
            ├── data/         # JSON test data files
            ├── env.conf      # Unified configuration for backends and platforms
            └── suites/       # TestNG suite XML files (Android, iOS)

## 🏗️ Core Components and Responsibilities

To maintain a scalable and separated architecture, functionality must be placed in the appropriate layer.

### 1. BaseMobileTest (Test Lifecycle)
Location: src/test/java/appium/common/BaseMobileTest.java
- Responsibility: Manages the global test lifecycle, driver startup/teardown via MobileConfigLoader, Allure listeners, and Selenide configurations.
- Do include: @BeforeSuite, @BeforeMethod, @AfterMethod, TestNG configurations.
- Do NOT include: Appium driver interaction logic, OS checks, page element assertions, or business logic.

### 2. DeviceActions (Device & OS Layer)
Location: src/main/java/actions/DeviceActions.java
- Responsibility: Encapsulates system-level commands that bypass standard UI elements (e.g., simulating SMS on Android).
- Do include: Raw coordinate gestures (swipes, taps), app state querying (foreground/background), device rotation, SMS simulation.
- Do NOT include: Verifications of specific on-screen UI elements.

### 3. Steps (Business Logic & Verification Layer)
Location: src/main/java/steps/
- Responsibility: Houses all validation checks and user flows. This acts as the intermediate layer between tests and page element definitions.
- Do include: Relies on Page objects (interfaces for cross-platform screens, concrete classes for platform-exclusive screens) to fetch raw `SelenideElement`s. Uses declarative Allure `@Step` annotations on step methods. Performs fluid Selenide assertions (e.g., `shouldBe`, `shouldHave`) to ensure reporting features clear element details upon failure rather than generic boolean mismatch logs.
- Do NOT include: Hardcoded element locators or platform-specific driver calls.
- **Package layout mirrors the page-object pattern below:** steps for cross-platform screens live in `steps.common` and depend on the page *interface* (e.g. `SettingsSteps` → `SettingsPage`); steps for platform-exclusive screens live in `steps.android`/`steps.ios` and depend directly on the concrete page class (e.g. `AndroidSmsNotificationsSteps` → `AndroidSmsNotificationsPage`).

### 4. Page Objects (POM) — default pattern vs. platform-exclusive exception
Location: `src/main/java/pages/interfaces/` & `src/main/java/pages/implementations/`

**Default pattern — assume identical behavior until proven otherwise.** For any new screen, start by assuming Android and iOS behave the same way from the user's perspective. Declare one interface under `pages.interfaces` (e.g. `SettingsPage`) exposing the screen's elements/actions, then provide exactly two implementations — `pages.implementations.android.Android<Name>` and `pages.implementations.ios.IOS<Name>` — that differ only in their underlying locators. Steps and tests depend on the interface; `guice.PageModule` binds it through `guice.DynamicPageProvider`, which resolves the correct platform implementation at runtime via `MobileConfigLoader`.
- Do include: Static-like mappings of UI elements wrapped as `SelenideElement`.
- Do NOT include: Assertions, test flows, complex state machine validation, or device controls (e.g. swipes). Keep constructors empty or omitted.
- **Architectural Convention & Unit Testing:** To prevent missing platform implementations, the test suite includes `ArchitectureConventionTest` (located in `src/test/java/unit/ArchitectureConventionTest.java`). This unit test scans the `pages.interfaces` package via Guava ClassPath and programmatically verifies that for every interface (e.g., `SettingsPage`), corresponding physical classes exist under `pages.implementations.android.Android<Name>` and `pages.implementations.ios.IOS<Name>`.

**Exception — platform-exclusive features get no interface.** Some features simply don't exist on both platforms and there's no shared behavior to abstract. Creating an interface + a second implementation that just stubs everything out (returning `null`/no-ops) adds a layer of ceremony without any real cross-platform benefit. When a feature is platform-exclusive:
- Do NOT create an interface under `pages.interfaces` for it.
- Create a single concrete page class directly under the owning platform's package, e.g. `pages.implementations.android.AndroidSmsNotificationsPage` (a plain class, no `implements` clause).
- Its corresponding Steps class lives in that platform's `steps` sub-package (e.g. `steps.android.AndroidSmsNotificationsSteps`) and depends on the concrete page class directly — not through Guice's `DynamicPageProvider`, since there's nothing to dynamically resolve.
- `ArchitectureConventionTest` does not (and should not) flag this: it only scans `pages.interfaces`, so platform-exclusive pages are outside its scope by construction.

**Worked example — SMS notifications:** Appium cannot send/simulate SMS on the iOS Simulator at all (there is no API or facility for it), so there is nothing to verify in the notification shade on iOS in the first place — not just "no cross-platform behavior to abstract," but no iOS test scenario to write at all. Rather than force an interface with a fake `IOSSmsNotificationsPage` stub, the module only has `pages.implementations.android.AndroidSmsNotificationsPage` and `steps.android.AndroidSmsNotificationsSteps`, used exclusively by Android SMS tests. Compare this to `pages.interfaces.SettingsPage`, which *is* identical on both platforms and therefore follows the default interface + two-implementation pattern.

### 5. Utils, Models & Exceptions (Support Layer)
Location: src/main/java/utils/, src/main/java/model/, src/main/java/exceptions/
- Responsibility: Handle cross-cutting concerns like parsing JSON test datasets (JsonDataLoader), mapping domain entities (SmsData), and throwing framework-level errors with platform context (MobileFrameworkException).

## 🚀 Running Tests

Locally running the mobile module requires an active Android Emulator (or iOS Simulator), and specified configuration flags.

### Configuration Parameters
- -Dplatform: Specifies the device/OS profile from env.conf (e.g., android, ios).
- -DintegrationSuiteXmlFile: Specifies the TestNG suite XML file located in src/test/resources/suites/ (e.g., android-smoke-suite, common-mobile-smoke-suite).

### Command Examples

1. Run Appium tests on Android or IOS (Common suite):

   mvn clean verify -Dplatform=android
   mvn clean verify -Dplatform=ios

2. Run Android specific tests:

   mvn clean verify -Dplatform=android -DintegrationSuiteXmlFile=android-smoke-suite

3. Run IOS specific tests:

   mvn clean verify -Dplatform=ios -DintegrationSuiteXmlFile=ios-smoke-suite


### Reporting
Allure history/trend graphs are stored in `mobile/allure-history/` and must be restored/saved around report generation (see root `README.md` for the full explanation of why `allure:report`, not `allure:serve`, is required before saving history):

    mvn antrun:run@restore-allure-history   # optional: restore trend history from previous runs
    mvn allure:report                       # generates target/site/allure-maven-plugin/ (required before saving history)
    mvn antrun:run@copy-allure-history       # optional: persist this run's history back to mobile/allure-history/
    mvn allure:serve                        # opens the report in your browser

If you don't need trend history, skip straight to:

    mvn allure:serve
