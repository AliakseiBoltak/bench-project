# Copilot Instructions

Multi-module Maven test automation framework, Java 17. Follow the conventions below — they describe what is already in this repo, not aspirational best practice. Match existing code rather than introducing new patterns.

## Modules

| Module            | Purpose             | Driver / lib           | Base test class  |
|-------------------|---------------------|------------------------|------------------|
| `core`            | Shared library      | Guice, Typesafe Config | —                |
| `api`             | REST API tests      | REST Assured           | `BaseAPITest`    |
| `api-apache-http` | REST API tests      | Apache HttpClient      | `BaseAPITest`    |
| `ui`              | Browser UI tests    | Selenide               | `BaseUiTest`     |
| `ui-playwright`   | UI tests + BDD      | Playwright, Cucumber   | `GitHubBaseTest` |
| `db`              | Database tests      | plain JDBC             | `BaseDBTest`     |
| `mobile`          | Android app tests   | Appium `java-client`   | `appium.BaseMobileTest` |

`core` (version `1.2-SNAPSHOT`, decoupled from the root aggregator's `1.0-SNAPSHOT`) must be installed before anything else runs.

## Build & test commands

```sh
cd core && mvn clean install          # required first / after any core change

mvn -f api/pom.xml clean test
mvn -f ui/pom.xml clean test
mvn -f ui-playwright/pom.xml clean test
mvn -f db/pom.xml clean test          # needs MySQL at localhost:3306
mvn -f mobile/pom.xml clean verify      # needs Appium server + Android device/emulator
```

Common overrides:

```sh
-DsuiteXmlFile=my-suite   # non-default TestNG suite
-Dheadless=false          # default: true
-Dbrowser=firefox         # Selenide: chrome|firefox|edge|safari — Playwright: chromium|firefox|webkit
-Denv=dev                 # env.conf profile: default|dev|test|uat
```

Allure: `mvn allure:report` (static) or `mvn allure:serve` (open in browser).

Playwright extras:

```sh
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace traces/github-login-trace.zip"
```

## Project layout

```
<module>/
  src/main/java/<packages>              # page objects, factories, listeners, constants, models
  src/test/java/...
  src/test/resources/env.conf           # HOCON: default/dev/test/uat blocks
  src/test/resources/suites/<module>-suite.xml
```

- Only `core` uses an `org.example.*` package root. Every other module uses short unprefixed packages (`pages`, `factory`, `constants`, `model`, `listeners`, `missions`, `queries`, `hooks`, `stepdefs`). Never add a reverse-domain prefix outside `core`.
- Test classes in `api`, `ui`, and `db` live in the **default (unnamed) package** — do not add a `package` statement there. `ui-playwright` tests *do* use packages (`testng`, `testng.github`, `cucumber`, `locatorhealing`).
- A test class not listed in its suite XML's `<classes>` block silently never runs. Suites use `parallel="classes"` and register an Allure listener.
- Adding a `ConfigLoader` getter means adding the key to every module `env.conf` that will call it — a missing key fails at runtime with no compile-time warning.

## Dependency injection (Guice)

- `@Guice(modules = {CoreModule.class})` goes on the **abstract base test class**, never on concrete tests.
- Base class constructor takes `ConfigLoader` and derives the single field the module needs (`baseUri`, `baseUrl`, `gitHubUrl`); `db` keeps `configLoader` itself.
- Every concrete test declares its own `@Inject` constructor calling `super(configLoader)`, even with no extra dependencies. Extra collaborators become constructor params assigned to `private final` fields.
- **Constructor injection only** — no field injection.
- Most bindings live in `core`'s `CoreModule`: `@Provides @Singleton` for expensive/stateful construction (I/O, config parsing), `bind(X.class).to(Y.class)` for stateless interface impls. 
- Module-specific Guice configurations (like `PageModule` in `mobile`) are officially permitted for specialized UI or domain-specific bindings. For base services, avoid creating separate Guice modules outside of `core`.

## Writing tests

TestNG (not JUnit) + Allure + Guice.

- Class names describe the scenario and end in `Test`; methods are descriptive camelCase.
- Always set `@Test(description = "...")` with a plain-English sentence — it feeds Allure. Omit only if every other test in that file already omits it (inconsistent today, but prefer adding it for new tests).
- Test classes and `@Test` methods are package-private by default; don't add `public` unless TestNG/Cucumber requires it.
- Parameterize with `@DataProvider` returning `Object[][]`. Clean up created data in `@AfterMethod` via a `protected` base-class helper.
- Assertions: **`org.testng.Assert`** only — not AssertJ, Hamcrest, or JUnit. Always pass a failure message as the last argument:
  ```java
  assertEquals(response.getJob(), request.getJob(), "Job does not match");
  ```
- Narrate meaningful actions with `Allure.step("...")` (IDs created, queries executed, records found). No custom logging framework.
- Static-import repeated constants (`import static constants.Constants.*;`) and individual assertion methods.
- For multi-step UI flows reused across tests, go through mission/workflow helpers (`LoginMissions`); direct page-object chaining is fine for one-off flows.

## Page objects

Live in `src/main/java/pages/` — never under `src/test/java`. One class per screen, named `<Thing>Page`. They perform no assertions and never touch `ConfigLoader`/Guice; URLs are resolved by the test or mission layer and passed in as arguments. Same-page actions `return this`; navigating actions return a `new <NextPage>(...)`.

- **`ui` (Selenide):** stateless POJOs using static locators (`$(By.id(...))`) inlined at the call site (unless reused). No driver field.
- **`ui-playwright`:** explicit `private final Page page` passed via constructor and threaded into the next page. Prefer `page.getByRole(...)` over CSS/XPath, and extract repeated role-name strings into `private static final String` constants.

## Models & constants

- DTOs use the Lombok stack `@Data @Builder @NoArgsConstructor @AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE)`. Declare fields **without** an explicit `private` — `@FieldDefaults` supplies it. Construct via the builder, not `new` + setters.
- `db` row-mapping models expose a static `mapRowsFromResultSet(ResultSet)` factory used as a method reference — mapping logic stays on the model, not in the DAO or test.
- Each module owns its `Constants` class: annotate with Lombok's `@UtilityClass` (supplies the private no-arg constructor and implicit `final`/`static`) and declare only `public static final` `SCREAMING_SNAKE_CASE` fields. Don't centralize module constants into `core` — `core`'s `Constants` is reserved for cross-module file paths.
- SQL strings are constants in `db`'s `queries.QueriesBank`; never inline SQL literals in tests.

## Build files

- Shared versions are root `<properties>` (`core.library.version`, `surefire.version`, `aspectj.version`, `allure.version`, `cucumber.version`). Bump the root property; never re-hardcode a version in a child POM. Known exception: `allure-rest-assured` in `api` and `allure-cucumber7-jvm` in root POM are hardcoded to `2.29.1`, diverging from `allure.version=2.24.0` - prefer aligning to `${allure.version}`.
- Depend on `core` with exactly `<version>${core.library.version}</version>`, never a literal version.
- Genuinely module-specific dependencies (`selenide`, `playwright`, `mysql-connector-j`) go in that module's own POM. Cross-module ones currently sit in the root `<dependencies>` (not `<dependencyManagement>`) — mirror that for consistency, but treat it as a known smell rather than something to expand.
- Don't remove the root `maven-antrun-plugin` executions that copy `allure-history/` in and out of `target/allure-results/history` — they're what make Allure trend graphs work.
- `core`'s `RandomUserGenerator` produces `users.json` during `core`'s install; `api`, `ui`, and `ui-playwright` copy it in via `maven-resources-plugin` at `generate-test-resources`. A new module needing this data adds the same copy execution rather than regenerating.

## Automation & AI Agents

Agent instructions are located in `.github/agents/`. When performing tasks, note:
- `code-architect`: Handles design reviews, refactoring plans, dependency analysis.
- `code-simplifier`: Simplifies recent commits without changing functionality (uses `mvn ... test` to verify).
- `staff-reviewer`: Skeptical reviewer to find edge-cases and over-engineering before implementation.

## Tooling / MCP Servers
- For Playwright locator healing, integrate an external MCP server into the current configuration (no Playwright locator healing is actively pre-installed natively, requires local agent/MCP setup).

## Module notes

- **`ui-playwright`** — `sessionState.json` stores a reusable GitHub SSO session (`GitHubLoginByLoadingStoredSessionTest` skips the login flow). Videos land in `videos/`, traces in `traces/`. `github.username` / `github.password` must be set in `env.conf`.
- **`db`** — initialize the schema from `db/src/test/resources/scripts/init_test_db.sql` before the first run.
- **`mobile`** — drives the device's pre-installed Settings app; no APK build/install needed. Test methods are tagged with TestNG groups `android`/`ios`; `mobile-suite.xml` has one `<test>` block per platform, each filtered to its group. Run a single platform with `-Dgroups=android` or `-Dgroups=ios` (passed through to Failsafe); without it, both blocks run as defined in the suite.
