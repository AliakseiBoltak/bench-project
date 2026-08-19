# Core Module

The `core` module serves as the foundational shared library for the entire automation framework. It provides dependency injection, environment configuration management, JSON data loading, and centralized logging/reporting listeners.

This module does not contain functional tests (`skipTests` is set to `true` in the `maven-surefire-plugin`). It is versioned independently (`1.2-SNAPSHOT`) and must be installed into your local Maven repository before executing tests in any other module.

## 📁 Project Structure

    core/
    ├── src/main/java/org/example/
    │   ├── config/
    │   │   └── ConfigLoader.java           # Typesafe Config reader
    │   ├── dao/
    │   │   ├── UserDataDao.java            # DAO interface for users
    │   │   └── UserDataJsonDao.java        # Concrete DAO loading users from JSON
    │   ├── exception/
    │   │   └── DataException.java          # Custom runtime exception
    │   ├── guice/
    │   │   └── CoreModule.java             # Main Guice bindings and singletons
    │   ├── listeners/
    │   │   └── AllureListener.java         # TestNG listener for Allure & Log4j2
    │   ├── loader/
    │   │   ├── DataLoader.java             # Interface for data deserialization
    │   │   └── JSONDataLoader.java         # Gson-based JSON deserializer
    │   ├── model/
    │   │   └── User.java                   # Lombok POJO for user credentials
    │   ├── service/
    │   │   ├── UserDataService.java        # Interface for user business logic
    │   │   └── UserDataJsonService.java    # Service filtering users by type
    │   └── utils/
    │       ├── FileManager.java            # NIO-based file reader/writer
    │       └── RandomUserGenerator.java    # Dynamic user test data generator
    └── pom.xml                             # Module dependencies and build plugins

## 🏗️ Core Components

### 1. Dependency Injection (`guice`)
*   `CoreModule` acts as the central Guice configuration.
*   It provides a `@Singleton` instance of `ConfigLoader`.
*   It binds interfaces to their concrete implementations (e.g., `DataLoader.class` to `JSONDataLoader.class`, `UserDataService.class` to `UserDataJsonService.class`).

### 2. Configuration Management (`config`)
*   `ConfigLoader` utilizes the Typesafe Config library to parse `env.conf`.
*   It dynamically resolves the active profile based on the environment (falling back to the `default` profile if the specified profile is missing) and exposes getters for `baseUrl`, `github.url`, and database credentials.

### 3. Data Management Layer (`loader`, `dao`, `service`, `model`)
*   **Loading:** `JSONDataLoader` utilizes `Gson` to deserialize JSON files into Java objects from either the classpath resources or absolute file paths.
*   **Data Access:** `UserDataJsonDao` fetches arrays of `User` objects via the data loader.
*   **Business Logic:** `UserDataJsonService` injects the DAO and filters the loaded users by their `usertype` property, throwing a `DataException` if no user of the requested type is found.
*   **Model:** The `User` class is a boilerplate-free Lombok POJO.

### 4. Dynamic Data Generation (`utils`)
*   The `RandomUserGenerator` generates randomized user credentials (usernames, passwords, and types) and writes them to a formatted JSON file using an `ObjectMapper`.
*   This generation is fully automated: the `exec-maven-plugin` executes this class during the `generate-test-resources` build phase, passing an argument of `10` to ensure a fresh pool of 10 users is created on every build.
*   `FileManager` provides utility methods to read and write file strings using `java.nio.file.Files`.

### 5. Logging and Reporting (`listeners`)
*   `AllureListener` implements TestNG's `ITestListener` to bridge test execution with both Log4j2 and Allure.
*   It automatically records test lifecycle events (start, success, failure, skipped) using `Allure.step()` and `LOGGER`.
*   On test execution, it automatically attaches test parameters to the report, and on test failure/skip, it attaches the error stacktrace and skip reasons.
*   The module's `pom.xml` explicitly includes `log4j-slf4j2-impl` to route SLF4J-based logging (from tools like Appium or Selenium) directly into Log4j2, preventing dropped logs.

## 🚀 Build Instructions

Because other modules depend on `core`, you must compile and install it into your local Maven `.m2` repository before executing tests elsewhere.

Navigate to the `core` directory and run:

    mvn clean install