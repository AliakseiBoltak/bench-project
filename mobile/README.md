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
│   ├── utils/            # Utilities for data loading (e.g., JsonDataLoader)
│   └── pages/            # Page Object Model layer
│       ├── implementations/
│       │   ├── android/  # Android-specific page objects and locators
│       │   └── ios/      # iOS-specific page objects and locators
│       └── interfaces/   # Cross-platform page contracts
└── src/test/java/
├── appium/           # TestNG test classes and base setup
└── resources/
├── data/         # JSON test data files
├── env.conf      # Unified configuration for backends and platforms
└── suites/       # TestNG suite XML files (Android, iOS)

## 🏗️ Core Components and Responsibilities

To maintain a scalable and separated architecture, functionality must be placed in the appropriate layer.

### 1. BaseMobileTest (Test Lifecycle)
Location: src/test/java/appium/BaseMobileTest.java
- Responsibility: Manages the global test lifecycle, driver startup/teardown via MobileConfigLoader, Allure listeners, and Selenide configurations.
- Do include: @BeforeSuite, @BeforeMethod, @AfterMethod, TestNG configurations.
- Do NOT include: Appium driver interaction logic, OS checks, page element assertions, or business logic.

### 2. DeviceActions (Device & OS Layer)
Location: src/main/java/actions/DeviceActions.java
- Responsibility: Encapsulates system-level commands that bypass standard UI elements (e.g., simulating SMS on Android).
- Do include: Raw coordinate gestures (swipes, taps), app state querying (foreground/background), device rotation, SMS simulation.
- Do NOT include: Verifications of specific on-screen UI elements.

### 3. BasePage (UI Orchestration Layer)
Location: src/main/java/pages/BasePage.java
- Responsibility: Provides standard wrapper methods around Selenide UI elements (waits, clicks, typing).
- Do include: Methods taking a SelenideElement and performing UI commands (isElementVisible, clickElement).
- Do NOT include: Physical pointer logic (delegated to DeviceActions) or app lifecycle transitions.

### 4. interfaces & implementations (Page Objects)
Location: src/main/java/pages/interfaces/ & src/main/java/pages/implementations/
- Responsibility: Define screen-specific locators and business workflows. Tests rely on interfaces, while Guice dynamically injects the platform-specific implementation.
- Do include: SelenideElement locators and high-level behavioral domain methods.
- Do NOT include: Any WebDriver initialization or device manipulation logic not tied to evaluating page elements.

### 5. Utils, Models & Exceptions (Support Layer)
Location: src/main/java/utils/, src/main/java/model/, src/main/java/exceptions/
- Responsibility: Handle cross-cutting concerns like parsing JSON test datasets (JsonDataLoader), mapping domain entities (SmsData), and throwing framework-level errors with platform context (MobileFrameworkException).

## 🚀 Running Tests

Locally running the mobile module requires a running Appium server, an active Android Emulator (or iOS Simulator), and specified configuration flags.

### Configuration Parameters
- -Dplatform: Specifies the device/OS profile from env.conf (e.g., android-16, android-17, ios-17). Defaults to platform-default.
- -DintegrationSuiteXmlFile: Specifies the TestNG suite XML file located in src/test/resources/suites/ (e.g., mobile-suite, ios-suite).

### Command Examples

1. Run Android tests on Android 17 (Default suite):
   mvn -f mobile/pom.xml clean verify -Dplatform=android-17

2. Run Android tests with a specific suite (e.g., Smoke suite):
   mvn -f mobile/pom.xml clean verify -Dplatform=android-16 -DintegrationSuiteXmlFile=smoke-suite

3. Run iOS tests:
   mvn -f mobile/pom.xml clean verify -Dplatform=ios-17 -DintegrationSuiteXmlFile=ios-suite

### Reporting
To generate and view the Allure report after execution:
mvn allure:report
mvn allure:serve