# API Module

This module contains REST API automation tests utilizing REST Assured, TestNG, Guice for dependency injection, and Allure for reporting.

## 📁 Project Structure

```text
api/
├── src/main/java/
│   ├── constants/        # Endpoint paths, headers, and static values (Constants)
│   └── model/            # Request/response DTOs
│       ├── requests/      # Lombok-based request bodies (e.g., CreateUserRequest)
│       └── responses/     # Lombok-based response bodies (e.g., CreateUserResponse)
└── src/test/java/         # TestNG test classes, live in the default (unnamed) package
    ├── BaseAPITest.java
    ├── CreateUserTest.java
    ├── CheckUsersJsonSchemaTest.java
    └── WaitForUserCreationTest.java
```

## 🏗️ Core Components and Responsibilities

### 1. `BaseAPITest` (Test Lifecycle & Shared Spec)
**Location:** `src/test/java/BaseAPITest.java`
*   **Responsibility:** Resolves `baseUri` from `ConfigLoader`, builds the shared `RequestSpecification` (base URI, content type, logging filters, Allure filter, API key header) in `@BeforeClass`, and exposes a `protected` cleanup helper (`cleaningUpCreatedUser`) for use in test `@AfterMethod` hooks.
*   **Do include:** `@Guice(modules = {CoreModule.class})`, constructor injection of `ConfigLoader`, request-spec construction, reusable cleanup helpers.
*   **Do NOT include:** Test-specific assertions or endpoint-specific request logic — that stays in the concrete test classes.

### 2. `model` (Requests & Responses)
**Location:** `src/main/java/model/requests/` & `src/main/java/model/responses/`
*   **Responsibility:** Plain DTOs describing request/response payloads.
*   **Do include:** Lombok stack `@Data @Builder @NoArgsConstructor @AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE)`; construct instances via the builder.
*   **Do NOT include:** REST Assured calls, assertions, or business logic — models are pure data holders.

### 3. `constants.Constants`
**Location:** `src/main/java/constants/Constants.java`
*   **Responsibility:** Centralizes endpoint URIs (e.g., `USER_URI`) and header names/values (e.g., `X_API_KEY_HEADER`) used across tests.
*   **Do include:** Lombok `@UtilityClass` annotation, `public static final` `SCREAMING_SNAKE_CASE` fields only.
*   **Do NOT include:** Cross-module values — those belong in `core`'s `Constants`.

### 4. Test Classes
**Location:** `src/test/java/` (default package)
*   **Responsibility:** Exercise the API under test via REST Assured, using the shared spec from `BaseAPITest`.
*   **Do include:** `@Test(description = "...")`, `org.testng.Assert` assertions with failure messages, `Allure.step("...")` narration, `@DataProvider` for parameterization, cleanup of created data via the base-class helper in `@AfterMethod`.
*   **Do NOT include:** A `package` statement (default/unnamed package by convention), AssertJ/Hamcrest/JUnit assertions.

## 🚀 Running Tests

```sh
cd core && mvn clean install     # required first / after any core change
mvn -f api/pom.xml clean test
```

Common overrides:

```sh
-DsuiteXmlFile=my-suite   # non-default TestNG suite
-Denv=dev                 # env.conf profile: default|dev|test|uat
```

To generate and view the Allure report after execution:
```sh
mvn allure:serve
```
