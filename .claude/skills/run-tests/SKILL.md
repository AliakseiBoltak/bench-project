---
name: run-tests
description: Runs tests for one or more modules in this Maven multi-module test automation framework (core, api, api-apache-http, ui, ui-playwright, db) — handles the mandatory core install step and forwards suite/env/browser flags correctly. Trigger on "run tests", "run the api tests", "run ui suite", "test the db module", "run everything", etc.
---

# Run Tests

Runs one or more modules of this framework the way `CLAUDE.md` documents, without the caller having to remember the exact flag names or the `core`-first ordering rule.

## Modules

`core` (shared lib, not directly tested), `api` (REST Assured), `api-apache-http` (Apache HttpClient), `ui` (Selenide), `ui-playwright` (Playwright + Cucumber), `db` (JDBC/MySQL).

## Step 1 — always build `core` first

```sh
mvn -f core/pom.xml clean install -q
```

Required before running any other module: it installs `core:1.2-SNAPSHOT` to the local repo and regenerates `users.json` via `RandomUserGenerator`. Skip this step only if the user explicitly says `core` hasn't changed and this was already run earlier in the session.

## Step 2 — resolve which module(s) to run

- If the user names a specific module (or several), run only those.
- If the user says "run everything" / "run all tests" without specifying, run `api api-apache-http ui ui-playwright` in that order (mirrors `.github/workflows/maven.yml`) — do **not** include `db` in an "everything" run unless asked, since it requires a local MySQL instance the user may not have running.
- If the user asks for `db` specifically, first confirm (or check) that MySQL is reachable at `localhost:3306` and that `db/src/test/resources/scripts/init_test_db.sql` has been applied — if unsure, ask before running.

## Step 3 — build the command

Base form per module:

```sh
mvn -f <module>/pom.xml clean test
```

Translate any of these user requests into the matching system property, appended to the command:

| User asks for                         | Flag                          |
|----------------------------------------|--------------------------------|
| a specific suite / suite file         | `-DsuiteXmlFile=<name>`        |
| an environment profile (dev/test/uat) | `-Denv=<profile>`              |
| a specific browser                    | `-Dbrowser=<chrome/firefox/edge/safari \| chromium/firefox/webkit>` |
| headed / visible browser              | `-Dheadless=false`             |

Only pass `-Dbrowser` / `-Dheadless` for `ui` and `ui-playwright` — the other modules ignore them. Only pass `-DsuiteXmlFile` when the user wants a non-default suite; each module already has its own default (`api-suite`, `api-apache-http-suite`, `ui-suite`, `playwright-suite`, `db-suite`).

## Step 4 — run and report

Run modules sequentially (not in parallel — they share the same Maven local repo cache and some modules, like `ui`/`ui-playwright`, drive a real browser). After each module, report pass/fail counts and the names of failing tests with file:line where available — don't just paste raw Maven output. If a module fails, still continue to the next requested module rather than aborting the whole batch, unless the user asked to stop on first failure.

## Notes

- Never run `clean install` on a non-`core` module — only `core` needs to be installed to the local repo; the rest only need `test`.
- If `ui-playwright` tests are being run for the first time in this environment, Playwright browsers may need installing first: `mvn -f ui-playwright/pom.xml exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"`.
