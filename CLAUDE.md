# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Multi-module Maven test automation framework in Java 17. Modules: `core` (shared library), `api` (REST Assured), `api-apache-http` (Apache HttpClient), `ui` (Selenide), `ui-playwright` (Playwright + Cucumber), `db` (JDBC/MySQL), `mobile` (Appium, Android).

## Conventions

Before writing or modifying code, check `.claude/conventions/` for established patterns — module/package structure, Guice DI wiring, test naming/assertion style, page object idioms, Lombok model shape, and POM/dependency handling. Follow what's documented there rather than introducing new patterns.

## Skills

Check `.claude/skills/` before running tests or working with Allure reports — `run-tests` wraps the module/suite/env/browser command combinations below into one flow, and `allure-report` covers report generation plus the `maven-antrun-plugin` wiring needed for history/trend graphs to actually work.

## Build & Test Commands

### First-time / after core changes
```sh
cd core && mvn clean install
```
This is **required** before running any other module — it installs the `core` 1.2-SNAPSHOT artifact to the local Maven repo and generates `users.json` via `RandomUserGenerator`.

### Run tests per module
```sh
mvn -f api/pom.xml clean test
mvn -f ui/pom.xml clean test
mvn -f ui-playwright/pom.xml clean test
mvn -f db/pom.xml clean test   # requires a running MySQL instance (see db module below)
mvn -f mobile/pom.xml clean test   # requires a running Appium server and Android device/emulator (see mobile module below)
```

### Override suite XML
```sh
mvn -f api/pom.xml clean test -DsuiteXmlFile=my-suite
```

### Browser options (ui and ui-playwright)
```sh
-Dheadless=false          # default: true
-Dbrowser=firefox         # Selenide: chrome|firefox|edge|safari; Playwright: chromium|firefox|webkit
```

### Environment profile
```sh
-Denv=dev    # selects block in src/test/resources/env.conf (default|dev|test|uat)
```

### Allure reports
```sh
mvn allure:report    # generate static report in target/site/allure-maven-plugin/
mvn allure:serve     # generate + open in browser
```

### Playwright browser install (ui-playwright, first time / CI)
```sh
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### View Playwright trace
```sh
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace traces/github-login-trace.zip"
```

## Architecture

### Dependency injection
All test base classes are annotated `@Guice(modules = {CoreModule.class})`. Guice binds providers for config (`EnvConfig`), users (`UserProvider`), and module-specific clients (e.g., `RestAssuredClient`, `SelenideConfig`, `PlaywrightFactory`).

### Configuration
Each module has `src/test/resources/env.conf` (HOCON/Typesafe Config) with profile blocks. The `-Denv` system property selects the active profile at runtime.

### User data
`core`'s `RandomUserGenerator` generates `users.json` (10 users) during `mvn install` and writes it to `src/main/resources/users/`. The `api`, `ui`, and `ui-playwright` modules copy this file into their own `src/test/resources/users/` via the `generate-test-resources` Maven phase — so `core` must be installed before these modules can run.

### Test base classes
Each module has its own base class that handles lifecycle setup/teardown:
- `api/` → `BaseAPITest`
- `api-apache-http/` → `BaseAPITest`
- `ui/` → `BaseUiTest`
- `db/` → `BaseDBTest`
- `ui-playwright/` → `GitHubBaseTest`
- `mobile/` → `appium.BaseMobileTest`

### Page Object Model
Used in both `ui` (Selenide) and `ui-playwright` (Playwright). Pages live in each module's `src/main/java/pages/`.

### Cucumber (ui-playwright only)
Feature files are in `src/test/resources/features/`, step definitions in `src/main/java/stepdefs/`, hooks in `src/main/java/hooks/`. The Cucumber runner (`AllCucumberTests`) is currently commented out of `playwright-suite.xml`.

### Allure reporting
All modules use `allure-testng` (or `allure-cucumber7-jvm` for Playwright). History is stored per-module in `allure-history/` to enable trend graphs across runs.

## Module-specific Notes

### `ui-playwright`
- `sessionState.json` — saved Playwright browser session for GitHub SSO reuse. Tests that use it (`GitHubLoginByLoadingStoredSessionTest`) skip the full login flow.
- Videos land in `videos/`, traces in `traces/`.
- GitHub credentials (`github.username`, `github.password`) must be set in `env.conf` for auth tests to pass.

### `db`
- Requires a running MySQL instance at `localhost:3306`.
- Initialize the schema before first run: `db/src/test/resources/scripts/init_test_db.sql` — creates `test_db`, `test_user`, `users`, and `orders` tables with sample data.
- The `db` module is intentionally excluded from CI (no MySQL in CI).

### `mobile`
- Requires a running Appium server (default `http://127.0.0.1:4723`, see `appium.serverUrl` in `env.conf`) and an attached/running Android device or emulator matching `appium.deviceName`.
- The single test (`appium.VerifySettingsAppIT`) drives the device's pre-installed Settings app (`appium.appPackage`/`appActivity`) — no APK build/install step is required to run it.
- The `mobile` module is intentionally excluded from CI (no Android device/emulator or Appium server in CI), same rationale as `db`.

## CI/CD

`.github/workflows/maven.yml` triggers on push/PR to the `default` branch. It runs `core install`, then `api`, `api-apache-http`, `ui`, and `ui-playwright` tests in sequence on `ubuntu-latest` (JDK 17 Temurin). The `db` and `mobile` modules are excluded.
