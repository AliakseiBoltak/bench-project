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
    │   ├── guice/            # Guice configuration and dynamic page provisioning
    │   ├── model/            # Data transfer objects and test data models (e.g., SmsData)
    │   ├── steps/            # Intermediate business logic and assertions layer (BaseSteps, Steps subclasses)
    │   ├── utils/            # Utilities
    │   └── pages/            # Page Object Element definitions (Locators only)
    │       ├── implementations/
    │       │   ├── android/  # Android-specific page objects and element accessors
    │       │   └── ios/      # iOS-specific page objects and element accessors
    │       └── interfaces/   # Cross-platform page element contracts
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
- Do include: Relies directly on Page interfaces to fetch raw `SelenideElement`s. Uses declarative Allure `@Step` annotations on step methods. Performs fluid Selenide assertions (e.g., `shouldBe`, `shouldHave`) to ensure reporting features clear element details upon failure rather than generic boolean mismatch logs.
- Do NOT include: Hardcoded element locators or platform-specific driver calls.

### 4. interfaces & implementations (Page Elements POM)
Location: src/main/java/pages/interfaces/ & src/main/java/pages/implementations/
- Responsibility: Screen-specific representation containing element locators only. Intermediates (Steps) call getters defined inside the platform-agnostic interface, while Guice dynamically injects the platform-specific implementation.
- Do include: Static-like mappings of UI elements wrapped as `SelenideElement`.
- Do NOT include: Assertions, test flows, complex state machine validation, or device controls (e.g. swipes). Keeps constructors empty or omitted.
- **Architectural Convention & Unit Testing:** To prevent missing platform implementations, the test suite includes `ArchitectureConventionTest` (located in `src/test/java/unit/ArchitectureConventionTest.java`). This unit test scans the `pages.interfaces` package via Guava ClassPath and programmatically verifies that for every interface (e.g., `SettingsPage`), corresponding physical classes exist under `pages.implementations.android.Android<Name>` and `pages.implementations.ios.IOS<Name>`.

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

   mvn -f mobile/pom.xml clean verify -Dplatform=android
   mvn -f mobile/pom.xml clean verify -Dplatform=ios

2. Run Android specific tests:

   mvn -f mobile/pom.xml clean verify -Dplatform=android -DintegrationSuiteXmlFile=android-smoke-suite

3. Run IOS specific tests:

   mvn -f mobile/pom.xml clean verify -Dplatform=ios -DintegrationSuiteXmlFile=ios-smoke-suite


### Reporting
To generate and view the Allure report after execution:

    mvn allure:serve