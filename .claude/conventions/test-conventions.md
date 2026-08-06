# Test Writing Conventions

Framework: TestNG (not JUnit) + Allure + Guice, across all modules.

## Naming

- Test class names describe the scenario, end in `Test`: `CreateUserTest`, `LoginWithInvalidCredsTest`, `GetOrdersForUsersTest`.
- Test method names are descriptive camelCase, often ending in `Test`: `checkUserCreatedWithExpectedNameAndJobTest`, `getOrdersWithUsers`, `testGithubLoginWithInvalidCreds`. No fixed prefix/suffix rule is enforced — match the style of neighboring tests in the same module.
- Always set `@Test(description = "...")` with a plain-English sentence describing what the test verifies — used for Allure reporting. Omit only if every other test in that file already omits it (inconsistent today, but prefer adding it for new tests).

## Structure of a test class

```java
class CreateUserTest extends BaseAPITest {

    @Inject
    public CreateUserTest(ConfigLoader configLoader) {
        super(configLoader);
    }

    @DataProvider
    public Object[][] userDataProvider() { ... }

    @AfterMethod
    public void cleanUp() { ... }

    @Test(dataProvider = "userDataProvider", description = "...")
    void someTest(String name, String job) { ... }
}
```

- Extend the module's abstract base test class (`BaseAPITest`, `BaseUiTest`, `BaseDBTest`, or `GitHubBaseTest`).
- Test classes and their `@Test` methods default to package-private visibility (`class CreateUserTest`, `void checkUserCreatedWithExpectedNameAndJobTest`) — do not add `public` unless TestNG/Cucumber requires it.
- Parameterize with TestNG `@DataProvider` methods returning `Object[][]`, defined inline in the test class (see `CreateUserTest.userDataProvider`) or provided by a shared source (see `allUserTypes` used in `LoginWithInvalidCredsTest`).
- Clean up created data in `@AfterMethod`, calling a `protected` helper on the base class (e.g. `BaseAPITest.cleaningUpCreatedUser`).

## Assertions

Use **TestNG's `org.testng.Assert`** (static imports of `assertEquals`, `assertNotNull`, `assertFalse`, etc., or `org.testng.Assert.assertX`) — not AssertJ, not Hamcrest, not JUnit assertions. Always pass a failure message as the last argument:

```java
assertEquals(createUserResponse.getJob(), createUserRequest.getJob(), "Job does not match");
assertFalse(orderUserDataRecords.isEmpty(), "No orders found in the database.");
```

## Allure reporting

Narrate meaningful actions/business steps with `Allure.step("...")` — request bodies, IDs created, queries executed, records found:

```java
Allure.step("Created user ID: " + createdUserId);
Allure.step("Executing SQL query: " + SELECT_ORDERS_FOR_USERS_JOIN_QUERY);
```

Every module registers the relevant Allure TestNG/Selenide/Playwright listener in its suite XML (`AllureListener`, `AllureSelenideListener`, `AllurePlaywrightListener`) — don't add step reporting via a different mechanism (no custom logging framework).

## Imports

- Static-import constants used repeatedly in a test: `import static constants.Constants.*;`, `import static queries.QueriesBank.SELECT_ORDERS_FOR_USERS_JOIN_QUERY;`.
- Static-import TestNG assertion methods individually rather than the whole class: `import static org.testng.Assert.assertEquals;`.

## UI test flow (both `ui` and `ui-playwright`)

Drive page interactions through **mission/workflow helper classes** (e.g. `LoginMissions`) rather than chaining page objects directly in the test body when a multi-step flow is reused across tests:

```java
HomePage homePage = loginMissions
        .navigateToLoginPage(baseUrl)
        .loginWithCredentials(user.getUsername(), user.getPassword());
```

For one-off/simple flows, chaining page object methods directly in the test is acceptable (see Page Object conventions).
