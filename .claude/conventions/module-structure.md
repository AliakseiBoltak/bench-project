# Module Structure

7 Maven modules under one aggregator root POM (`groupId org.example`, no shared `dependencyManagement` — see `dependency-management.md`):

| Module            | Purpose                          | Driver / lib             | Base test class  |
|-------------------|-----------------------------------|---------------------------|------------------|
| `core`            | Shared library                   | Guice, Typesafe Config    | —                |
| `api`             | REST API tests                   | REST Assured              | `BaseAPITest`    |
| `api-apache-http` | REST API tests                   | Apache HttpClient         | `BaseAPITest`    |
| `ui`              | Browser UI tests                 | Selenide                  | `BaseUiTest`     |
| `ui-playwright`   | Browser UI tests + BDD           | Playwright, Cucumber      | `GitHubBaseTest` |
| `db`              | Database tests                   | plain JDBC                | `BaseDBTest`     |
| `mobile`          | Android app tests                | Appium (`java-client`)    | `BaseMobileTest` |

`core` must be built (`mvn clean install`) before other modules — it installs `core:1.2-SNAPSHOT` to the local repo and generates `users.json` test data. `core`'s own version (`1.2-SNAPSHOT`) is intentionally decoupled from the root aggregator version (`1.0-SNAPSHOT`).

## Per-module layout

```
<module>/
  src/main/java/<packages>       # page objects, factories, listeners, constants, models — no shared package prefix
  src/test/java/<packages or default package>
  src/test/resources/env.conf    # HOCON profiles: default/dev/test/uat
  src/test/resources/suites/*-suite.xml   # TestNG suite, name pattern: <module>-suite.xml
```

Exception: `core` uses `org.example.*` as its package root (`config`, `guice`, `dao`, `loader`, `service`, `model`, `constants`, `exception`, `utils`, `listeners`). Every other module uses short, unprefixed packages (`pages`, `factory`, `constants`, `model`, `listeners`, `missions`, `queries`, `interfaces`, `hooks`, `context`, `stepdefs`) — do not introduce an `org.example` (or any reverse-domain) prefix outside `core`.

Test classes in `api`, `ui`, `db` live in the **default (unnamed) package** — do not add a `package` statement to new test classes in those modules. `ui-playwright` test classes do use packages (`testng`, `testng.github`, `cucumber`, `locatorhealing`) — follow that module's existing pattern when adding new Playwright tests.

## Config profiles

`env.conf` (HOCON, loaded via `core`'s `ConfigLoader`) defines a `default` block plus optional `dev`/`test`/`uat` overrides selected by `-Denv=<profile>` at runtime, falling back to `default` for missing keys:

```hocon
default {
  baseUrl = "https://reqres.in"
}
dev {
  baseUrl = "https://dev.reqres.in"
}
```

Each module's `env.conf` only defines the keys that module's `ConfigLoader` getters need (`api` → `baseUrl`; `db` → `db.*`; `ui-playwright` → `github.*`). When adding a new config getter to `ConfigLoader`, add the corresponding key to every module's `env.conf` that will call it, or the missing key throws at runtime with no compile-time warning.

## TestNG suite XML

One suite file per module at `src/test/resources/suites/<module>-suite.xml`, named `<Module> Suite`, referenced via the `-DsuiteXmlFile=<name>` Maven property (default set per-module in that module's `pom.xml`). Suites use `parallel="classes"` and register `AllureListener` (or the Playwright/Selenide equivalent) as a `<listener>`. Add new test classes to the relevant `<test>` block's `<classes>` list — a class that exists but isn't listed in the suite XML will silently never run (see `playwright-suite.xml`, which has several classes commented out).
